package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.OrderDetail;
import ebnatural.bizcurator.apiserver.domain.RefundApplication;
import ebnatural.bizcurator.apiserver.repository.querydsl.RefundApplicationRepositoryCustom;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefundApplicationRepository extends JpaRepository<RefundApplication, Long> ,
        RefundApplicationRepositoryCustom {
    boolean existsByOrderDetailId(Long orderId);
    List<RefundApplication> findAllByMemberId(Long memberId);
    List<RefundApplication> findAllByMemberIdAndCreatedAtAfter(Long memberId, LocalDateTime filterDate);

    @Query("SELECT r.orderDetail FROM RefundApplication r WHERE r.id = :id")
    OrderDetail findOrderDetailById(@Param("id") Long id);

}