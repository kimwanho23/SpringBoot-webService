package kwh.awsweb.config.auth;

import kwh.awsweb.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static jakarta.servlet.DispatcherType.FORWARD;

@RequiredArgsConstructor
@EnableJpaAuditing  // Security 활성화
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headerConfig -> headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // h2-console 화면을 사용하기 위해 해당 옵션들을 disable 처리
                .authorizeHttpRequests( // antMatchers(), mvcMatchers(), regexMatchers()가 -> authorizeHttpRequests() 또는 securityMatchers()로 변경
                        request -> request
                                .dispatcherTypeMatchers(FORWARD).permitAll()
                                .requestMatchers(
                                        new AntPathRequestMatcher("/"),
                                        new AntPathRequestMatcher("/css/**"),
                                        new AntPathRequestMatcher("/images/**"),
                                        new AntPathRequestMatcher("/js/**"),
                                        new AntPathRequestMatcher("/h2-console/**"),
                                        new AntPathRequestMatcher("/profile")
                                ).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/**")).hasRole(Role.USER.name())    // 권한 관리 대상을 지정하는 옵션, URL, HTTP 메서드별로 관리가 가능, "/" 등 지정된 URL들을 permitAll() 옵션을 통해 전체 열람 권한을 줌, "/api/v1/**"주소를 가진 API는 USER 권한을 가진 사람만 가능
                                .anyRequest().authenticated()   // 설정된 값들 이외 나머지 URL들을 나타낸다. 여기선 authenticated()을 추가하여 나머지 URL들은 모두 인증된 사용자들에게만 허용. 즉, 인증된 사용자, 로그인한 사용자들만 허용
                )
                .logout((logoutConfig) -> logoutConfig.logoutSuccessUrl("/"))
                .oauth2Login(oauth2 -> oauth2                                   // OAuth2.0 로그인 기능에 대한 여러 설정의 진입점
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint  // OAuth2.0 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                                .userService(customOAuth2UserService)
                        )
                );        // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록. 리소스 서버(즉, 소설 서비스)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시

        return http.build();
    }
}