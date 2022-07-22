package security.security_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.security_test.model.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByUsername(String username);
}
