package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ebnatural.bizcurator.apiserver.domain.SellDocument;
import ebnatural.bizcurator.apiserver.domain.constant.RequestStateType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SellDocumentDto {
    Long id;
    String businessName;
    String ceoName;
    String businessNumber;
    String managerPhoneNumber;
    Long category;
    String productDetail;
    int establishYear;
    String introduction;
    String imageDirectory;
    RequestStateType stateType = RequestStateType.WAIT;

    public SellDocumentDto(Long id, String businessName, String ceoName, String businessNumber, String managerPhoneNumber, Long category, String productDetail, int establishYear, String introduction, String imageDirectory, RequestStateType stateType) {
        this.id = id;
        this.businessName = businessName;
        this.ceoName = ceoName;
        this.businessNumber = businessNumber;
        this.managerPhoneNumber = managerPhoneNumber;
        this.category = category;
        this.productDetail = productDetail;
        this.establishYear = establishYear;
        this.introduction = introduction;
        this.imageDirectory = imageDirectory;
        this.stateType = stateType;
    }

    public static SellDocumentDto from(SellDocument entity) {
        return new SellDocumentDto(
                entity.getId(),
                entity.getBusinessName(),
                entity.getCeoName(),
                entity.getBusinessNumber(),
                entity.getManagerPhoneNumber(),
                entity.getCategory().getId(),
                entity.getProductDetail(),
                entity.getEstablishYear(),
                entity.getIntroduction(),
                entity.getImageDirectory(),
                entity.getStateType()
        );
    }
}
