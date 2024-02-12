package ebnatural.bizcurator.apiserver.dto;

import ebnatural.bizcurator.apiserver.dto.PaymentHistoryDto.OrderHistoryDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

/**
 * 주문 상세 내역 조회 dto
 */
@Getter
public class PaymentDetailDto {
    private Long paymentId;
    private LocalDateTime orderTime;
    private String buyer;
    private String paymentMethod;
    private int totalCost;
    private int shippingFee;
    private String postalCode;
    private String address;
    private String requestContent;
    private List<OrderDetailDto> orderDetailList;

    @Getter
    public static class OrderDetailDto{

        private Long orderId;
        private String productName;
        private String productImage;
        private int cost;

        private int quantity;
        private String deliveryState;


        private OrderDetailDto(Long orderId, String productName, String productImage, int cost,
                int quantity, String deliveryState) {
            this.orderId = orderId;
            this.productName = productName;
            this.productImage = productImage;
            this.cost = cost;
            this.quantity = quantity;
            this.deliveryState = deliveryState;
        }

        public static OrderDetailDto of(Long orderId, String productName, String productImage, int cost,
                int quantity, String deliveryState) {
            return new OrderDetailDto(orderId, productName, productImage, cost, quantity, deliveryState);
        }

    }

    private PaymentDetailDto(Long paymentId, LocalDateTime orderTime, String buyer,
            String paymentMethod, int totalCost, int shippingFee, String postalCode, String address,
            String requestContent, List<OrderDetailDto> orderDetailList) {
        this.paymentId = paymentId;
        this.orderTime = orderTime;
        this.buyer = buyer;
        this.paymentMethod = paymentMethod;
        this.totalCost = totalCost;
        this.shippingFee = shippingFee;
        this.postalCode = postalCode;
        this.address = address;
        this.requestContent = requestContent;
        this.orderDetailList = orderDetailList;
    }

    public static PaymentDetailDto of(Long paymentId, LocalDateTime orderTime, String buyer,
            String paymentMethod, int totalCost, int shippingFee, String postalCode, String address,
            String requestContent, List<OrderDetailDto> orderDetailList) {
        return new PaymentDetailDto(paymentId, orderTime, buyer, paymentMethod, totalCost,
                shippingFee, postalCode, address, requestContent,
                orderDetailList);
    }
}
