package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ebnatural.bizcurator.apiserver.domain.MakeDocument;
import ebnatural.bizcurator.apiserver.domain.PurchaseDocument;
import ebnatural.bizcurator.apiserver.domain.SellDocument;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MyPageDocumentDto {

    private Long requestId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private String requestType;
    private String state;
    private String category;

    private MyPageDocumentDto(Long requestId, LocalDateTime createdAt, String requestType, String state,
            String category) {
        this.requestId = requestId;
        this.createdAt = createdAt;
        this.requestType = requestType;
        this.state = state;
        this.category = category;
    }

    public static MyPageDocumentDto fromEntity(SellDocument document) {
        return new MyPageDocumentDto(
                document.getId(),
                document.getCreatedAt(),
                "제품입점 의뢰",
                document.getStateType().getStatus(),
                document.getCategory().getName());
    }

    public static MyPageDocumentDto fromEntity(MakeDocument document) {
        return new MyPageDocumentDto(
                document.getId(),
                document.getCreatedAt(),
                "제품제작 의뢰",
                document.getStateType().getStatus(),
                document.getPurposeCategory().getName());
    }

    public static MyPageDocumentDto fromEntity(PurchaseDocument document) {
        return new MyPageDocumentDto(
                document.getId(),
                document.getCreatedAt(),
                "제품구매 의뢰",
                document.getStateType().getStatus(),
                document.getCategory().getName());
    }
}
