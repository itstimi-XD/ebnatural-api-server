package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.CancelApplication;
import ebnatural.bizcurator.apiserver.domain.OrderDetail;
import ebnatural.bizcurator.apiserver.repository.querydsl.CancelApplicationRepositoryCustom;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CancelApplicationRepository extends JpaRepository<CancelApplication, Long>, CancelApplicationRepositoryCustom {

    boolean existsByOrderDetailId(Long orderId);

    List<CancelApplication> findAllByMemberId(Long memberId);

    List<CancelApplication> findAllByMemberIdAndCreatedAtAfter(Long memberId, LocalDateTime filterDate);

    @Query("SELECT c.orderDetail FROM CancelApplication c WHERE c.id = :id")
    OrderDetail findOrderDetailById(@Param("id") Long id);

}