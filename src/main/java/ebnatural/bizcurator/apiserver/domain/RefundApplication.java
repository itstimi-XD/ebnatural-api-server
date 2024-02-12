package ebnatural.bizcurator.apiserver.domain;

import ebnatural.bizcurator.apiserver.domain.constant.ApplicationState;
import ebnatural.bizcurator.apiserver.domain.constant.OrderRefundType;
import ebnatural.bizcurator.apiserver.domain.constant.ReceiveAddressType;
import ebnatural.bizcurator.apiserver.domain.constant.ReceiveWayType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 환불신청내역 클래스
 */

@Getter
@Table(indexes = {
        @Index(columnList = "order_id"),
        @Index(columnList = "user_id"),
        @Index(columnList = "createdAt")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class RefundApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 고유번호

    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member member;    // 주문자

    @JoinColumn(name = "order_id")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private OrderDetail orderDetail;    // 주문정보

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private OrderRefundType opinionCategory = OrderRefundType.UNSELECTED; // 환불 사유 카테고리

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ReceiveWayType receiveWayType = ReceiveWayType.PICK_UP_BY_COMPANY;  // 제품 발송 방법
    @Enumerated(EnumType.STRING)
    private ReceiveAddressType receiveAddressType = ReceiveAddressType.UNSELECTED; // 수거지 선택

    @Setter
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ApplicationState state = ApplicationState.WAIT; // 상태값
    private String address; // 수거지 주소

    @Column(length = 20)
    private String postalCode; // 우편번호

    private LocalDateTime approveTime; // 처리 완료(FINISHED)된 시간

    @Column(nullable = false, updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    private LocalDateTime createdAt;    // 신청 시간
    public RefundApplication() {

    }
    private RefundApplication(Member member, OrderDetail orderDetail,
            OrderRefundType opinionCategory, ReceiveWayType receiveWayType, ReceiveAddressType receiveAddressType,
            String address, String postalCode) {
        this.member = member;
        this.orderDetail = orderDetail;
        this.opinionCategory = opinionCategory;
        this.receiveWayType = receiveWayType;
        this.receiveAddressType = receiveAddressType;
        this.address = address;
        this.postalCode = postalCode;
    }

    public static RefundApplication of(Member member, OrderDetail orderDetail, OrderRefundType opinionCategory,
            ReceiveWayType receiveWayType, ReceiveAddressType receiveAddressType, String address, String postalCode) {
        return new RefundApplication(member, orderDetail, opinionCategory, receiveWayType, receiveAddressType, address, postalCode);
    }

    public static RefundApplication of(Member member, OrderDetail orderDetail, OrderRefundType opinionCategory, ReceiveWayType receiveWayType) {
        return new RefundApplication(member, orderDetail, opinionCategory, receiveWayType, ReceiveAddressType.UNSELECTED, "", "");
    }
}
