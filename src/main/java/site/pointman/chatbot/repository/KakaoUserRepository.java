package site.pointman.chatbot.repository;

import site.pointman.chatbot.domain.KakaoUser;

import java.util.Optional;

public interface KakaoUserRepository {
    KakaoUser save (KakaoUser user);
    Optional<KakaoUser> findByUserkey(String  kakaoUserkey);
}
