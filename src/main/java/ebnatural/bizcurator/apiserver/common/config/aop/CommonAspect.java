package ebnatural.bizcurator.apiserver.common.config.aop;

import ebnatural.bizcurator.apiserver.common.util.MemberUtil;
import ebnatural.bizcurator.apiserver.dto.MemberDto;
import ebnatural.bizcurator.apiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class CommonAspect {

    private final MemberService memberService;
    @Around("@annotation(LoginLog)")
    public Object logSuccessfulLogin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        MemberDto result = (MemberDto) proceedingJoinPoint.proceed();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();

        memberService.logSuccessfulLogin(result, userAgent, ipAddress);

        return result;
    }

    @AfterReturning("@annotation(CleanAuth)")
    public void cleanAuth(){
        MemberUtil.clean();
    }
    
}
