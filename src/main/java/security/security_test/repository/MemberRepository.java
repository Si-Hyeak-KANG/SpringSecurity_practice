package security.security_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.security_test.model.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
