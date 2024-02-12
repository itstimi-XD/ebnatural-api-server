package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.Manufacturer;
import ebnatural.bizcurator.apiserver.repository.querydsl.ManufacturerRepositoryCustom;
import java.util.Objects;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long>,
        ManufacturerRepositoryCustom {
}
