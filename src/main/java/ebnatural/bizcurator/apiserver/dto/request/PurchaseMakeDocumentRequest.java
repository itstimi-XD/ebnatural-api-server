package ebnatural.bizcurator.apiserver.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ebnatural.bizcurator.apiserver.common.validator.Enum;
import ebnatural.bizcurator.apiserver.domain.constant.DocumentType;
import ebnatural.bizcurator.apiserver.domain.constant.RequestStateType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PurchaseMakeDocumentRequest {
    @Enum(enumClass = DocumentType.class, ignoreCase = true)
    String documentType;
    @NotBlank
    String managerName;
    @NotBlank
    String managerCall;
    @NotNull
    Long category;
    @NotBlank
    String productName;
    @NotBlank
    String productDetail;
    @NotNull
    Integer quantity;
    @NotNull
    Date desiredEstimateDate;
    @NotNull
    Date DesiredDeliveryDate;
    @NotBlank
    String requestMessage;
    String imageDirectory;
    RequestStateType stateType = RequestStateType.WAIT;
}
