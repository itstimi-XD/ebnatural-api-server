package ebnatural.bizcurator.apiserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ebnatural.bizcurator.apiserver.domain.OrderDetail;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AdminOrderDetailDto {

    private Long orderId;
    private String productName;
    private String manufacturerName;
    private String productCategory;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime deliveryTime;
    private String deliveryState;
    private int quantity;
    private int cost;
    private String invoiceNumber;

    private AdminOrderDetailDto(Long orderId, String productName, String manufacturerName,
            String productCategory, LocalDateTime deliveryTime, String deliveryState, int quantity,
            int cost,
            String invoiceNumber) {
        this.orderId = orderId;
        this.productName = productName;
        this.manufacturerName = manufacturerName;
        this.productCategory = productCategory;
        this.deliveryTime = deliveryTime;
        this.deliveryState = deliveryState;
        this.quantity = quantity;
        this.cost = cost;
        this.invoiceNumber = invoiceNumber;
    }

    public static AdminOrderDetailDto of(Long orderId, String productName, String manufacturerName,
            String productCategory, LocalDateTime deliveryTime, String deliveryState, int quantity, int cost, String invoiceNumber) {

        return new AdminOrderDetailDto(orderId, productName, manufacturerName, productCategory, deliveryTime, deliveryState, quantity, cost, invoiceNumber);
    }

    public static AdminOrderDetailDto fromEntity(OrderDetail orderDetail) {
        return new AdminOrderDetailDto(
                orderDetail.getId(),
                orderDetail.getProduct().getName(),
                orderDetail.getProduct().getManufacturer().getName(),
                orderDetail.getProduct().getCategory().getName(),
                orderDetail.getOrderTime(),
                orderDetail.getDeliveryState().getMeaning(),
                orderDetail.getQuantity(),
                orderDetail.getCost(),
                orderDetail.getInvoiceNumber()
        );
    }
}
