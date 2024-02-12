package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ebnatural.bizcurator.apiserver.domain.MakeDocument;
import ebnatural.bizcurator.apiserver.domain.PurchaseDocument;
import ebnatural.bizcurator.apiserver.domain.RequestDocumentEntity;
import ebnatural.bizcurator.apiserver.domain.SellDocument;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;

@Getter
public class AdminPurchaseAndMakeDocumentDto {

    private Long id;
    private String category;

    private String productDetail;

    private int quantity;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date desiredEstimateDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date desiredDeliveryDate;

    private String directPhoneNumber;

    private String state;

    private AdminPurchaseAndMakeDocumentDto(Long id, String category, String productDetail,
            int quantity, Date desiredEstimateDate, Date desiredDeliveryDate, String directPhoneNumber, String state) {
        this.id = id;
        this.category = category;
        this.productDetail = productDetail;
        this.quantity = quantity;
        this.desiredEstimateDate = desiredEstimateDate;
        this.desiredDeliveryDate = desiredDeliveryDate;
        this.directPhoneNumber = directPhoneNumber;
        this.state = state;
    }

    public static AdminPurchaseAndMakeDocumentDto from(MakeDocument entity) {
        return new AdminPurchaseAndMakeDocumentDto(
                entity.getId(),
                entity.getPurposeCategory().getName(),
                entity.getProductDetail(),
                entity.getQuantity(),
                entity.getDesiredEstimateDate(),
                entity.getDesiredDeliveryDate(),
                entity.getManagerCall(),
                entity.getStateType().getStatus()
        );
    }

    public static AdminPurchaseAndMakeDocumentDto from(PurchaseDocument entity) {
        return new AdminPurchaseAndMakeDocumentDto(
                entity.getId(),
                entity.getCategory().getName(),
                entity.getProductDetail(),
                entity.getQuantity(),
                entity.getDesiredEstimateDate(),
                entity.getDesiredDeliveryDate(),
                entity.getManagerCall(),
                entity.getStateType().getStatus()
        );
    }
}
