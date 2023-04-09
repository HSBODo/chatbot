package site.pointman.chatbot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.KakaoMember;
import site.pointman.chatbot.domain.KakaoMemberLocation;
import site.pointman.chatbot.service.MemberService;

import java.util.Map;

@SpringBootTest
@Transactional
class KakaoUserLocationRepositoryTest {
    @Autowired
    KakaoMemberRepository kakaoMemberRepository;
    @Autowired
    MemberService memberService;

    @Test
    @Commit
    void saveLocation() {
        KakaoMemberLocation member = new KakaoMemberLocation();
        member.setKakaoUserkey("testuserkey");
        member.setPartnerId("pointman");
        member.setX("456456.1516");
        member.setY("1232131.14148");
        Map<String, String> result = memberService.saveLocation(member);
        System.out.println("result"+result.toString());
    }



}