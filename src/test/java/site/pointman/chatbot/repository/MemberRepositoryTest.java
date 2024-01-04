package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.constant.member.MemberRole;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;

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

        Optional<MemberProfileDto> mayBeMember = memberRepository.findMemberProfileByRole(name, userKey,admin ,isUse);

        Assertions.assertThat(mayBeMember).isNotEmpty();
        Assertions.assertThat(mayBeMember.get().getName()).isEqualTo(name);

        Assertions.assertThat(mayBeMember.get().getRole()).isEqualTo(admin);
    }

    @Test
    @Transactional
    void updateCustomerPhoneNumber() {
        //give
        String phoneNumber = "01011112222";

        //when
        Integer integer = memberRepository.updateMemberPhoneNumber(userKey, phoneNumber, isUse);


        //then
        Assertions.assertThat(integer.intValue()).isEqualTo(1);
    }

//    @Test
//    @Transactional
//    void updateMember() {
//       MemberJoinDto memberJoinDto = MemberJoinDto.builder()
//               .name("이름")
//               .phoneNumber("01000001111")
//               .build();
//        Member member = memberJoinDto.toEntity();
//
//        Member updateMember = memberRepository.updateMember(userKey, member, isUse);
//
//        //then
//        Assertions.assertThat(updateMember.getPhoneNumber()).isEqualTo(memberJoinDto.getPhoneNumber());
//        Assertions.assertThat(updateMember.getName()).isEqualTo(memberJoinDto.nickname());
//    }

    @Test
    @Transactional
    void delete() {
        //give
        String userKey = "userKey";

        //when
        memberRepository.delete(userKey);

        //then
    }


    @Test
    void findMemberProfileDtoByUserKey() {

        Optional<MemberProfileDto> memberProfileDtoByUserKey = memberRepository.findMemberProfileDtoByUserKey(userKey, isUse);

        Assertions.assertThat(memberProfileDtoByUserKey.isEmpty()).isFalse();
        Assertions.assertThat(memberProfileDtoByUserKey.get().getName()).isEqualTo(name);

    }

    @Test
    void findMemberCountByUserKey() {
        Integer memberCountByUserKey = memberRepository.findMemberCountByUserKey(userKey, isUse);


        Assertions.assertThat(memberCountByUserKey).isEqualTo(1);

    }

    @Test
    void findAll() {
        int size =5;
        int page = 0;

        Page<MemberProfileDto> all = memberRepository.findAllMemberProfileDto(PageRequest.of(page, size), isUse);

        Assertions.assertThat(all.getSize()).isLessThanOrEqualTo(size);

    }
}