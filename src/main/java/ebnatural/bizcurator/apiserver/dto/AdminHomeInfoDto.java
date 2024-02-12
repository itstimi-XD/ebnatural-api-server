package ebnatural.bizcurator.apiserver.dto;

import lombok.Getter;

/**
 * 관리자페이지 홈화면에 필요한 회원 관련 데이터
 */
@Getter
public class AdminHomeInfoDto {

    private int totalUserCount;
    private int userCountPerDay;

    private AdminHomeInfoDto(int totalUserCount, int userCountPerDay) {
        this.totalUserCount = totalUserCount;
        this.userCountPerDay = userCountPerDay;
    }

    public static AdminHomeInfoDto of(int totalUserCount, int userCountPerDay) {
        return new AdminHomeInfoDto(totalUserCount, userCountPerDay);
    }
}
