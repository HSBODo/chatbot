package site.pointman.chatbot.view.kakaochatobotview;

import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;

public interface MemberChatBotView {
    ChatBotResponse joinMemberResultPage(String userKey, String name, String phoneNumber);
    ChatBotResponse myProfilePage(String userKey);
    ChatBotResponse updateMemberPhoneNumberResultPage(String userKey, String updatePhoneNumber);
    ChatBotResponse withdrawalMemberResultPage(String userKey);
    ChatBotResponse myPage();
    ChatBotResponse mySalesCategoryListPage();
    ChatBotResponse updateMemberProfileImageResultPage(String userKey, String profileImageUrl);
}
