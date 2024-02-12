package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ebnatural.bizcurator.apiserver.domain.MakeDocument;
import ebnatural.bizcurator.apiserver.domain.PurchaseDocument;
import ebnatural.bizcurator.apiserver.domain.constant.DocumentType;
import ebnatural.bizcurator.apiserver.domain.constant.RequestStateType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PurchaseMakeDocumentDto {
    DocumentType documentType;
    Long id;
    String managerName;
    String managerCall;
    long category;
    String productName;
    String productDetail;
    int quantity;
    Date desiredEstimateDate;
    Date DesiredDeliveryDate;
    String requestMessage;
    String imageDirectory;
    RequestStateType stateType = RequestStateType.WAIT;

    public PurchaseMakeDocumentDto(Long id, DocumentType documentType, String managerName, String managerCall, long category, String productName, String productDetail, int quantity, Date desiredEstimateDate, Date desiredDeliveryDate, String requestMessage, String imageDirectory, RequestStateType stateType) {
        this.id = id;
        this.documentType = documentType;
        this.managerName = managerName;
        this.managerCall = managerCall;
        this.category = category;
        this.productName = productName;
        this.productDetail = productDetail;
        this.quantity = quantity;
        this.desiredEstimateDate = desiredEstimateDate;
        DesiredDeliveryDate = desiredDeliveryDate;
        this.requestMessage = requestMessage;
        this.imageDirectory = imageDirectory;
        this.stateType = stateType;
    }

    public static PurchaseMakeDocumentDto fromPurchase(PurchaseDocument purchaseDocument){
        return new PurchaseMakeDocumentDto(
                purchaseDocument.getId(),
                DocumentType.purchase,
                purchaseDocument.getManagerName(),
                purchaseDocument.getManagerCall(),
                purchaseDocument.getCategory().getId(),
                purchaseDocument.getProductName(),
                purchaseDocument.getProductDetail(),
                purchaseDocument.getQuantity(),
                purchaseDocument.getDesiredEstimateDate(),
                purchaseDocument.getDesiredDeliveryDate(),
                purchaseDocument.getRequestMessage(),
                purchaseDocument.getImageDirectory(),
                purchaseDocument.getStateType()
        );
    }
    public static PurchaseMakeDocumentDto fromMake(MakeDocument makeDocument){
        return new PurchaseMakeDocumentDto(
                makeDocument.getId(),
                DocumentType.make,
                makeDocument.getManagerName(),
                makeDocument.getManagerCall(),
                makeDocument.getPurposeCategory().getId(),
                makeDocument.getProductName(),
                makeDocument.getProductDetail(),
                makeDocument.getQuantity(),
                makeDocument.getDesiredEstimateDate(),
                makeDocument.getDesiredDeliveryDate(),
                makeDocument.getRequestMessage(),
                makeDocument.getImageDirectory(),
                makeDocument.getStateType()
        );
    }
}
