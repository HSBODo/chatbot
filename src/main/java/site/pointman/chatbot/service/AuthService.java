package site.pointman.chatbot.service;

import org.json.simple.JSONObject;
import site.pointman.chatbot.dto.RequestDto;

public interface AuthService {

    JSONObject createAuthForm(RequestDto reqDto) throws Exception;
    JSONObject createAuthInfo(RequestDto reqDto) throws Exception;
    JSONObject createAuthCancel(RequestDto reqDto) throws Exception;
    boolean isAuthMember(String userKey);

    JSONObject createFailMessage(String msg) throws Exception;
}
