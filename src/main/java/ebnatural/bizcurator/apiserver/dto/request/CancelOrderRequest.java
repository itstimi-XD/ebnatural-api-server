package ebnatural.bizcurator.apiserver.dto.request;

import lombok.Getter;

@Getter
public class CancelOrderRequest {
    private Long orderId;
    private String opinion;

    private CancelOrderRequest(Long orderId, String opinion) {
        this.orderId = orderId;
        this.opinion = opinion;
    }

    public static CancelOrderRequest of(Long orderId, String opinion) {
        return new CancelOrderRequest(orderId, opinion);
    }

}
