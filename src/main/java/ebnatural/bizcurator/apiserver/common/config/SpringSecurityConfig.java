package ebnatural.bizcurator.apiserver.common.config;

import ebnatural.bizcurator.apiserver.common.config.filter.JwtFilter;
import ebnatural.bizcurator.apiserver.common.exception.FilterExceptionHandler;
import ebnatural.bizcurator.apiserver.common.jwt.JwtProvider;
import ebnatural.bizcurator.apiserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SpringSecurityConfig {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http.csrf().disable();
        http.rememberMe().disable();
        http.cors();
        http.formLogin().disable();

        http.authorizeRequests()
                .antMatchers("/api/users/refresh").authenticated()
                .anyRequest().permitAll();

//        http.authorizeRequests()
//                .antMatchers("/api/users/findEmail", "/api/users/findPassword", "/api/users/emailConfirm",
//                "/api/users/certificationNumberConfirm", "/api/users/setNewPwd",
//                "/api/users/login", "/api/users/signup").permitAll()
//                .antMatchers("/api/admins/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
//                .antMatchers(HttpMethod.POST, "/api/notices/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.PUT, "/api/notices/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/api/notices/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/requests/partners").hasRole("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/requests/orders").hasRole("ADMIN")
//                .anyRequest().authenticated();


        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        // 인증문제가 발생했을 때 이 부분을 호출한다.
                        throw authException;
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        // 권한이 없는 페이지 요청을 하면 이 부분을 호출한다.
                        throw accessDeniedException;
                    }
                });
//
        http.addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class);
        http.addFilterBefore(new FilterExceptionHandler(), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
    JwtFilter jwtAuthorizationFilter() {
        return new JwtFilter(jwtProvider, memberRepository);
    }
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
