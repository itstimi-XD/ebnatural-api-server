package ebnatural.bizcurator.apiserver.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ebnatural.bizcurator.apiserver.domain.OrderDetail;
import ebnatural.bizcurator.apiserver.domain.QCategory;
import ebnatural.bizcurator.apiserver.domain.QManufacturer;
import ebnatural.bizcurator.apiserver.domain.QOrderDetail;
import ebnatural.bizcurator.apiserver.domain.QProduct;
import java.util.List;
import javax.persistence.EntityManager;

public class OrderDetailRepositoryImpl implements OrderDetailRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public OrderDetailRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Tuple> countByDeliveryState(Long memberId) {
        QOrderDetail orderDetail = QOrderDetail.orderDetail;
        return queryFactory
                .select(orderDetail.deliveryState, orderDetail.count())
                .from(orderDetail)
                .where(orderDetail.member.id.eq(memberId))
                .groupBy(orderDetail.deliveryState)
                .orderBy(orderDetail.deliveryState.asc())  // deliveryState 값을 오름차순으로 정렬
                .fetch();
    }

    @Override
    public List<OrderDetail> findByAllOrderDetailProductNameContainingOrderByCreatedAtDesc(String search) {
        QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
        QProduct qProduct = QProduct.product;
        QManufacturer qManufacturer = QManufacturer.manufacturer;
        QCategory qCategory = QCategory.category;
        BooleanBuilder predicateBuilder = new BooleanBuilder();

        if (search == null) {
            // No search keyword provided, retrieve all sell documents
            predicateBuilder.and(qOrderDetail.isNotNull());
        } else {
            // Search sell documents by product name containing the provided search keyword
            predicateBuilder.and(qOrderDetail.product.name.containsIgnoreCase(search));
        }

        Predicate predicate = predicateBuilder.getValue();

        return queryFactory.selectFrom(qOrderDetail)
                .leftJoin(qOrderDetail.product, qProduct).fetchJoin()
                .leftJoin(qProduct.manufacturer, qManufacturer).fetchJoin()
                .leftJoin(qProduct.category, qCategory).fetchJoin()
                .where(predicate)
                .orderBy(qOrderDetail.orderTime.desc())
                .fetch();
    }
}
