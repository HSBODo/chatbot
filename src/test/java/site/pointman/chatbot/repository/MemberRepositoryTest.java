package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.constant.member.MemberRole;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.dto.member.MemberDto;
import site.pointman.chatbot.dto.member.MemberProfileDto;

import java.util.Optional;

@Slf4j
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    private boolean isUse = true;
    private String userKey = "QFJSyeIZbO77";
    private String name = "라이언";

    @Test
    @Transactional
    void findByUserKey() {
        Optional<Member> mayBeMember = memberRepository.findByUserKey(userKey, isUse);

        Assertions.assertThat(mayBeMember).isNotEmpty();
        Assertions.assertThat(mayBeMember.get().getName()).isEqualTo(name);
        Assertions.assertThat(mayBeMember.get().getUserKey()).isEqualTo(userKey);
    }

    @Test
    @Transactional
    void findByName() {
        Optional<Member> mayBeMember = memberRepository.findByName(name, isUse);

        Assertions.assertThat(mayBeMember).isNotEmpty();
        Assertions.assertThat(mayBeMember.get().getName()).isEqualTo(name);
        Assertions.assertThat(mayBeMember.get().getUserKey()).isEqualTo(userKey);
    }

    @Test
    @Transactional
    void findByRole() {
        MemberRole admin = MemberRole.ADMIN;

        Optional<Member> mayBeMember = memberRepository.findByRole(name, userKey,admin ,isUse);

        Assertions.assertThat(mayBeMember).isNotEmpty();
        Assertions.assertThat(mayBeMember.get().getName()).isEqualTo(name);
        Assertions.assertThat(mayBeMember.get().getUserKey()).isEqualTo(userKey);
        Assertions.assertThat(mayBeMember.get().getRole()).isEqualTo(admin);
    }

    @Test
    @Transactional
    void updateCustomerPhoneNumber() {
        //give
        String phoneNumber = "01011112222";

        //when
        Member member = memberRepository.updateMemberPhoneNumber(userKey, phoneNumber, isUse);

        //then
        Assertions.assertThat(member.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    @Transactional
    void updateMember() {
        MemberDto memberDto = MemberDto.builder()
                .name("이름")
                .phoneNumber("01015154789")
                .build();
        Member member = memberDto.toEntity();

        Member updateMember = memberRepository.updateMember(userKey, member, isUse);

        //then
        Assertions.assertThat(updateMember.getPhoneNumber()).isEqualTo(memberDto.getPhoneNumber());
        Assertions.assertThat(updateMember.getName()).isEqualTo(memberDto.getName());
    }

    @Test
    @Transactional
    void delete() {
        //give
        String userKey = "userKey";

        //when
        memberRepository.delete(userKey,isUse);

        //then
    }


    @Test
    void findMemberProfileDtoByUserKey() {

        Optional<MemberProfileDto> memberProfileDtoByUserKey = memberRepository.findMemberProfileDtoByUserKey(userKey, isUse);

        Assertions.assertThat(memberProfileDtoByUserKey.isEmpty()).isFalse();
        Assertions.assertThat(memberProfileDtoByUserKey.get().getNickname()).isEqualTo(name);

    }
}