package site.pointman.chatbot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.service.MemberService;

import java.math.BigDecimal;
import java.util.Map;

@SpringBootTest
@Transactional
class KakaoMemberRepositoryTest {
    @Test
    @Commit
    void save() {

    }


    @Test
    @Commit
    void saveLocation() {

    }
    @Test
    void findByMember (){

    }
    @Test
    void updateLocation(){

    }
    @Test
    void findByLocation(){

    }

}