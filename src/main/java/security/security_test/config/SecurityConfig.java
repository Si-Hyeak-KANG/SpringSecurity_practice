package security.security_test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // filterChain 메서드를 @Bean 으로 등록한 후 스프링 컨테이너에서 관리할 수 있도록 함
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // form 태그로만 요청이 가능해지고 postman 등의 요청이 불가능하게 됨.
        http.csrf().disable();

        // h2 연결할 때 필요
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()  // '/','/login','/join' 3개의 url 은 로그인 없이도 접속 가능
                .and()
                .formLogin().loginPage("/login"); // 접근 권한으로 에러가 발생하면 login 페이지로 이동
        return http.build();
    }

    // 패스워드 암호화를 위한 코드
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
