package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CartRepository extends JpaRepository<Cart, Long> {

    //Optional<List<Cart>> findByMemberId(Long memberId);//memberId로 유저에 해당하는 장바구니 리스트 조회
    List<Cart> findByMemberId(Long memberId);//memberId로 유저에 해당하는 장바구니 리스트 조회
    Cart findByProduct_Id(Long productId);


    @Modifying
    @Query("DELETE FROM Cart c where c.member.id = ?1 and c.product.id = ?2 ")
    void deleteInQuery(Long memberId, Long productId);

    @Query("SELECT c from Cart c WHERE c.member.id = ?1 and c.product.id = ?2")
    Cart findByMemberIdAndProductId(Long memberId, Long productId);
}
