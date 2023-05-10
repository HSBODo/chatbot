package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import site.pointman.chatbot.domain.member.*;
import site.pointman.chatbot.dto.member.MemberLocationDto;
import site.pointman.chatbot.dto.member.MemberAttributeDto;
import site.pointman.chatbot.dto.member.MemberDto;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
@Slf4j
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Test
    @Commit
    void save() {
        MemberDto memberDto= MemberDto.builder()
                .kakaoUserkey("123123")
                .name("테스트")
                .phone("01000000000")
                .platform(Platform.KAKAO)
                .roleType(RoleType.MEMBER)
                .build();
        Member member = memberDto.toEntity();
        memberRepository.save(member);
    }

    @Test
    @Commit
    void delete() {
        memberRepository.delete("QFERwysZbO77");
    }

    @Test
    void findAll() {
    }

    @Test
    void findByMember() {
        Optional<Member> byMember = memberRepository.findByMember("123");
        Assertions.assertThat(byMember).isNotEmpty();
    }


    @Test
    void findByKakaoMember() {
        Optional<Member> maybeMember = memberRepository.findByKakaoMember("123123");
        if(maybeMember.isEmpty()) throw new NullPointerException("회원존재x");
        Assertions.assertThat(maybeMember).isNotEmpty();
    }

    @Test
    @Commit
    void saveLocation() {
        MemberLocationDto kakaoMemberLocation = MemberLocationDto.builder()
                .kakaoUserkey("test")
                .x(BigDecimal.valueOf(123))
                .y(BigDecimal.valueOf(456))
                .build();
        KakaoMemberLocation kakaoMemberLocationEntity = kakaoMemberLocation.toEntity();

        KakaoMemberLocation savedLocation = memberRepository.saveLocation(kakaoMemberLocationEntity);
        Assertions.assertThat(savedLocation).isEqualTo(kakaoMemberLocationEntity);
    }
    @Test
    void updateLocation(){
        MemberLocationDto memberLocationDto
                = MemberLocationDto.builder()
                .kakaoUserkey("test")
                .x(BigDecimal.valueOf(123))
                .y(BigDecimal.valueOf(456))
                .build();
        KakaoMemberLocation kakaoMemberLocationEntity = memberLocationDto.toEntity();
        Optional<KakaoMemberLocation> maybeMemberLocation = memberRepository.updateLocation(kakaoMemberLocationEntity.getKakaoUserkey(), kakaoMemberLocationEntity.getX(), kakaoMemberLocationEntity.getY());
        if (maybeMemberLocation.isEmpty()) throw new NullPointerException("위치정보 업데이트 실패");
        KakaoMemberLocation kakaoMemberLocation = maybeMemberLocation.get();
        Assertions.assertThat(kakaoMemberLocation.getX()).isEqualTo(memberLocationDto.getX());

    }

    @Test
    void findByLocation(){
        String kakaoUserkey= "test";
        Optional<KakaoMemberLocation> maybeMemberLocation = memberRepository.findByLocation(kakaoUserkey);
        if (maybeMemberLocation.isEmpty()) throw new NullPointerException("위치정보가 없습니다.");
        KakaoMemberLocation kakaoMemberLocation = maybeMemberLocation.get();
        Assertions.assertThat(kakaoMemberLocation.getKakaoUserkey()).isEqualTo(kakaoUserkey);
    }

    @Test
    @Commit
    void saveAttribute(){

        MemberAttributeDto memberAttributeDto = MemberAttributeDto.builder()
                .optionCode(1L)
                .quantity(2)
                .kakaoUserkey("QFERwysZbO77")
                .build();
        MemberAttribute memberAttribute = memberAttributeDto.toEntity();
        memberRepository.saveAttribute(memberAttribute);
    }
    @Test
    void findByAttribute(){
        Optional<MemberAttribute> maybeAttribute = memberRepository.findByAttribute("QFERwysZbO77");
    }

}