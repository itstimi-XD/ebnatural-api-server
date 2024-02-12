package ebnatural.bizcurator.apiserver.domain;

import ebnatural.bizcurator.apiserver.domain.constant.RequestStateType;
import ebnatural.bizcurator.apiserver.dto.DocumentChangeDto;
import ebnatural.bizcurator.apiserver.dto.request.PurchaseMakeDocumentRequest;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(indexes = {
        @Index(columnList = "category_id"),
        @Index(columnList = "member_id"),
        @Index(columnList = "createdAt")
})
@Entity
@Getter
public class PurchaseDocument extends RequestDocumentEntity{
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public PurchaseDocument(){}
    private PurchaseDocument(Member member, String managerName, String managerCall, Category category, String productName, String productDetail,
                             int quantity, String requestMessage, Date desiredEstimateDate, Date desiredDeliveryDate, String storedPath, RequestStateType stateType){
        this.member = member;
        this.managerName = managerName;
        this.managerCall = managerCall;
        this.category = category;
        this.productName = productName;
        this.productDetail = productDetail;
        this. quantity = quantity;
        this.requestMessage = requestMessage;
        this.desiredEstimateDate = desiredEstimateDate;
        this.desiredDeliveryDate = desiredDeliveryDate;
        this.imageDirectory = storedPath;
        this.stateType = stateType;
    }
    public static PurchaseDocument of(Member member, PurchaseMakeDocumentRequest docDto, Category category, String storedPath){
        return new PurchaseDocument(member, docDto.getManagerName(), docDto.getManagerCall(), category, docDto.getProductName(), docDto.getProductDetail(),
                docDto.getQuantity(), docDto.getRequestMessage(), docDto.getDesiredEstimateDate(), docDto.getDesiredDeliveryDate(), storedPath, docDto.getStateType());
    }

    public PurchaseDocument update(Member member, Category category, PurchaseMakeDocumentRequest docDto){
        this.member = member;
        this.managerName = docDto.getManagerName();
        this.managerCall = docDto.getManagerCall();
        this.category = category;
        this.productName = docDto.getProductName();
        this.productDetail = docDto.getProductDetail();
        this. quantity = docDto.getQuantity();
        this.requestMessage = docDto.getRequestMessage();
        this.desiredEstimateDate = docDto.getDesiredEstimateDate();
        this.desiredDeliveryDate = docDto.getDesiredDeliveryDate();
        this.imageDirectory = docDto.getImageDirectory();
        this.stateType = docDto.getStateType();
        return this;
    }

    public void update(Category category, DocumentChangeDto documentChangeDto) {
        this.productName = documentChangeDto.getProductName();
        this.category = category;
        this.productDetail = documentChangeDto.getProductDetail();
        this.quantity = documentChangeDto.getQuantity();
        this.desiredEstimateDate = documentChangeDto.getDesiredEstimateDate();
        this.desiredDeliveryDate = documentChangeDto.getDesiredDeliveryDate();
        this.requestMessage = documentChangeDto.getRequestContext();
    }
}
