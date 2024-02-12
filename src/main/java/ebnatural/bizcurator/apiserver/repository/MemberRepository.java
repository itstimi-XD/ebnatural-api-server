package ebnatural.bizcurator.apiserver.repository;

import ebnatural.bizcurator.apiserver.domain.Member;
import ebnatural.bizcurator.apiserver.dto.MemberDto;
import ebnatural.bizcurator.apiserver.repository.querydsl.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    @Query("SELECT m FROM Member m WHERE m.username = ?1 and m.isEnable = true")
    Member findByUsername(String username);
    @Query("SELECT m FROM Member m WHERE m.id = ?1 and m.isEnable = true")
    Optional<Member> findById(Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Member u set u.refreshToken = ?1 where u.id = ?2")
    void updateRefreshToken(String newRefreshToken, Long id);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.lastLoginTime = :currentDate and m.isEnable = true")
    int countByLastLoginTime(@Param("currentDate") LocalDate currentDate);
    @Query("SELECT m FROM Member m WHERE m.id = :id and m.isEnable = true")
    Member findByUserId(@Param("id") Long userId);

    @Query("SELECT m FROM Member m WHERE m.isEnable = true ORDER BY m.id ASC")
    List<Member> getAllMember();

    @Query("DELETE FROM Member m where m.id = ?1")
    void deleteById(Long id);

    @Query("SELECT count(m) FROM Member m where m.isEnable = true")
    int countTotalMember();

    @Transactional
    @Modifying
    @Query("UPDATE Member m SET m.lastLoginTime = ?2 WHERE m.id = ?1")
    void updateLastLoginTimeToNowById(Long id, LocalDate now);
}
