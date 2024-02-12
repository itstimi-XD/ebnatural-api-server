package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;

/**
 * 관리자페이지
 * 주문 취소, 환불 신청 내역 dto
 */
@Getter
public class AdminApplicationDto {
    private Long applicationId;
    private String productName;
    private String manufacturerName;
    private String productCategory;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime orderTime;
    private String state;
    private int quantity;
    private int cost;
    private String opinionCategory;

    private AdminApplicationDto(Long applicationId, String productName, String manufacturerName,
            String productCategory, LocalDateTime orderTime, String state, int quantity, int cost, String opinionCategory) {
        this.applicationId = applicationId;
        this.productName = productName;
        this.manufacturerName = manufacturerName;
        this.productCategory = productCategory;
        this.orderTime = orderTime;
        this.state = state;
        this.quantity = quantity;
        this.cost = cost;
        this.opinionCategory = opinionCategory;
    }

    public static AdminApplicationDto of(Long applicationId, String productName, String manufacturerName,
            String productCategory, LocalDateTime orderTime, String deliveryState, int quantity, int cost, String opinionCategory) {
        return new AdminApplicationDto(applicationId, productName, manufacturerName, productCategory, orderTime, deliveryState, quantity, cost, opinionCategory);
    }
}
