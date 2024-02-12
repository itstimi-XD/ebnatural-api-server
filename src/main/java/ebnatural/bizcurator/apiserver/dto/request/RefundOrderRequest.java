package ebnatural.bizcurator.apiserver.dto.request;

import lombok.Getter;

@Getter
public class RefundOrderRequest {
    private Long orderId;
    private String opinionCategory;
    private String receiveWayType;
    private String receiveAddressType;
    private String address;
    private String postalCode;

    private RefundOrderRequest(Long orderId, String opinionCategory, String receiveWayType,
            String receiveAddressType, String address, String postalCode) {
        this.orderId = orderId;
        this.opinionCategory = opinionCategory;
        this.receiveWayType = receiveWayType;
        this.receiveAddressType = receiveAddressType;
        this.address = address;
        this.postalCode = postalCode;
    }

    public static RefundOrderRequest of(Long orderId, String opinionCategory, String receiveWayType,
            String receiveAddressType, String address, String postalCode) {
        return new RefundOrderRequest(orderId, opinionCategory, receiveWayType, receiveAddressType, address, postalCode);
    }

}
