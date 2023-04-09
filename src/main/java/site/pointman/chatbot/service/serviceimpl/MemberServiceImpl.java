package site.pointman.chatbot.service.serviceimpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.pointman.chatbot.domain.member.KakaoMember;
import site.pointman.chatbot.domain.member.KakaoMemberLocation;
import site.pointman.chatbot.repository.KakaoMemberRepository;
import site.pointman.chatbot.service.MemberService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Transactional
@Service
public class MemberServiceImpl implements MemberService {
    KakaoMemberRepository kakaoUserRepository;


    public MemberServiceImpl(KakaoMemberRepository kakaoUserRepository) {
        this.kakaoUserRepository = kakaoUserRepository;
    }

    @Override
    public Map<String,String> saveLocation(KakaoMemberLocation userLocation) {

        return  validateDuplicateMemberLocation(userLocation);
    }

    @Override
    public Map<String,String> join(KakaoMember user) {
        return  validateDuplicateMember(user);
    }


    private Map<String,String> validateDuplicateMember(KakaoMember user) {
        Map result = new HashMap<>();
        Optional<KakaoMember> findUser = kakaoUserRepository.findByUserkey(user.getKakaoUserkey());
        if(!findUser.isPresent()){
            kakaoUserRepository.save(user);
            result.put("msg","회원가입 완료.");
            result.put("code",1);
            result.put("kakaoUserkey",user.getKakaoUserkey());
        }else {
            result.put("msg","중복회원");
            result.put("code",0);
            result.put("kakaoUserkey",user.getKakaoUserkey());
        }
        return result;
    }

    private Map<String,String> validateDuplicateMemberLocation(KakaoMemberLocation user) {
        Map result = new HashMap<>();
        Optional<KakaoMemberLocation> findUser = kakaoUserRepository.findByLocation(user.getKakaoUserkey());
        result.put("msg","중복회원입니다.");
        result.put("code",0);
        result.put("kakaoUserkey",user.getKakaoUserkey());
        result.put("X",user.getX());
        result.put("Y",user.getY());
        if(!findUser.isPresent()){
            kakaoUserRepository.save(user);
            result.put("msg","위치저장 완료");
            result.put("code",1);
            result.put("kakaoUserkey",user.getKakaoUserkey());
            result.put("X",user.getX());
            result.put("Y",user.getY());
        }else {
            result.put("msg","위치저장 실패.");
            result.put("code",0);
            result.put("kakaoUserkey",user.getKakaoUserkey());
            result.put("X",user.getX());
            result.put("Y",user.getY());
        }
        return result;
    }
}
