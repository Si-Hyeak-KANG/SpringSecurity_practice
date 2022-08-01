package security.security_test.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;


// 전역 또는 특정 구역에서 사용 가능
// @Component 지우고, @WebFilter(urlPatterns = "/api/user/*") -> 배열로 여러가지 url도 등록가능
@Slf4j
@Component
public class GlobalFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 전처리
        //HttpServletRequest httpServletRequest = (HttpServletRequest) request; // request 형변환
        //HttpServletResponse httpServletResponse = (HttpServletResponse) response; // response 형변환

        // 몇번이고 읽을 수 있도록 content 에 담아둠.
        // br의 오류 방지용
        // 길이만 초기화
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        // request의 정보를 읽음 -> bufferedReader 반환
//        BufferedReader br = httpServletRequest.getReader();
//
//        br.lines().forEach(line -> {
//            log.info("url : {}, line : {}", url, line);
//        });

        // bufferedReader 를 사용할 경우 request body를 한번 받을 때, 이미 다 읽어버림 -> 더 이상 읽을게 없어짐
        // 왜냐 라인별로 읽기 때문에

        chain.doFilter(httpServletRequest,httpServletResponse); // doFilter를 진행해야 내용이 담김

        // 후처리

        String url = httpServletRequest.getRequestURI();// 어떠한 주소로 요청한건지

        // request 정보를 바이트 array로 받고 문자화 (default가 utf-8)
        String reqContent = new String(httpServletRequest.getContentAsByteArray());
        log.info("request url : {}, request body : {}", url, reqContent);

        String resContent = new String(httpServletResponse.getContentAsByteArray());
        int httpStatus = httpServletResponse.getStatus();

        httpServletResponse.copyBodyToResponse(); // 반드시 호출해줘야 클라이언트가 응답을 받을 수 있음.

        log.info("response status : {}, response body : {}", httpStatus, resContent);

    }
}
