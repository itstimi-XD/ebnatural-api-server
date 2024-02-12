package ebnatural.bizcurator.apiserver.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ebnatural.bizcurator.apiserver.domain.constant.RequestStateType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SellDocumentRequest {
    @NotBlank
    String businessName;
    @NotBlank
    String ceoName;
    @NotBlank
    String businessNumber;
    @NotBlank
    String managerPhoneNumber;
    @NotNull
    Long category;
    @NotBlank
    String productDetail;
    @NotNull
    Integer establishYear;
    @NotBlank
    String introduction;
    String imageDirectory;
    RequestStateType stateType = RequestStateType.WAIT;
}
