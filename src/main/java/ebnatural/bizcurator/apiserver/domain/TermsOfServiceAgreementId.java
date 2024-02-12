package ebnatural.bizcurator.apiserver.domain;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
public class TermsOfServiceAgreementId implements Serializable {
    @EqualsAndHashCode.Include
    private Long member;
    @EqualsAndHashCode.Include
    private Long termsOfService;

    public TermsOfServiceAgreementId(){};
}
