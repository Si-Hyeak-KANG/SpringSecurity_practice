package security.security_test.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import security.security_test.config.auth.PrincipalDetails;
import security.security_test.model.Member;
import security.security_test.repository.MemberRepository;

// 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
// userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필 정보를 받아온다.
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    
    @Autowired
    private MemberRepository memberRepository;

    // 이 메서드는 구글로부터 받은 userRequest 데이터에 대한 후처리 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oauth2User.getAttribute("sub");
        String username = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");
        String role = "ROSE_USER";

        Member memberEntity = memberRepository.findByUsername(username);

        if(memberEntity == null) {
            // OAuth로 처음 로그인한 유저 - 회원가입 처리
            memberEntity = Member.builder()
                    .username(username)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            memberRepository.save(memberEntity);
       }

        return new PrincipalDetails(memberEntity, oauth2User.getAttributes());
    }

    // userRequest 에 담긴 정보를 확인할 수 있는 메서드
    // 1. userRequest.getClientRegistration()
    // 2. userRequest.getAccessToken().getTokenValue()
    // 3. super.loadUser(userRequest).getAttributest()
}
