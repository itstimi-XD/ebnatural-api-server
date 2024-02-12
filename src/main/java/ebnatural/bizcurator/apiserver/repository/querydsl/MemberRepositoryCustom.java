package ebnatural.bizcurator.apiserver.repository.querydsl;

import ebnatural.bizcurator.apiserver.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    Page<Member> findByMemberBusinessNameContainingOrderByCreatedAtDesc(
            String search, Pageable pageable);
}
