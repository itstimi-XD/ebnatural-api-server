package ebnatural.bizcurator.apiserver.repository.querydsl;

import com.querydsl.core.Tuple;
import ebnatural.bizcurator.apiserver.domain.OrderDetail;
import java.util.List;

public interface OrderDetailRepositoryCustom {

    List<Tuple> countByDeliveryState(Long memberId);

    List<OrderDetail> findByAllOrderDetailProductNameContainingOrderByCreatedAtDesc(
            String search);
}
