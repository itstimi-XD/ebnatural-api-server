package ebnatural.bizcurator.apiserver.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@IdClass(TermsOfServiceAgreementId.class)
public class TermsOfServiceAgreement extends TimeEntity{
    @Id
    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @Id
    @ManyToOne(targetEntity = TermsOfService.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_of_service_id", insertable = false, updatable = false)
    private TermsOfService termsOfService;

    Boolean Agreement;

    private TermsOfServiceAgreement(Member member, TermsOfService termsOfService, Boolean agreement) {
        this.member = member;
        this.termsOfService = termsOfService;
        Agreement = agreement;
    }

    public TermsOfServiceAgreement() {

    }

    public static TermsOfServiceAgreement of(Member member, TermsOfService termsOfService, Boolean agreement){
        return new TermsOfServiceAgreement(member, termsOfService, agreement);
    }
}
