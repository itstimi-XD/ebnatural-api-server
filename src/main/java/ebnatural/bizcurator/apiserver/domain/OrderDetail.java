package ebnatural.bizcurator.apiserver.domain;

import ebnatural.bizcurator.apiserver.domain.constant.DeliveryState;
import ebnatural.bizcurator.apiserver.domain.constant.OrderType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 주문 내역 클래스
 */

@Table(indexes = {
        @Index(columnList = "paymentId"),
        @Index(columnList = "orderTime"),
        @Index(columnList = "buyer_id"),
        @Index(columnList = "product_id")
})
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 고유번호

    private Long paymentId; // 결제 고유 번호

    private int quantity;   // 수량

    @JoinColumn(name = "buyer_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member member;    // 주문자

    @JoinColumn(name = "product_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Product product;    // 물건

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderTime;    // 주문시간

    @Column(length = 20)
    private String receiverName;   // 수령인

    @Column(length = 20)
    private String buyerPhoneNumber; // 주문자 휴대폰번호

    @Column(length = 100)
    private String address; // 수령주소

    @Column(length = 500)
    private String requestContent; // 요청사항

    @Column(length = 20)
    private String postalCode; // 우편번호

    @Column(length = 10)
    private String paymentMethod; // 결제수단

    private int cost; // 결제금액 (물건 정가 + 할인율 + 배송비)

    @Enumerated(EnumType.STRING)
    private DeliveryState deliveryState = DeliveryState.PAID; // 배송상태

    @Enumerated(EnumType.STRING)
    private OrderType orderType = OrderType.IMMEDIATE; // 주문 유형

    private int shippingFee; // 배송비

    @Column(length = 20)
    private String invoiceNumber; // 송장번호
    protected OrderDetail() {
    }

    private OrderDetail(Long paymentId, int quantity, String receiverName, String buyerPhoneNumber, String address,
            String requestContent, String postalCode, String paymentMethod, int cost, int shippingFee) {
        this.paymentId = paymentId;
        this.quantity = quantity;
        this.receiverName = receiverName;
        this.buyerPhoneNumber = buyerPhoneNumber;
        this.address = address;
        this.requestContent = requestContent;
        this.postalCode = postalCode;
        this.paymentMethod = paymentMethod;
        this.cost = cost;
        this.shippingFee = shippingFee;
    }

    public static OrderDetail of(Long paymentId, int quantity, String receiverName, String buyerPhoneNumber, String address,
            String requestContent, String postalCode, String paymentMethod, int cost, int shippingFee) {
        return new OrderDetail(paymentId, quantity, receiverName, buyerPhoneNumber, address, requestContent, postalCode,
                paymentMethod, cost, shippingFee);
    }

}
