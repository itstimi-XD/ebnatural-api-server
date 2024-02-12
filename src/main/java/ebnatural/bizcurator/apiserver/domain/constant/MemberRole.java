package ebnatural.bizcurator.apiserver.domain.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberRole {
    ROLE_USER("일반유저"),
    ROLE_ADMIN("관리자");

    private final String authority;
    public String getAuthority(){
        return authority;
    }

}
