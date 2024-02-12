package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.OrderDetail;
import ebnatural.bizcurator.apiserver.domain.Product;
import ebnatural.bizcurator.apiserver.domain.constant.DeliveryState;
import ebnatural.bizcurator.apiserver.repository.querydsl.OrderDetailRepositoryCustom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>, OrderDetailRepositoryCustom {

    List<OrderDetail> findAllByMemberIdAndOrderTimeAfterAndDeliveryState(Long memberId, LocalDateTime orderTime, DeliveryState deliveryState);

    List<OrderDetail> findAllByMemberIdAndOrderTimeAfter(Long memberId, LocalDateTime orderTime);

    List<OrderDetail> findAllByMemberIdAndDeliveryState(Long memberId, DeliveryState deliveryState);

    List<OrderDetail> findAllByMemberId(Long memberId);

    List<OrderDetail> findAllByPaymentIdAndMemberId(Long paymentId, Long memberId);

    Optional<OrderDetail> findById(Long id);

    @Query("SELECT o.product FROM OrderDetail o WHERE o.id = :id")
    Product findProductById(@Param("id") Long id);
}
