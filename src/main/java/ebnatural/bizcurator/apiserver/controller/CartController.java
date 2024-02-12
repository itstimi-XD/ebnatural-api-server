package ebnatural.bizcurator.apiserver.controller;

import ebnatural.bizcurator.apiserver.dto.CartProductDto;
import ebnatural.bizcurator.apiserver.dto.request.CartProductRequest;
import ebnatural.bizcurator.apiserver.dto.response.CommonResponse;
import ebnatural.bizcurator.apiserver.service.CartService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CartController {

  private final CartService cartService;


    @GetMapping("/api/carts")//장바구니 조회
    public ResponseEntity<CommonResponse> getCartsList() {
        List<CartProductDto> cartLists = cartService.getCartsList();
        HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("cartsLists", cartLists);
        return CommonResponse.ok(HttpStatus.OK.value(), "장바구니 조회가 완료되었습니다. ", cartMap);
    }

    //장바구니 담기
    @PostMapping("/api/carts")
    public ResponseEntity<CommonResponse> containingProduct(@Valid @RequestBody CartProductRequest productRequest) {
        cartService.containingCartProducts(productRequest);
        return CommonResponse.ok(HttpStatus.OK.value(), "장바구니담기 성공");
    }

    //장바구니 상품 수량 수정
    @PatchMapping("/api/carts")
    public ResponseEntity<CommonResponse> updateProductQuantity(@Valid @RequestBody CartProductRequest productRequest) {

        cartService.updateProductQuantity(productRequest);
        return CommonResponse.ok(HttpStatus.OK.value(), "상품수량 수정 성공");
    }

    //장바구니 상품 삭제
    @PostMapping("/api/carts/delete")
    public ResponseEntity<CommonResponse> deleteCartsList(@RequestBody List<Long> productIds) {
        cartService.deleteProductsByCart(productIds);
        return CommonResponse.ok(HttpStatus.OK.value(), "장바구니 삭제가 완료되었습니다. ",
                Map.of("cartProducts", cartService.getCartsList()));
    }

}
