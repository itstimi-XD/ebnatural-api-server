package ebnatural.bizcurator.apiserver.service;

import ebnatural.bizcurator.apiserver.common.exception.custom.CategoryNotFoundException;
import ebnatural.bizcurator.apiserver.common.exception.custom.ProductNotFoundException;
import ebnatural.bizcurator.apiserver.domain.Category;
import ebnatural.bizcurator.apiserver.domain.Manufacturer;
import ebnatural.bizcurator.apiserver.domain.Product;
import ebnatural.bizcurator.apiserver.domain.ProductImage;
import ebnatural.bizcurator.apiserver.dto.ProductAdminDetailDto;
import ebnatural.bizcurator.apiserver.dto.ProductAdminListDto;
import ebnatural.bizcurator.apiserver.dto.ProductDetailDto;
import ebnatural.bizcurator.apiserver.dto.ProductListDto;
import ebnatural.bizcurator.apiserver.dto.request.ProductRequest;
import ebnatural.bizcurator.apiserver.repository.CategoryRepository;
import ebnatural.bizcurator.apiserver.repository.ManufacturerRepository;
import ebnatural.bizcurator.apiserver.repository.ProductImageRepository;
import ebnatural.bizcurator.apiserver.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;  // CategoryRepository 인스턴스 추가
    private final ProductImageRepository productImageRepository;
    private final S3ImageUploadService s3ImageUploadService;
    private final ManufacturerRepository manufacturerRepository;

    @Value("${cloud.aws.s3.product-dir}")
    private String productDir;
    @Transactional
    public void registerProduct(ProductRequest productRequest, MultipartFile mainImage, MultipartFile detailImage){
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category id"));
        Manufacturer manufacturer = manufacturerRepository.findOrCreateManufacturer(productRequest.getManufacturerName());
        Product product = productRepository.save(productRequest.toEntity(category, manufacturer));

        String mainImageUrl = s3ImageUploadService.uploadImage(productDir, mainImage);
        String detailImageUrl = s3ImageUploadService.uploadImage(productDir, detailImage);

        ProductImage mainProductImage = ProductImage.createProductImage(product, mainImageUrl, "Y");
        ProductImage detailProductImage = ProductImage.createProductImage(product, detailImageUrl, "N");

        productImageRepository.save(mainProductImage);
        productImageRepository.save(detailProductImage);

        product.getProductImages().add(mainProductImage);
        product.getProductImages().add(detailProductImage);

    }
    @Transactional
    public void updateProduct(Long productId, ProductRequest productRequest, MultipartFile mainImage, MultipartFile detailImage) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category id"));
        Manufacturer manufacturer = manufacturerRepository.findOrCreateManufacturer(productRequest.getManufacturerName());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));

        // Update Product entity with new values from productRequest
        product.update(productRequest, category, manufacturer);

        String mainImageUrl = null;
        String detailImageUrl = null;

        // Get current main image and detail image URL
        for (ProductImage productImage : product.getProductImages()) {
            if ("Y".equals(productImage.getRepimgYn())) {
                mainImageUrl = productImage.getImgUrl();
            } else if ("N".equals(productImage.getRepimgYn())) {
                detailImageUrl = productImage.getImgUrl();
            }
        }

        // If new images are provided, delete the old ones and upload the new ones
        if (mainImage != null && !mainImage.isEmpty()) {
            s3ImageUploadService.deleteFile(mainImageUrl); // 기존 이미지 삭제
            mainImageUrl = s3ImageUploadService.uploadImage(productDir, mainImage);
        }
        if (detailImage != null && !detailImage.isEmpty()) {
            s3ImageUploadService.deleteFile(detailImageUrl); // 기존 이미지 삭제
            detailImageUrl = s3ImageUploadService.uploadImage(productDir, detailImage);
        }

        // Update ProductImages with new URLs
        product.updateImages(mainImageUrl, detailImageUrl);
    }


    public List<ProductAdminListDto> getAdminProducts(String keyword){
        List<ProductAdminListDto> products = productRepository.findAdminProducts(keyword);
        if(products.isEmpty()){
            throw new ProductNotFoundException();
        }
        return products;
    }

    public List<ProductListDto> getProducts(Long categoryId, String sort) {
        if (categoryId != 0 && !categoryRepository.existsById(categoryId)) {  // 인스턴스를 사용하여 existsById 메소드 호출 및 categoryId가 존재할 때만
            throw new CategoryNotFoundException();
        }return productRepository.findByCategoryId(categoryId, sort);
    }
    public List<ProductListDto> searchProducts(String keyword, String sort) {
        List<ProductListDto> products = productRepository.searchByKeyword(keyword, sort);

        if (products.isEmpty()) {
            throw new ProductNotFoundException();
        }

        return products;
    }
    public ProductAdminDetailDto getAdminProductDetail(Long productId){
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }
        ProductAdminDetailDto productDetail = productRepository.findAdminProductDetail(productId);
        if (productDetail == null) {
            throw new ProductNotFoundException();
        }
        return productDetail;
    }
    @Transactional
    public ProductDetailDto getProductDetail(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }

        productRepository.incrementClicks(productId);

        ProductDetailDto productDetail = productRepository.findDetailById(productId);
        if (productDetail == null) {
            throw new ProductNotFoundException();
        }
        return productDetail;
    }

    public List<ProductListDto> getTop3ProductsWeekly() {
        return productRepository.findTop3ByWeeklyClicks();
    }

    public List<ProductListDto> getTop3ProductsMonthly() {
        return productRepository.findTop3ByMonthlyClicks();
    }

    /**
     * 물품의 이미지들을 반환한다.
     * @param productId
     * @return
     */
    public List<ProductImage> getProductImages(Long productId) {
        List<ProductImage> productImages = productImageRepository.findAllByProductId(productId);
        if(productImages.isEmpty()){
            return null;
        }

        return productImages;
    }

    /**
     * 물품의 메인 이미지를 반환한다.
     * 메인이미지는 0번째 이미지라고 한다.
     * @param productId
     * @return
     */
    public ProductImage getProductMainImage(Long productId) {
        List<ProductImage> productImages = this.getProductImages(productId);
        if(productImages.isEmpty()){
            return null;
        }

        return productImages.get(0);
    }
}
