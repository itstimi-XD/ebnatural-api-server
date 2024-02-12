package ebnatural.bizcurator.apiserver.dto;

import com.querydsl.core.Tuple;
import ebnatural.bizcurator.apiserver.domain.constant.DeliveryState;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 마이페이지 홈 화면 정보 dto
 */
@Getter
@NoArgsConstructor
public class MyPageHomeDto {
    private int payDoneCount;
    private int deliveringCount;
    private int deliverDoneCount;
    private int paymentConfirmedCount;

    public void tupleToDto(List<Tuple> deliveryStateCounts) {
        for (Tuple tuple : deliveryStateCounts) {
            DeliveryState deliveryState = tuple.get(0, DeliveryState.class);
            int count = tuple.get(1, Long.class).intValue();
            switch (deliveryState) {
                case PAID:
                    this.payDoneCount = count;
                    break;
                case DELIVERING:
                    this.deliveringCount = count;
                    break;
                case DELIVER_DONE:
                    this.deliverDoneCount = count;
                    break;
                case FINISH:
                    this.paymentConfirmedCount = count;
                    break;
            }
        }
    }
}
