package ebnatural.bizcurator.apiserver.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class TermsOfService extends TimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String content;
    private Boolean needAgreement;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_of_service_agreement_id")
    List<TermsOfServiceAgreement> termsOfServiceAgreements;
}
