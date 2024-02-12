package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ebnatural.bizcurator.apiserver.domain.MakeDocument;
import ebnatural.bizcurator.apiserver.domain.PurchaseDocument;
import ebnatural.bizcurator.apiserver.domain.SellDocument;
import lombok.Getter;

import java.util.Date;

@Getter
public class MyPageDocumentDetailDto {

    private Long requestId;

    private Long categoryId;
    private String category;

    private String productName;
    private String productDetail;
    private Integer quantity;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date desiredEstimateDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date desiredDeliveryDate;

    private String image;

    private Integer establishYear;

    private String companyIntroduction;

    private String requestContext;


    private MyPageDocumentDetailDto(Long requestId, Long categoryId, String category, String productName, String productDetail,
                                    Integer quantity,
                                    Date desiredEstimateDate, Date desiredDeliveryDate,
                                    String image,
                                    Integer establishYear, String companyIntroduction, String requestContext) {
        this.requestId = requestId;
        this.categoryId = categoryId;
        this.category = category;
        this.productName = productName;
        this.productDetail = productDetail;
        this.quantity = quantity;
        this.desiredEstimateDate = desiredEstimateDate;
        this.desiredDeliveryDate = desiredDeliveryDate;
        this.image = image;
        this.establishYear = establishYear;
        this.companyIntroduction = companyIntroduction;
        this.requestContext = requestContext;
    }


    public static MyPageDocumentDetailDto fromEntity(SellDocument document) {
        return new MyPageDocumentDetailDto(
                document.getId(),
                document.getCategory().getId(),
                document.getCategory().getName(),
                null,
                document.getProductDetail(),
                null,  //입점의뢰는 갯수를 입력하지 않음
                null, //입점의뢰는 desiredEstimateDate 입력하지 않음
                null, //입점의뢰는 desiredDeliveryDate 입력하지 않음
                document.getImageDirectory(),
                document.getEstablishYear(),
                document.getIntroduction(),
                null); //입점의뢰는 요청사항 입력하지 않음
    }

    public static MyPageDocumentDetailDto fromEntity(MakeDocument document) {
        return new MyPageDocumentDetailDto(
                document.getId(),
                document.getPurposeCategory().getId(),
                document.getPurposeCategory().getName(),
                document.getProductName(),
                document.getProductDetail(),
                document.getQuantity(),
                document.getDesiredEstimateDate(),
                document.getDesiredDeliveryDate(),
                document.getImageDirectory(),
                null, // 제작의뢰는 설립년도 필요없음
                null, // 제작의뢰는 회사 소개 필요없음
                document.getRequestMessage()
        );
    }

    public static MyPageDocumentDetailDto fromEntity(PurchaseDocument document) {
        return new MyPageDocumentDetailDto(
                document.getId(),
                document.getCategory().getId(),
                document.getCategory().getName(),
                document.getProductName(),
                document.getProductDetail(),
                document.getQuantity(),
                document.getDesiredEstimateDate(),
                document.getDesiredDeliveryDate(),
                document.getImageDirectory(),
                null, // 구매의뢰는 설립년도 필요없음
                null, // 구매의뢰는 회사 소개 필요없음
                document.getRequestMessage()
        );
    }
}
