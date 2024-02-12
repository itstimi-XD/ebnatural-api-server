package ebnatural.bizcurator.apiserver.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

/**
 * 주문 내역 조회 dto
 */
@Getter
public class PaymentHistoryDto {
    private Long paymentId;

    private List<OrderHistoryDto> orderInfo;

    @Getter
    public static class OrderHistoryDto {
        private Long orderId;
        private String image;

        private int costPerOne;
        private String deliveryState;
        private LocalDateTime orderTime;
        private String name;
        private int quantity;
        private int cost;

        private OrderHistoryDto(Long orderId, String image, int costperOne, String deliveryState,
                LocalDateTime orderTime, String name, int quantity, int cost) {
            this.orderId = orderId;
            this.image = image;
            this.costPerOne = costperOne;
            this.deliveryState = deliveryState;
            this.orderTime = orderTime;
            this.name = name;
            this.quantity = quantity;
            this.cost = cost;
        }

        public static OrderHistoryDto of(Long orderId, String image, int costperOne, String deliveryState,
                LocalDateTime orderTime, String name, int quantity, int cost) {
            return new OrderHistoryDto(orderId, image, costperOne, deliveryState, orderTime, name, quantity,
                    cost);
        }
    }

    private PaymentHistoryDto(Long paymentId, List<OrderHistoryDto> orderInfo) {
        this.paymentId = paymentId;
        this.orderInfo = orderInfo;
    }

    public static PaymentHistoryDto of(Long paymentId, List<OrderHistoryDto> orderInfo) {
        return new PaymentHistoryDto(paymentId, orderInfo);
    }
}
