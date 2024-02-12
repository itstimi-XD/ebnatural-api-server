package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.PurposeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurposeCategoryRepository extends JpaRepository<PurposeCategory, Long> {

    PurposeCategory findByName(String name);
}
