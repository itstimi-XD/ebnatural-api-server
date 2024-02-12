package ebnatural.bizcurator.apiserver.domain;

import ebnatural.bizcurator.apiserver.domain.constant.RequestStateType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class RequestDocumentEntity extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;
    @NotBlank
    String managerName;
    @NotBlank
    String managerCall;
    @NotBlank
    String productName;
    @NotBlank
    String productDetail;
    @NotNull
    int quantity;
    @NotNull
    Date desiredEstimateDate;
    @NotNull
    Date desiredDeliveryDate;
    @NotBlank
    String requestMessage;

    @Setter
    @NotBlank
    String imageDirectory;

    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    RequestStateType stateType = RequestStateType.WAIT;

}
