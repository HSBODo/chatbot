package site.pointman.chatbot.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.dto.KakaoMemberDto;
import site.pointman.chatbot.dto.KakaoMemberLocationDto;
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
        KakaoMemberDto kakaoMemberDto = KakaoMemberDto.builder()
                .partnerId("")
                .kakaoUserkey("")
                .build();
        KakaoMember member = kakaoMemberDto.toEntity();
        KakaoMember result = kakaoMemberRepository.save(member);
        Assertions.assertThat(result).isEqualTo(member);
    }


    @Test
    @Commit
    void saveLocation() {
        KakaoMemberLocationDto kakaoMemberLocation = KakaoMemberLocationDto.builder()
                .kakaoUserkey("test")
                .x(BigDecimal.valueOf(123))
                .y(BigDecimal.valueOf(456))
                .build();
        KakaoMemberLocation kakaoMemberLocationEntity = kakaoMemberLocation.toEntity();

        KakaoMemberLocation savedLocation = kakaoMemberRepository.saveLocation(kakaoMemberLocationEntity);
        Assertions.assertThat(savedLocation).isEqualTo(kakaoMemberLocationEntity);
    }
    @Test
    void updateLocation(){
        KakaoMemberLocationDto kakaoMemberLocationDto
                = KakaoMemberLocationDto.builder()
                .kakaoUserkey("test")
                .x(BigDecimal.valueOf(123))
                .y(BigDecimal.valueOf(456))
                .build();
        KakaoMemberLocation kakaoMemberLocationEntity = kakaoMemberLocationDto.toEntity();
        Optional<KakaoMemberLocation> maybeMemberLocation = kakaoMemberRepository.updateLocation(kakaoMemberLocationEntity.getKakaoUserkey(), kakaoMemberLocationEntity.getX(), kakaoMemberLocationEntity.getY());
        if (maybeMemberLocation.isEmpty()) throw new NullPointerException("위치정보 업데이트 실패");
        KakaoMemberLocation kakaoMemberLocation = maybeMemberLocation.get();
        Assertions.assertThat(kakaoMemberLocation.getX()).isEqualTo(kakaoMemberLocationDto.getX());

    }
    @Test
    void findByMember (){
        String kakaoUserkey= "test";
        Optional<KakaoMember> maybeMember = kakaoMemberRepository.findByMember(kakaoUserkey);
        if (maybeMember.isEmpty()) throw new NullPointerException("회원이 존재하지 않습니다.");
        KakaoMember member = maybeMember.get();
        KakaoMemberDto kakaoMemberDto = member.toKakaoMemberDto();
        Assertions.assertThat(member.getKakaoUserkey()).isEqualTo( kakaoMemberDto.getKakaoUserkey());

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