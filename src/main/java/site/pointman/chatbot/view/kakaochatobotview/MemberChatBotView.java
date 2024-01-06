package site.pointman.chatbot.view.kakaochatobotview;

import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;
import site.pointman.chatbot.domain.member.dto.MemberProfileDto;

public interface MemberChatBotView {
    ChatBotResponse joinMemberResultPage();
    ChatBotResponse myProfilePage(MemberProfileDto memberProfileDto);
    ChatBotResponse updateMemberPhoneNumberResultPage();
    ChatBotResponse withdrawalMemberResultPage();
    ChatBotResponse myPage();
    ChatBotResponse mySalesCategoryListPage();
    ChatBotResponse updateMemberProfileImageResultPage();
}
