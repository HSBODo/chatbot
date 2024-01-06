package site.pointman.chatbot.domain.member.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.constant.MemberRole;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    private final String name = "테스트";
    private final String userKey = "test11";
    private final String phoneNumber = "01000000000";

    @Test
    void 회원가입() {

        memberService.join(userKey,name,phoneNumber);

    }

    @Test
    void 회원프로필_페이징_조회() {
        int page = 1;

        Page<MemberProfileDto> memberProfiles = memberService.getMemberProfiles(page);

        Assertions.assertThat(memberProfiles.getSize()).isLessThanOrEqualTo(5);

    }

    @Test
    void 유저키로_회원프로필_조회() {
        memberService.join(userKey,name,phoneNumber);

        MemberProfileDto memberProfileDto = memberService.getMemberProfileDto(userKey);

        Assertions.assertThat(memberProfileDto.getName()).isEqualTo(name);
        Assertions.assertThat(memberProfileDto.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void 이름으로_회원프로필_조회() {
        memberService.join(userKey,name,phoneNumber);

        MemberProfileDto memberProfileDtoByName = memberService.getMemberProfileDtoByName(name);

        Assertions.assertThat(memberProfileDtoByName.getName()).isEqualTo(name);
        Assertions.assertThat(memberProfileDtoByName.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void 회원정보_수정() {
        //give
        String updateName = "이름변경";
        String updatePhoneNumber = "01077778888";
        MemberRole updateRole = MemberRole.CUSTOMER_PLATINUM;

        memberService.join(userKey,name,phoneNumber);
        MemberProfileDto memberProfileDto = MemberProfileDto.builder()
                .name(updateName)
                .phoneNumber(updatePhoneNumber)
                .role(updateRole)
                .build();

        //when
        memberService.updateMemberProfile(name,memberProfileDto);
        MemberProfileDto memberProfileDtoByName = memberService.getMemberProfileDtoByName(updateName);

        //then
        Assertions.assertThat(memberProfileDtoByName.getName()).isEqualTo(updateName);
        Assertions.assertThat(memberProfileDtoByName.getPhoneNumber()).isEqualTo(updatePhoneNumber);
        Assertions.assertThat(memberProfileDtoByName.getRole()).isEqualTo(updateRole);
    }

    @Test
    void 회원연락처_수정() {
        //give
        String updatePhoneNumber = "01077778888";
        memberService.join(userKey,name,phoneNumber);

        //when
        memberService.updateMemberPhoneNumber(userKey,updatePhoneNumber);
        MemberProfileDto memberProfileDto = memberService.getMemberProfileDto(userKey);

        //then
        Assertions.assertThat(memberProfileDto.getPhoneNumber()).isEqualTo(updatePhoneNumber);
    }

    @Test
    void 회원탈퇴() {
        String userKey = "QFJSyeIZbO77";
        boolean isUse = true;
        List<Product> products = productRepository.findByUserKey(userKey, isUse);

        memberService.deleteMember(userKey);

        Optional<Member> deleteMember = memberRepository.findById(userKey);
        Assertions.assertThat(deleteMember.get().getIsUse()).isFalse();

        products.forEach(product -> {
            Assertions.assertThat(product.getIsUse()).isFalse();
            Assertions.assertThat(product.getProductImages().getIsUse()).isFalse();
        });
    }

    @Test
    void 유저키로_회원존재여부() {
        memberService.join(userKey,name,phoneNumber);

        boolean result = memberService.isCustomer(userKey);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    void 이름으로_회원존재여부() {
        memberService.join(userKey,name,phoneNumber);

        boolean result = memberService.isCustomerByName(name);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    void 이름과_유저키의_관리자가_존재하는지_여부() {
        memberService.join(userKey,name,phoneNumber);
        MemberProfileDto memberProfileDto = MemberProfileDto.builder()
                .role(MemberRole.ADMIN)
                .build();
        memberService.updateMemberProfile(name,memberProfileDto);

        boolean result = memberService.isAdmin(userKey,name);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    void isDuplicationName() {
        memberService.join(userKey,name,phoneNumber);

        boolean result = memberService.isDuplicationName(name);

        Assertions.assertThat(result).isTrue();
    }
}