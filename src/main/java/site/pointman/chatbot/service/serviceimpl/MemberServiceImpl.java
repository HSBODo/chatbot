package site.pointman.chatbot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Duplication;
import org.json.simple.JSONObject;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.domain.member.MemberAttribute;
import site.pointman.chatbot.dto.MemberAttributeDto;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.service.MemberService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Transactional
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
    KakaoMemberRepository kakaoUserRepository;


    public MemberServiceImpl(KakaoMemberRepository kakaoUserRepository) {
        this.kakaoUserRepository = kakaoUserRepository;
    }

    @Override
    public void saveLocation(KakaoMemberLocation memberLocation) {
        String kakaoUserkey = memberLocation.getKakaoUserkey();
        try {
            Optional<KakaoMember> maybeFindMember = kakaoUserRepository.findByMember(kakaoUserkey);
            if(maybeFindMember.isEmpty()) throw new NullPointerException("존재하지 않은 회원입니다.");
            KakaoMember member = maybeFindMember.get();

            Optional<KakaoMemberLocation> maybeFindLocation = kakaoUserRepository.findByLocation(member.getKakaoUserkey());
            if(maybeFindLocation.isEmpty()){ //<================= 기존에 위치정보가 없으면 save
                kakaoUserRepository.saveLocation(memberLocation);
            }else {                             //<========= 기존에 위치정보가 있으면 update
                    kakaoUserRepository.updateLocation(kakaoUserkey,memberLocation.getX(),memberLocation.getY());
                }
        }catch (Exception e){
            e.printStackTrace();
            throw new NullPointerException("위치정보 저장에 실패하였습니다.");
        }
    }

    @Override
    public void join(KakaoMember member) {
        try {
            Optional<KakaoMember> findMember = kakaoUserRepository.findByMember(member.getKakaoUserkey());
            if(findMember.isEmpty())kakaoUserRepository.save(member); //<== 중복회원이 아니면
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
            Optional<MemberAttribute> maybeAttribute = kakaoUserRepository.findByAttribute(kakaoUserkey);
            if (maybeAttribute.isEmpty()){
                MemberAttributeDto memberAttributeDto = MemberAttributeDto.builder()
                        .optionCode(optionId)
                        .quantity(quantity)
                        .kakaoUserkey(kakaoUserkey)
                        .build();
                MemberAttribute memberAttribute = memberAttributeDto.toEntity();
                kakaoUserRepository.saveAttribute(memberAttribute);
            }else {
                if(optionId!=null) kakaoUserRepository.updateOptionAttribute(kakaoUserkey,optionId);
                if(quantity!=0)kakaoUserRepository.updateQuantityAttribute(kakaoUserkey,quantity);
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
