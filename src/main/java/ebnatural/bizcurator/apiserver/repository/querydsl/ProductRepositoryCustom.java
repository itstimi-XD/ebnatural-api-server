package ebnatural.bizcurator.apiserver.repository.querydsl;

import ebnatural.bizcurator.apiserver.dto.ProductAdminDetailDto;
import ebnatural.bizcurator.apiserver.dto.ProductAdminListDto;
import ebnatural.bizcurator.apiserver.dto.ProductDetailDto;
import ebnatural.bizcurator.apiserver.dto.ProductListDto;
import java.util.List;

public interface ProductRepositoryCustom {
    List<ProductListDto> findByCategoryId(Long categoryId, String sort);
    List<ProductListDto> searchByKeyword(String keyword, String sort);
    ProductDetailDto findDetailById(Long productId);
    List<ProductListDto> findTop3ByWeeklyClicks();
    List<ProductListDto> findTop3ByMonthlyClicks();
    List<ProductAdminListDto> findAdminProducts(String keyword);
    ProductAdminDetailDto findAdminProductDetail(Long productId);

}
