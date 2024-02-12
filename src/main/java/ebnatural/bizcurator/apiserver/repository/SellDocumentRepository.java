package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.SellDocument;
import ebnatural.bizcurator.apiserver.repository.querydsl.SellDocumentRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SellDocumentRepository extends JpaRepository<SellDocument, Long>, SellDocumentRepositoryCustom {
    @Query("SELECT d FROM SellDocument d where d.member.id = ?1  ORDER BY d.id ASC")
    List<SellDocument> findByMemberId(Long memberId);

    @Query("SELECT d FROM SellDocument d JOIN FETCH d.category WHERE d.member.id = :memberId AND d.id = :id")
    Optional<SellDocument> findByMemberIdAndId(@Param("memberId") Long memberId, @Param("id") Long id);

}
