package site.pointman.chatbot.domain.member.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import site.pointman.chatbot.domain.chatbot.response.property.common.Profile;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.constant.MemberRole;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;
import site.pointman.chatbot.domain.member.service.MemberService;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.product.dto.ProductImageDto;
import site.pointman.chatbot.exception.DuplicationMember;
import site.pointman.chatbot.exception.NotFoundMember;
import site.pointman.chatbot.globalservice.S3FileService;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final boolean isUse = true;
    private final int pageSize = 5;

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final S3FileService s3FileService;


    @Transactional
    @Override
    public void join(String userKey, String name, String phoneNumber) {
        Optional<Member> mayBeDuplicationMember = memberRepository.findByUserKey(userKey, isUse);

        if (!mayBeDuplicationMember.isEmpty()) throw new DuplicationMember("중복되는 회원입니다.");

        Profile profile = new Profile(name);

        Member member = Member.builder()
                .userKey(userKey)
                .name(name)
                .phoneNumber(phoneNumber)
                .profile(profile)
                .memberRole(MemberRole.CUSTOMER_BRONZE)
                .build();

        memberRepository.save(member);
    }

    @Override
    public Page<MemberProfileDto> getMemberProfiles(int page){

        Page<MemberProfileDto> members = memberRepository.findAllMemberProfileDto(PageRequest.of(page,pageSize),isUse);

        return members;
    }

    @Override
    public MemberProfileDto getMemberProfileDto(String userKey) {
        MemberProfileDto memberProfileDto = memberRepository.findMemberProfileDtoByUserKey(userKey, isUse)
                .orElseThrow(() -> new NotFoundMember("회원이 존재하지 않습니다."));

        return memberProfileDto;
    }

    @Override
    public MemberProfileDto getMemberProfileDtoByName(String name) {
        MemberProfileDto memberProfileDto = memberRepository.findMemberProfileDtoByName(name, isUse)
                .orElseThrow(() -> new NotFoundMember("회원이 존재하지 않습니다."));

        return memberProfileDto;
    }

    @Transactional
    @Override
    public void updateMemberProfile(String name, MemberProfileDto memberProfileDto) {
        Member updateMember = memberRepository.findByName(name, isUse)
                .orElseThrow(() -> new NotFoundMember("정보를 수정할 회원이 존재하지 않습니다."));

        //이름 변경
        if (StringUtils.hasText(memberProfileDto.getName())) updateMember.changeName(memberProfileDto.getName());
        //연락처 변경
        if (StringUtils.hasText(memberProfileDto.getPhoneNumber())) updateMember.changePhoneNumber(memberProfileDto.getPhoneNumber());
        //등급 변경
        if (Objects.nonNull(memberProfileDto.getRole())) updateMember.changeRole(memberProfileDto.getRole());
    }

    @Transactional
    @Override
    public void updateMemberPhoneNumber(String userKey, String updatePhoneNumber) {
        Member member = memberRepository.findByUserKey(userKey, isUse)
                .orElseThrow(() -> new NotFoundMember("회원이 존재하지 않습니다."));

        member.changePhoneNumber(updatePhoneNumber);
    }

    @Transactional
    @Override
    public void deleteMember(String userKey) {

        List<Order> purchaseOrders = orderRepository.findByBuyerUserKey(userKey);
        purchaseOrders.stream().forEach(order -> {
            if (order.isTrading()) throw new IllegalStateException("구매중인 상품이 존재합니다.");
        });

        List<Product> products = productRepository.findByUserKey(userKey, isUse);
        products.stream().forEach(product -> {
            if (product.isTrading()) throw new IllegalStateException("거래가 체결된 상품이 존재합니다.");
        });

        Member removeMember = memberRepository.findByUserKey(userKey, isUse)
                .orElseThrow(() -> new NotFoundMember("회원이 존재하지 않습니다."));

        //회원 isUse = false
        removeMember.delete();

        for (Product product : products) {
            //상품 isUse = false
            //상품이미지 isUse = false
            product.deleteProduct();
        }
    }

    @Transactional
    @Override
    public void updateMemberProfileImage(String userKey, String profileImageUrl) {
        Member member = memberRepository.findByUserKey(userKey,isUse)
                .orElseThrow(() -> new NotFoundMember("회원이 존재하지 않습니다."));

        List<String> uploadImages = new ArrayList<>();
        uploadImages.add(profileImageUrl);

        ProductImageDto productImageDto = s3FileService.uploadImages(uploadImages, userKey,member.getName(),"image/profile");

        member.changeMemberProfileImage(productImageDto.getImageUrls().get(0));
    }

    @Override
    public boolean isCustomer(String userKey) {
        Integer memberCountByUserKey = memberRepository.findMemberCountByUserKey(userKey, isUse);


        if (!memberCountByUserKey.equals(1)) return false;

        return true;
    }

    @Override
    public boolean isCustomerByName(String name) {
        Optional<MemberProfileDto> mayBeCustomer = memberRepository.findMemberProfileDtoByName(name,isUse);

        if (mayBeCustomer.isEmpty()) return false;

        return true;
    }

    @Override
    public boolean isAdmin(String userKey, String name) {
        Optional<MemberProfileDto> mayBeCustomer = memberRepository.findMemberProfileByRole(name,userKey,MemberRole.ADMIN,isUse);

        if (mayBeCustomer.isEmpty()) return false;

        return true;
    }

    @Override
    public boolean isDuplicationName(String name) {
        Optional<MemberProfileDto> mayBeMember = memberRepository.findMemberProfileDtoByName(name, isUse);
        if (mayBeMember.isEmpty()) return false;

        return true;
    }
}
