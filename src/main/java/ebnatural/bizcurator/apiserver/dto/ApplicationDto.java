package ebnatural.bizcurator.apiserver.dto;

import lombok.Getter;

/**
 * 주문 취소, 환불 신청 내역
 */
@Getter
public class ApplicationDto {


    private Long id;
    private Long paymentId;
    private String image;
    private Long orderId;
    private String orderTime;
    private String name;
    private int quantity;
    private int cost;
    private String state;

    private ApplicationDto(Long id, Long paymentId, String image, Long orderId, String orderTime,
            String name, int quantity, int cost, String state) {
        this.id = id;
        this.paymentId = paymentId;
        this.image = image;
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.state = state;
    }

    public static ApplicationDto of(Long id, Long paymentId, String image, Long orderId, String orderTime,
            String name, int quantity, int cost, String state) {
        return new ApplicationDto(id, paymentId, image, orderId, orderTime, name, quantity, cost,
                state);
    }

}
