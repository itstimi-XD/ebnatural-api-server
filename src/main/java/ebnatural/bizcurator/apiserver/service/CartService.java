package ebnatural.bizcurator.apiserver.service;


import ebnatural.bizcurator.apiserver.domain.Cart;
import ebnatural.bizcurator.apiserver.domain.Member;
import ebnatural.bizcurator.apiserver.domain.Product;
import ebnatural.bizcurator.apiserver.dto.CartProductDto;
import ebnatural.bizcurator.apiserver.dto.request.CartProductRequest;
import ebnatural.bizcurator.apiserver.repository.CartRepository;
import ebnatural.bizcurator.apiserver.repository.MemberRepository;
import ebnatural.bizcurator.apiserver.repository.ProductImageRepository;
import ebnatural.bizcurator.apiserver.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class CartService {
    private final ProductService productService;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    //장바구니 조회
    public List<CartProductDto> getCartsList() {
        Long memberId = getMember().getId();

        List<Cart> cartList = cartRepository.findByMemberId(memberId);
        List<CartProductDto> cartProductDtos = new ArrayList<>();
        for (Cart carts: cartList) {
            Product product = carts.getProduct();
            //ProductImage productMainImage = productService.getProductMainImage(product.getId());
            String mainImageUrl = productService.getProductMainImage(product.getId()).getImgUrl();

            cartProductDtos.add(new CartProductDto(product.getName(), product.getId(), product.getCostWithDiscount(),
                    product.getRegularPrice(), carts.getQuantity(), product.getMinQuantity(), mainImageUrl)) ;
        }
        return cartProductDtos;
    }

    //장바구니에 제품 담기
    public void containingCartProducts(CartProductRequest cartProductRequest) {
        Member member = getMember();
        Long memberId = member.getId();
        Long productId = cartProductRequest.getProductId();
        int addQuantity = cartProductRequest.getQuantity();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("장바구니에 담을 productId가 잘못됐습니다."));
        Optional<Cart> cart = Optional.ofNullable(cartRepository.findByMemberIdAndProductId(memberId, productId));
        boolean isAlreadyInCart = cart.isPresent();

        if (isAlreadyInCart == true){
            cart.get().addQuantity(addQuantity);
        }else {
            cartRepository.save(Cart.of(member, product, addQuantity));
        }
    }

    //장바구니 상품 수량 수정
    public void updateProductQuantity(CartProductRequest productRequest) {
        Member member = getMember();
        Long memberId = member.getId();
        Long productId = productRequest.getProductId();
        int updateQuantity = productRequest.getQuantity();
        Optional.of(cartRepository.findByMemberIdAndProductId(memberId, productId))
                .map(cart -> cart.updateQuantity(updateQuantity))
                .orElseThrow(() -> new NoSuchElementException("장바구니에 담을 productId가 잘못됐습니다."));
    }

    //장바구니 상품 삭제
    public void deleteProductsByCart(List<Long> productIds) {
        Member member = getMember();
        Long memberId = member.getId();
        for (Long productId : productIds)
            cartRepository.deleteInQuery(memberId, productId);
    }

    public Member getMember() { // 로그인한 유저의 로그인정보 반환
        //todo
        //Long memberId = MemberUtil.getMemberId();
        Long memberId = 3L;
        return memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
    }

    //Cart에 Request로 들어온 ProductId가 있는지 체크
//    private Cart checkExist(Long productId) {
//        Cart cart = cartRepository.findByProduct_Id(productId).orElseThrow(() -> new NoSuchElementException());
//        return cart;
//    }


}
