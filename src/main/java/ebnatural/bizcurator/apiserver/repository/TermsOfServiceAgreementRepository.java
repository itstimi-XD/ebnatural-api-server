package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.TermsOfServiceAgreement;
import ebnatural.bizcurator.apiserver.domain.TermsOfServiceAgreementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TermsOfServiceAgreementRepository extends JpaRepository<TermsOfServiceAgreement, TermsOfServiceAgreementId> {

    @Modifying
    @Query("DELETE FROM TermsOfServiceAgreement db where db.member.id = ?1")
    void deleteAllByMemberId(Long id);
}