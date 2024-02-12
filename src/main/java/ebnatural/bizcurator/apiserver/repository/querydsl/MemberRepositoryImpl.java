package ebnatural.bizcurator.apiserver.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ebnatural.bizcurator.apiserver.domain.Member;
import ebnatural.bizcurator.apiserver.domain.QMember;
import ebnatural.bizcurator.apiserver.domain.QRefundApplication;
import ebnatural.bizcurator.apiserver.domain.RefundApplication;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Member> findByMemberBusinessNameContainingOrderByCreatedAtDesc(
            String search, Pageable pageable) {
        QMember qMember = QMember.member;

        BooleanBuilder predicateBuilder = new BooleanBuilder();

        if (search == null) {
            // No search keyword provided, retrieve all refund applications
            predicateBuilder.and(qMember.isNotNull());
        } else {
            // Search refund applications by product name containing the provided search keyword
            predicateBuilder.and(qMember.businessName.containsIgnoreCase(search));
        }

        Predicate predicate = predicateBuilder.getValue();
        long total = queryFactory.selectFrom(qMember)
                .where(predicate)
                .fetchCount();

        List<Member> memberList = queryFactory.selectFrom(qMember)
                .where(predicate)
                .orderBy(qMember.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(memberList, pageable, total);
    }
}
