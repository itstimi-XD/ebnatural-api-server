package ebnatural.bizcurator.apiserver.repository.querydsl;

import ebnatural.bizcurator.apiserver.domain.CancelApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CancelApplicationRepositoryCustom {

    Page<CancelApplication> findByOrderDetailProduct_NameContainingOrderByCreatedAtDesc(String search, Pageable pageable);
}
