package site.pointman.chatbot.domain.member.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.response.constant.ResultCode;
import site.pointman.chatbot.domain.member.constant.MemberRole;
import site.pointman.chatbot.domain.order.constatnt.OrderStatus;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.order.Order;
import site.pointman.chatbot.domain.product.Product;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.domain.chatbot.response.property.common.Profile;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;
import site.pointman.chatbot.domain.product.dto.ProductImageDto;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.repository.OrderRepository;
import site.pointman.chatbot.repository.ProductRepository;
import site.pointman.chatbot.domain.member.service.MemberService;
import site.pointman.chatbot.globalservice.S3FileService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
    private boolean isUse = true;
    private int pageSize = 5;

    MemberRepository memberRepository;
    OrderRepository orderRepository;
    ProductRepository productRepository;

    S3FileService s3FileService;

    public MemberServiceImpl(MemberRepository memberRepository, OrderRepository orderRepository, ProductRepository productRepository, S3FileService s3FileService) {
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.s3FileService = s3FileService;
    }

    @Override
    public Response join(String userKey, String name, String phoneNumber) {
        try {

            Member member = Member.builder()
                    .userKey(userKey)
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .profile(new Profile(name))
                    .memberRole(MemberRole.CUSTOMER_BRONZE)
                    .build();

            memberRepository.saveAndFlush(member);

            return new Response(ResultCode.OK,"회원가입을 성공적으로 완료하였습니다.");
        }catch (Exception e) {
            return new Response(ResultCode.FAIL,"회원가입을 실패하였습니다. e= "+e.getMessage());
        }
    }

    @Override
    public Page<MemberProfileDto> getMemberProfiles(int page){
        page = page-1;

        Page<MemberProfileDto> members = memberRepository.findAllMemberProfileDto(PageRequest.of(page,pageSize),isUse);

        return members;
    }

    @Override
    public Optional<MemberProfileDto> getMemberProfileDto(String userKey) {
        Optional<MemberProfileDto> mayBeMemberProfile = memberRepository.findMemberProfileDtoByUserKey(userKey, isUse);
        return mayBeMemberProfile;
    }

    @Override
    public Optional<MemberProfileDto> getMemberProfileDtoByName(String name) {
        Optional<MemberProfileDto> mayBeMember = memberRepository.findMemberProfileDtoByName(name, isUse);
        return mayBeMember;
    }

    @Override
    public Response updateMember(String name, MemberProfileDto memberProfileDto) {
        try {
            memberRepository.updateMember(name,memberProfileDto,isUse);
            return new Response(ResultCode.OK,"회원정보 변경을 완료하였습니다.");
        }catch (Exception e){
            return new Response(ResultCode.FAIL,"회원정보 변경을 실패하였습니다.");
        }
    }

    @Override
    public Response updateMemberPhoneNumber(String userKey, String updatePhoneNumber) {
        try {
            memberRepository.updateMemberPhoneNumber(userKey, updatePhoneNumber,isUse);

            return new Response(ResultCode.OK,"연락처를 수정하였습니다.");
        }catch (Exception e) {

            return new Response(ResultCode.FAIL,"연락처 수정을 실패하였습니다. e= "+e.getMessage());
        }
    }

    @Override
    public Response deleteMember(String userKey) {
        try {
            List<Order> orders = orderRepository.findByBuyerUserKey(userKey);

            for (Order order : orders) {
                if (order.getStatus().equals(OrderStatus.주문체결)){
                    return new Response(ResultCode.EXCEPTION,"구매중인 주문이 있어 회원탈퇴가 불가능합니다.");
                }
            }

            List<Product> products = productRepository.findByUserKey(userKey, isUse);

            for (Product product : products) {
                Optional<Order> salesProduct = orderRepository.findByProductId(product.getId(), OrderStatus.주문체결);
                if (!salesProduct.isEmpty()) return new Response(ResultCode.EXCEPTION,"거래가 체결된 상품이 있어 회원탈퇴가 불가능합니다.");
            }

            memberRepository.delete(userKey);

            return new Response(ResultCode.OK,"회원탈퇴를 성공적으로 완료하였습니다.");
        }catch (Exception e) {
            return new Response(ResultCode.FAIL,"회원탈퇴를 실패하였습니다.");
        }
    }

    @Override
    @Transactional
    public Response updateMemberProfileImage(String userKey, String profileImageUrl) {
        try {
            Member member = memberRepository.findByUserKey(userKey,isUse).get();
            List<String> uploadImages = new ArrayList<>();
            uploadImages.add(profileImageUrl);

            ProductImageDto productImageDto = s3FileService.uploadImages(uploadImages, userKey,member.getName(),"image/profile");

            member.changeMemberProfileImage(productImageDto.getImageUrls().get(0));

            return new Response(ResultCode.OK,"성공적으로 프로필사진을 변경하였습니다.");
        }catch (Exception e) {
            return new Response(ResultCode.FAIL,"프로필사진 등록을 실패하였습니다.");
        }
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
    public boolean isAdmin(String name,String userKey) {
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
