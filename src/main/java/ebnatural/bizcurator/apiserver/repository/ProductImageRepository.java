package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.ProductImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findAllByProductId(Long productId);
}
