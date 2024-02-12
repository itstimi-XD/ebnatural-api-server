package ebnatural.bizcurator.apiserver.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ebnatural.bizcurator.apiserver.domain.QCategory;
import ebnatural.bizcurator.apiserver.domain.QManufacturer;
import ebnatural.bizcurator.apiserver.domain.QOrderDetail;
import ebnatural.bizcurator.apiserver.domain.QProduct;
import ebnatural.bizcurator.apiserver.domain.QRefundApplication;
import ebnatural.bizcurator.apiserver.domain.RefundApplication;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class RefundApplicationRepositoryImpl implements RefundApplicationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public RefundApplicationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<RefundApplication> findByOrderDetailProduct_NameContainingOrderByCreatedAtDesc(
            String search, Pageable pageable) {
        QRefundApplication qRefundApplication = QRefundApplication.refundApplication;
        QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
        QProduct qProduct = QProduct.product;
        QManufacturer qManufacturer = QManufacturer.manufacturer;
        QCategory qCategory = QCategory.category;

        BooleanBuilder predicateBuilder = new BooleanBuilder();

        if (search == null) {
            // No search keyword provided, retrieve all refund applications
            predicateBuilder.and(qRefundApplication.isNotNull());
        } else {
            // Search refund applications by product name containing the provided search keyword
            predicateBuilder.and(qRefundApplication.orderDetail.product.name.containsIgnoreCase(search));
        }

        Predicate predicate = predicateBuilder.getValue();
        long total = queryFactory.selectFrom(qRefundApplication)
                .where(predicate)
                .fetchCount();

        List<RefundApplication> refundApplications = queryFactory.selectFrom(qRefundApplication)
                .leftJoin(qRefundApplication.orderDetail, qOrderDetail).fetchJoin()
                .leftJoin(qOrderDetail.product, qProduct).fetchJoin()
                .leftJoin(qProduct.category, qCategory).fetchJoin()
                .leftJoin(qProduct.manufacturer, qManufacturer).fetchJoin()
                .where(predicate)
                .orderBy(qRefundApplication.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(refundApplications, pageable, total);
    }

}
