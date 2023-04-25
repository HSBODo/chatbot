package site.pointman.chatbot.repository;

import org.assertj.core.api.Assertions;
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
import java.util.Optional;

@SpringBootTest
@Transactional
class KakaoMemberRepositoryTest {
    @Autowired
    private KakaoMemberRepository kakaoMemberRepository;
    @Test
    @Commit
    void save() {
        KakaoMember member = KakaoMember.builder()
                .kakaoUserkey("test1")
                .partnerId("test")
                .build();
        KakaoMember result = kakaoMemberRepository.save(member);
        Assertions.assertThat(result).isEqualTo(member);
    }


    @Test
    @Commit
    void saveLocation() {
        KakaoMemberLocation kakaoMemberLocation = KakaoMemberLocation.builder()
                .kakaoUserkey("test")
                .x(BigDecimal.valueOf(123))
                .y(BigDecimal.valueOf(456))
                .build();
        KakaoMemberLocation savedLocation = kakaoMemberRepository.saveLocation(kakaoMemberLocation);
        Assertions.assertThat(savedLocation).isEqualTo(kakaoMemberLocation);
    }
    @Test
    void findByMember (){
        String kakaoUserkey= "test";
        Optional<KakaoMember> maybeMember = kakaoMemberRepository.findByMember(kakaoUserkey);
        if (maybeMember.isEmpty()) throw new NullPointerException("회원이 존재하지 않습니다.");
        KakaoMember member = maybeMember.get();
        Assertions.assertThat(member.getKakaoUserkey()).isEqualTo(kakaoUserkey);

    }
    @Test
    @Commit
    void updateLocation(){
        String kakaoUserkey= "test";
        BigDecimal x = BigDecimal.valueOf(13);
        BigDecimal y = BigDecimal.valueOf(456);
        Optional<KakaoMemberLocation> maybeMemberLocation = kakaoMemberRepository.updateLocation(kakaoUserkey, x, y);
        if (maybeMemberLocation.isEmpty()) throw new NullPointerException("위치정보 업데이트 실패");
        KakaoMemberLocation kakaoMemberLocation = maybeMemberLocation.get();
        Assertions.assertThat(kakaoMemberLocation.getX()).isEqualTo(x);

    }
    @Test
    void findByLocation(){
        String kakaoUserkey= "test";
        Optional<KakaoMemberLocation> maybeMemberLocation = kakaoMemberRepository.findByLocation(kakaoUserkey);
        if (maybeMemberLocation.isEmpty()) throw new NullPointerException("위치정보가 없습니다.");
        KakaoMemberLocation kakaoMemberLocation = maybeMemberLocation.get();
        Assertions.assertThat(kakaoMemberLocation.getKakaoUserkey()).isEqualTo(kakaoUserkey);
    }

}