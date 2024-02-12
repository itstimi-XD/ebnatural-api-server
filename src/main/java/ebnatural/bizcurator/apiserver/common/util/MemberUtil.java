package ebnatural.bizcurator.apiserver.common.util;

import ebnatural.bizcurator.apiserver.common.config.aop.CleanAuth;
import ebnatural.bizcurator.apiserver.dto.MemberPrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MemberUtil {
    private static Authentication authentication;
    private static String token;
    @CleanAuth
    public static String getUsername() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((MemberPrincipalDetails)(authentication.getPrincipal())).getUsername();
    }
    @CleanAuth
    public static Long getMemberId(){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((MemberPrincipalDetails)(authentication.getPrincipal())).getId();
    }

    @CleanAuth
    public static String getToken(){
        return MemberUtil.token;
    }

    public static void setToken(String token){
        MemberUtil.token = token;
    }
    public static void clean(){
        authentication = null;
    }
}
