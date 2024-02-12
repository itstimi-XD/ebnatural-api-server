package ebnatural.bizcurator.apiserver.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ebnatural.bizcurator.apiserver.domain.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Min;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CartProductRequest {

    private Long productId;

    @Min(value = 1, message = "최소 1개 이상 담아주세요") // 장바구니에 담을 갯수
    private Integer quantity;

    /*public Cart toEntity(CartProductRequest request) {
        return
    }*/

}
