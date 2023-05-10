package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.member.Member;
import site.pointman.chatbot.domain.member.MemberAttribute;
import site.pointman.chatbot.dto.member.MemberAttributeDto;
import site.pointman.chatbot.dto.member.MemberDto;
import site.pointman.chatbot.repository.MemberRepository;
import site.pointman.chatbot.service.MemberService;

import java.util.Optional;
@Transactional
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
    MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean isKakaoMember(String kakaoUserkey) {
        Optional<Member> maybeKakaoMember = memberRepository.findByKakaoMember(kakaoUserkey);
        if(maybeKakaoMember.isEmpty()) return false;
        return true;
    }

    @Override
    public boolean isWithdrawalKakaoMember(String kakaoUserkey) {
        Optional<Member> maybeKakaoMember = memberRepository.findByKakaoWithdrawalMember(kakaoUserkey);
        if(maybeKakaoMember.isEmpty()) return false;
        return true;
    }

    @Override
    public void saveLocation(KakaoMemberLocation memberLocation) {
        String kakaoUserkey = memberLocation.getKakaoUserkey();
        try {
            Optional<Member> maybeFindMember = memberRepository.findByMember(kakaoUserkey);
            if(maybeFindMember.isEmpty()) throw new NullPointerException("존재하지 않은 회원입니다.");
            Member member = maybeFindMember.get();

            Optional<KakaoMemberLocation> maybeFindLocation = memberRepository.findByLocation(member.getKakaoUserkey());
            if(maybeFindLocation.isEmpty()){ //<================= 기존에 위치정보가 없으면 save
                memberRepository.saveLocation(memberLocation);
            }else {                             //<========= 기존에 위치정보가 있으면 update
                memberRepository.updateLocation(kakaoUserkey,memberLocation.getX(),memberLocation.getY());
                }
        }catch (Exception e){
            e.printStackTrace();
            throw new NullPointerException("위치정보 저장에 실패하였습니다.");
        }
    }

    @Override
    public void Withdrawal(String kakaoUserkey) {
        memberRepository.delete(kakaoUserkey);
    }

    @Override
    public void join(MemberDto memberDto) {
        try {
            Optional<Member> findMember = memberRepository.findByMember(memberDto.getKakaoUserkey());
            if(!findMember.isEmpty()) throw new DuplicateKeyException("중복회원입니다.");
            Member member = memberDto.toEntity();
            memberRepository.save(member);
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("회원가입에 실패하였습니다.");
        }
    }

    @Override
    public void saveAttribute(JSONObject buttonParams, String kakaoUserkey) {
        try {
            Long optionId = buttonParams.get("optionId")==null?null:Long.parseLong((String) buttonParams.get("optionId"));
            int quantity = buttonParams.get("quantity")==null?0:Integer.parseInt((String) buttonParams.get("quantity"));
            log.info("optionCode={} quantity={}",optionId,quantity);
            Optional<MemberAttribute> maybeAttribute = memberRepository.findByAttribute(kakaoUserkey);
            if (maybeAttribute.isEmpty()){
                MemberAttributeDto memberAttributeDto = MemberAttributeDto.builder()
                        .optionCode(optionId)
                        .quantity(quantity)
                        .kakaoUserkey(kakaoUserkey)
                        .build();
                MemberAttribute memberAttribute = memberAttributeDto.toEntity();
                memberRepository.saveAttribute(memberAttribute);
            }else {
                if(optionId!=null) memberRepository.updateOptionAttribute(kakaoUserkey,optionId);
                if(quantity!=0)memberRepository.updateQuantityAttribute(kakaoUserkey,quantity);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("속성저장 실패");
        }
    }

    @Override
    public void updateAttribute(JSONObject buttonParams, String kakaoUserkey) {

    }
}
