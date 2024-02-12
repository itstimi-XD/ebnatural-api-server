package ebnatural.bizcurator.apiserver.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ebnatural.bizcurator.apiserver.domain.MakeDocument;
import ebnatural.bizcurator.apiserver.domain.QMakeDocument;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class MakeDocumentRepositoryImpl implements MakeDocumentRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public MakeDocumentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MakeDocument> findByAllDocumentCategoryContainingOrderByCreatedAtDesc(
            String search, Pageable pageable) {
        QMakeDocument qMakeDocument = QMakeDocument.makeDocument;

        BooleanBuilder predicateBuilder = new BooleanBuilder();

        if (search == null) {
            // No search keyword provided, retrieve all sell documents
            predicateBuilder.and(qMakeDocument.isNotNull());
        } else {
            // Search sell documents by purposeCategory name containing the provided search keyword
            predicateBuilder.and(qMakeDocument.purposeCategory.name.containsIgnoreCase(search));
        }

        Predicate predicate = predicateBuilder.getValue();
        long total = queryFactory.selectFrom(qMakeDocument)
                .leftJoin(qMakeDocument.purposeCategory).fetchJoin() // Perform fetch join on category
                .where(predicate)
                .fetchCount();

        List<MakeDocument> makeDocumentList = queryFactory.selectFrom(qMakeDocument)
                .leftJoin(qMakeDocument.purposeCategory).fetchJoin() // Perform fetch join on category
                .where(predicate)
                .orderBy(qMakeDocument.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(makeDocumentList, pageable, total);
    }

    @Override
    public List<MakeDocument> findAllByAfterFilteredDate(Long memberId, LocalDateTime filteredDate) {
        QMakeDocument qMakeDocument = QMakeDocument.makeDocument;

        return queryFactory
                .select(qMakeDocument)
                .from(qMakeDocument)
                .leftJoin(qMakeDocument.purposeCategory).fetchJoin() // Perform fetch join on category
                .where(qMakeDocument.member.id.eq(memberId).and(qMakeDocument.createdAt.goe(filteredDate)))
                .fetch();
    }

}
