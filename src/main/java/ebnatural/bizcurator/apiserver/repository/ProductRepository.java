package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.Product;
import ebnatural.bizcurator.apiserver.repository.querydsl.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Modifying
    @Query("update Product p set p.weeklyClicks = 0")
    void resetWeeklyClicks();

    @Modifying
    @Query("update Product p set p.monthlyClicks = 0")
    void resetMonthlyClicks();

    @Modifying
    @Query("update Product p set p.weeklyClicks = p.weeklyClicks + 1, p.monthlyClicks = p.monthlyClicks + 1 where p.id = :productId")
    void incrementClicks(@Param("productId") Long productId);

    @Query("SELECT p.manufacturer, p.category FROM Product p WHERE p.id = :id")
    Object[] findManufacturerAndCategoryById(@Param("id") Long id);
}
