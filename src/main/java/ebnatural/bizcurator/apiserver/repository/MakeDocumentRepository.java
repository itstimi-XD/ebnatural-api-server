package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.MakeDocument;
import ebnatural.bizcurator.apiserver.domain.SellDocument;
import ebnatural.bizcurator.apiserver.repository.querydsl.MakeDocumentRepositoryCustom;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface MakeDocumentRepository extends JpaRepository<MakeDocument, Long>, MakeDocumentRepositoryCustom {
    @Query("SELECT d FROM MakeDocument d where d.member.id = ?1 ORDER BY d.id ASC")
    List<MakeDocument> findByMemberId(Long userId);
    Optional<MakeDocument> findByMemberIdAndId(Long memberId, Long id);
}
