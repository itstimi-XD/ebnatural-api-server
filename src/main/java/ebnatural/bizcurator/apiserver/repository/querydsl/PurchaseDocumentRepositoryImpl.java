package ebnatural.bizcurator.apiserver.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ebnatural.bizcurator.apiserver.domain.PurchaseDocument;
import ebnatural.bizcurator.apiserver.domain.QPurchaseDocument;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PurchaseDocumentRepositoryImpl implements PurchaseDocumentRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public PurchaseDocumentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PurchaseDocument> findByAllDocumentCategoryContainingOrderByCreatedAtDesc(
            String search, Pageable pageable) {
        QPurchaseDocument qPurchaseDocument = QPurchaseDocument.purchaseDocument;

        BooleanBuilder predicateBuilder = new BooleanBuilder();

        if (search == null) {
            // No search keyword provided, retrieve all sell documents
            predicateBuilder.and(qPurchaseDocument.isNotNull());
        } else {
            // Search sell documents by category name containing the provided search keyword
            predicateBuilder.and(qPurchaseDocument.category.name.containsIgnoreCase(search));
        }

        Predicate predicate = predicateBuilder.getValue();
        long total = queryFactory.selectFrom(qPurchaseDocument)
                .leftJoin(qPurchaseDocument.category).fetchJoin() // Perform fetch join on category
                .where(predicate)
                .fetchCount();

        List<PurchaseDocument> purchaseDocumentList = queryFactory.selectFrom(qPurchaseDocument)
                .leftJoin(qPurchaseDocument.category).fetchJoin() // Perform fetch join on category
                .where(predicate)
                .orderBy(qPurchaseDocument.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(purchaseDocumentList, pageable, total);
    }

    @Override
    public List<PurchaseDocument> findAllByAfterFilteredDate(Long memberId, LocalDateTime filteredDate) {
        QPurchaseDocument qPurchaseDocument = QPurchaseDocument.purchaseDocument;

        return queryFactory
                .select(qPurchaseDocument)
                .from(qPurchaseDocument)
                .leftJoin(qPurchaseDocument.category).fetchJoin() // Perform fetch join on category
                .where(qPurchaseDocument.member.id.eq(memberId).and(qPurchaseDocument.createdAt.goe(filteredDate)))
                .fetch();
    }

}
