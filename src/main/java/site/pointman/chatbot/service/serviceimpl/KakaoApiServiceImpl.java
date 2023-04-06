package site.pointman.chatbot.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.KakaoUser;
import site.pointman.chatbot.repository.KakaoUserRepository;
import site.pointman.chatbot.service.KakaoApiService;
import site.pointman.chatbot.service.WeatherApiService;

import java.util.*;

@Slf4j
@Service
public class KakaoApiServiceImpl implements KakaoApiService {

    private  final KakaoUserRepository kakaoUserRepository;
    @Autowired
    public KakaoApiServiceImpl(KakaoUserRepository kakaoUserRepository) {
        this.kakaoUserRepository = kakaoUserRepository;
    }

    @Override
    public String join(KakaoUser user) {
       //회원 검증
//        kakaoUserRepository.save(user);
        return  validateDuplicateMember(user);
    }

    private String validateDuplicateMember(KakaoUser user) {
        Optional<KakaoUser> findUser = kakaoUserRepository.findByUserkey(user.getKakaoUserkey());
        String result ="중복회원";
        if(!findUser.isPresent()){
            kakaoUserRepository.save(user);
            result=user.getKakaoUserkey();
        }
        return result;
    }
}
