package ebnatural.bizcurator.apiserver.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ebnatural.bizcurator.apiserver.domain.CancelApplication;
import ebnatural.bizcurator.apiserver.domain.QCancelApplication;
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

public class CancelApplicationRepositoryImpl implements CancelApplicationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CancelApplicationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<CancelApplication> findByOrderDetailProduct_NameContainingOrderByCreatedAtDesc(String search, Pageable pageable) {
        QCancelApplication qCancelApplication = QCancelApplication.cancelApplication;
        QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
        QProduct qProduct = QProduct.product;
        QManufacturer qManufacturer = QManufacturer.manufacturer;
        QCategory qCategory = QCategory.category;

        BooleanBuilder predicateBuilder = new BooleanBuilder();

        if (search == null) {
            // No search keyword provided, retrieve all refund applications
            predicateBuilder.and(qCancelApplication.isNotNull());
        } else {
            // Search refund applications by product name containing the provided search keyword
            predicateBuilder.and(qCancelApplication.orderDetail.product.name.containsIgnoreCase(search));
        }

        Predicate predicate = predicateBuilder.getValue();

        long total = queryFactory.selectFrom(qCancelApplication)
                .where(predicate)
                .fetchCount();

        List<CancelApplication> cancelApplications = queryFactory.selectFrom(qCancelApplication)
                .where(predicate)
                .leftJoin(qCancelApplication.orderDetail, qOrderDetail).fetchJoin()
                .leftJoin(qOrderDetail.product, qProduct).fetchJoin()
                .leftJoin(qProduct.category, qCategory).fetchJoin()
                .leftJoin(qProduct.manufacturer, qManufacturer).fetchJoin()
                .orderBy(qCancelApplication.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(cancelApplications, pageable, total);
    }

}
