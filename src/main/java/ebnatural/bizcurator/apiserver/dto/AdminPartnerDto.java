package ebnatural.bizcurator.apiserver.dto;

import ebnatural.bizcurator.apiserver.domain.SellDocument;
import lombok.Getter;

@Getter
public class AdminPartnerDto {
    String businessName;
    String category;
    String businessNumber;
    String ceoName;
    String managerPhoneNumber;
    int establishYear;

    private AdminPartnerDto(String businessName, String category, String businessNumber, String ceoName, String managerPhoneNumber, int establishYear) {
        this.businessName = businessName;
        this.category = category;
        this.businessNumber = businessNumber;
        this.ceoName = ceoName;
        this.managerPhoneNumber = managerPhoneNumber;
        this.establishYear = establishYear;
    }

    public static AdminPartnerDto from(SellDocument entity) {
        return new AdminPartnerDto(
                entity.getBusinessName(),
                entity.getCategory().getName(),
                entity.getBusinessNumber(),
                entity.getCeoName(),
                entity.getManagerPhoneNumber(),
                entity.getEstablishYear()
        );
    }
}
