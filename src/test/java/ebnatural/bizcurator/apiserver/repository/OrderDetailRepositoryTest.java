package ebnatural.bizcurator.apiserver.repository;

import static org.junit.jupiter.api.Assertions.*;

import ebnatural.bizcurator.apiserver.domain.OrderDetail;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("주문 jpa 연결 테스트")
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Rollback(false)
class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    @DisplayName("주문 데이터 넣어보기")
    @Disabled
    void insert() {

        // given
        long previousCount = orderDetailRepository.count();
        OrderDetail orderDetail = OrderDetail.of(1L, 10, "leeyeonhee", "010-1111-1111",
                "경기도", "빨리 보내주세요", "16064", "국민", 20000, 2500);

        // when
        orderDetailRepository.save(orderDetail);

        //then
        Assertions.assertThat(orderDetailRepository.count()).isEqualTo(previousCount + 1);

    }

}