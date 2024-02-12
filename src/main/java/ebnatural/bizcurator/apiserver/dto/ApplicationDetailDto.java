package ebnatural.bizcurator.apiserver.dto;

import lombok.Getter;

/**
 * 주문 취소, 환불 신청 상세내역
 */
@Getter
public class ApplicationDetailDto {

    private String name;
    private String image;

    private int costPerOne;
    private int quantity;

    private String paymentMethod;

    private int shippingFee;

    private String approveTime;
    private int cost;

    private String buyerName;

    private String postalCode;

    private String address;

    private String reason;


    private ApplicationDetailDto(String name, String image, int costPerOne, int quantity,
            String paymentMethod, int shippingFee, String approveTime, int cost, String buyerName,
            String postalCode, String address, String reason) {
        this.name = name;
        this.image = image;
        this.costPerOne = costPerOne;
        this.quantity = quantity;
        this.paymentMethod = paymentMethod;
        this.shippingFee = shippingFee;
        this.approveTime = approveTime;
        this.cost = cost;
        this.buyerName = buyerName;
        this.postalCode = postalCode;
        this.address = address;
        this.reason = reason;
    }

    public static ApplicationDetailDto of(String name, String image, int costPerOne, int quantity,
            String paymentMethod, int shippingFee, String approveTime, int cost, String buyerName,
            String postalCode, String address, String reason) {
        return new ApplicationDetailDto(name, image, costPerOne, quantity, paymentMethod, shippingFee,
                approveTime, cost, buyerName, postalCode, address, reason);
    }

}
