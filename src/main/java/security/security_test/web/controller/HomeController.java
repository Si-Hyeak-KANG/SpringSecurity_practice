package security.security_test.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "홈페이지";
    }

    //사용자가 어떤 권한과 어떤 Authentication 으로 접근했는지 확인
    @RequestMapping("/auth")
    public Authentication auth() {

        return SecurityContextHolder.getContext()
                .getAuthentication();
    }

    // user가 접근할 수 있는 페이지
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @RequestMapping("/user")
    public SecurityMessage user() {

        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("User 정보")
                .build();
    }

    // admin이 접근할 수 있는 페이지
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping("/admin")
    public SecurityMessage admin() {

        return SecurityMessage.builder().
                auth(SecurityContextHolder.getContext().getAuthentication())
                .message("관리자 정보").build();
    }
}
