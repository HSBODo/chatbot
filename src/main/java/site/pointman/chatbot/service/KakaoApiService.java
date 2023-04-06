package site.pointman.chatbot.service;

import site.pointman.chatbot.domain.KakaoUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface KakaoApiService {


    String join(KakaoUser user);
}
