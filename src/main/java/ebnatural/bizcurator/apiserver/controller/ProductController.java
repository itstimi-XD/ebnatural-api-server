package ebnatural.bizcurator.apiserver.controller;

import ebnatural.bizcurator.apiserver.dto.ProductAdminDetailDto;
import ebnatural.bizcurator.apiserver.dto.ProductAdminListDto;
import ebnatural.bizcurator.apiserver.dto.ProductDetailDto;
import ebnatural.bizcurator.apiserver.dto.ProductListDto;
import ebnatural.bizcurator.apiserver.dto.request.ProductRequest;
import ebnatural.bizcurator.apiserver.dto.response.CommonResponse;
import ebnatural.bizcurator.apiserver.service.ProductService;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<CommonResponse> createProduct(
            @RequestPart("productRequest") @Valid ProductRequest productRequest,
            @RequestPart(value = "mainImage") MultipartFile mainImage,
            @RequestPart(value = "detailImage") MultipartFile detailImage
    ) {
        productService.registerProduct(productRequest, mainImage, detailImage);
        return CommonResponse.ok(HttpStatus.CREATED.value(), "상품 생성이 완료되었습니다.");
    }
    @PutMapping("/admins/products/{productId}")
    public ResponseEntity<CommonResponse> updateProduct(
            @PathVariable Long productId,
            @RequestPart("productRequest") @Valid ProductRequest productRequest,
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestPart(value = "detailImage", required = false) MultipartFile detailImage
    ) {
        productService.updateProduct(productId, productRequest, mainImage, detailImage);
        return CommonResponse.ok(HttpStatus.OK.value(), "상품 수정이 완료되었습니다.");
    }
    @GetMapping("/products")
    public ResponseEntity<CommonResponse> getProducts(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "new") String sort) {
        List<ProductListDto> products = productService.getProducts(categoryId, sort);
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("products", products);
        return CommonResponse.ok(HttpStatus.OK.value(), "상품 조회가 완료되었습니다.", productMap);
    }
    @GetMapping("/admins/products")
    public ResponseEntity<CommonResponse> getAdminProducts(
            @RequestParam(required = false) String keyword) {
        List<ProductAdminListDto> products = productService.getAdminProducts(keyword);
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("products", products);
        return CommonResponse.ok(HttpStatus.OK.value(), "상품 조회가 완료되었습니다.", productMap);
    }
    @GetMapping("/admins/products/{productId}")
    public ResponseEntity<CommonResponse> getAdminProductDetail(@PathVariable Long productId) {
        ProductAdminDetailDto productDetail = productService.getAdminProductDetail(productId);
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("productDetail", productDetail);
        return CommonResponse.ok(HttpStatus.OK.value(), "상품 상세 정보 조회가 완료되었습니다.", productMap);
    }

    @GetMapping("/products/search")
    public ResponseEntity<CommonResponse> searchProducts(
            @RequestParam(required = false) String keyword,//우선 따로 예외처리 하지 않고-keyword를 입력하지않았을 때, 전체 결과를 반환한다. 필요하다면 이 부분 수정.
            @RequestParam(defaultValue = "new") String sort) {
        List<ProductListDto> products = productService.searchProducts(keyword, sort);
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("products", products);
        return CommonResponse.ok(HttpStatus.OK.value(), "상품 검색이 완료되었습니다.", productMap);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<CommonResponse> getProductDetail(@PathVariable Long productId) {
        ProductDetailDto productDetail = productService.getProductDetail(productId);
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("productDetail", productDetail);
        return CommonResponse.ok(HttpStatus.OK.value(), "상품 상세 정보 조회가 완료되었습니다.", productMap);
    }

    @GetMapping("/products/top-weekly")
    public ResponseEntity<CommonResponse> getTopWeeklyProducts() {
        List<ProductListDto> products = productService.getTop3ProductsWeekly();
        HashMap<String, Object> productsMap = new HashMap<>();
        productsMap.put("topWeeklyProducts", products);
        return CommonResponse.ok(HttpStatus.OK.value(), "주간 인기 상품 정보 조회가 완료되었습니다.", productsMap);
    }

    @GetMapping("/products/top-monthly")
    public ResponseEntity<CommonResponse> getTopMonthlyProducts() {
        List<ProductListDto> products = productService.getTop3ProductsMonthly();
        HashMap<String, Object> productsMap = new HashMap<>();
        productsMap.put("topMonthlyProducts", products);
        return CommonResponse.ok(HttpStatus.OK.value(), "월간 인기 상품 정보 조회가 완료되었습니다.", productsMap);
    }

}
