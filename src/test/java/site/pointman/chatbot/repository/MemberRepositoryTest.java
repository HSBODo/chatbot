package site.pointman.chatbot.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.constant.MemberRole;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;
import site.pointman.chatbot.domain.member.dto.MemberSearchParamDto;

import java.util.Optional;

@Slf4j
@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    private boolean isUse = true;
    private String userKey = "QFJSyeIZbO77";
    private String name = "관리자";


    @Test
    void 회원프로필_전체_조회() {
        //give
        int size = 5;
        int page = 0;

        //when
        Page<MemberProfileDto> allMemberProfileDto = memberRepository.findAllMemberProfileDto(PageRequest.of(page, size), isUse);

        //then
        Assertions.assertThat(allMemberProfileDto.getSize()).isLessThanOrEqualTo(size);
    }

    @Test
    void 유저키로_회원조회() {
        //give

        //when
        Optional<Member> mayBeMember = memberRepository.findByUserKey(userKey, isUse);

        //then
        Assertions.assertThat(mayBeMember).isNotEmpty();
        Assertions.assertThat(mayBeMember.get().getUserKey()).isEqualTo(userKey);
    }

    @Test
    void 이름으로_회원조회() {

        Optional<Member> mayBeMember = memberRepository.findByName(name, isUse);

        Assertions.assertThat(mayBeMember).isNotEmpty();
        Assertions.assertThat(mayBeMember.get().getName()).isEqualTo(name);
    }

    @Test
    void 이름_유저키_동적_회원조회() {
        MemberSearchParamDto memberSearchParamDto = MemberSearchParamDto.builder()
                .userKey(userKey)
                .build();

        Optional<Member> mayBeMember = memberRepository.findMember(memberSearchParamDto);

        Assertions.assertThat(mayBeMember).isNotEmpty();
        Assertions.assertThat(mayBeMember.get().getUserKey()).isEqualTo(userKey);
    }

    @Test
    void 유저키로_회원프로필_DTO_조회() {

        Optional<MemberProfileDto> memberProfileDtoByUserKey = memberRepository.findMemberProfileDtoByUserKey(userKey,isUse);

        Assertions.assertThat(memberProfileDtoByUserKey).isNotEmpty();
        Assertions.assertThat(memberProfileDtoByUserKey.get().getName()).isEqualTo(name);
    }

    @Test
    void 이름으로_회원프로필_DTO_조회() {

        Optional<MemberProfileDto> memberProfileDtoByName = memberRepository.findMemberProfileDtoByName(name, isUse);

        Assertions.assertThat(memberProfileDtoByName).isNotEmpty();
        Assertions.assertThat(memberProfileDtoByName.get().getName()).isEqualTo(name);
    }

    @Test
    void 유저키_이름_등급_으로_회원프로필_조회() {
        MemberRole role = MemberRole.ADMIN;
        Optional<MemberProfileDto> memberProfileByRole = memberRepository.findMemberProfileByRole(name, userKey, role, isUse);

        Assertions.assertThat(memberProfileByRole).isNotEmpty();
        Assertions.assertThat(memberProfileByRole.get().getName()).isEqualTo(name);
        Assertions.assertThat(memberProfileByRole.get().getRole()).isEqualTo(role);
    }

    @Test
    void 유저키로_회원_수_조회() {
        Integer memberCountByUserKey = memberRepository.findMemberCountByUserKey(userKey, isUse);

        Assertions.assertThat(memberCountByUserKey).isEqualTo(1);
    }
}