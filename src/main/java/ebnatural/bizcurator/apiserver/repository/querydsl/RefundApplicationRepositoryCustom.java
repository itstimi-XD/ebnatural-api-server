package ebnatural.bizcurator.apiserver.repository.querydsl;

import ebnatural.bizcurator.apiserver.domain.RefundApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RefundApplicationRepositoryCustom {

    Page<RefundApplication> findByOrderDetailProduct_NameContainingOrderByCreatedAtDesc(String search, Pageable pageable);
}
