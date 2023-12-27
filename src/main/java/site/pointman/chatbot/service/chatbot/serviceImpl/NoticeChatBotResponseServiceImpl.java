package site.pointman.chatbot.service.chatbot.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.HttpResponse;
import site.pointman.chatbot.domain.response.property.common.ListItem;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.ListCard;
import site.pointman.chatbot.domain.response.property.components.TextCard;
import site.pointman.chatbot.service.NoticeService;
import site.pointman.chatbot.service.chatbot.NoticeChatBotResponseService;

import java.util.List;

@Slf4j
@Service
public class NoticeChatBotResponseServiceImpl implements NoticeChatBotResponseService {
    NoticeService noticeService;
    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    public NoticeChatBotResponseServiceImpl(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Override
    public ChatBotResponse getNoticesSuccessChatBotResponse() {
        HttpResponse result = noticeService.getNotices();
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException(result.getMessage());
        List<Notice> notices = (List<Notice>) result.getResult();

        ChatBotResponse chatBotResponse = new ChatBotResponse();

        ListCard listCard = new ListCard();
        listCard.setHeader("공지사항");

        notices.forEach(notice -> {
            String title = notice.getTitle();
            String writer = "작성자: " + notice.getMember().getName();

            ListItem listItem = new ListItem(title);

            listItem.setDescription(writer);
            listItem.setImageUrl(notice.getImageUrl());
            listItem.setActionBlock(BlockId.FIND_NOTICE);
            listItem.setExtra(ButtonParamKey.noticeId,String.valueOf(notice.getId()));

            listCard.setItem(listItem);
        });

        chatBotResponse.addListCard(listCard);
        return chatBotResponse;
    }

    @Override
    public ChatBotResponse getNoticeSuccessChatBotResponse(String noticeId) {
        HttpResponse result = noticeService.getNotice(noticeId);
        if (result.getCode() != ApiResultCode.OK.getValue()) return chatBotExceptionResponse.createException(result.getMessage());
        Notice notice = (Notice) result.getResult();

        ChatBotResponse chatBotResponse = new ChatBotResponse();

        String title = notice.getTitle();
        String description = notice.getDescriptionTypeOfChatBot();

        if(notice.getType().equals(NoticeType.TEXT_CARD)){
            TextCard textCard = new TextCard();

            textCard.setTitle(title);
            textCard.setDescription(description);
            notice.getButtons().forEach(button -> {
                textCard.setButtons(button);
            });

            chatBotResponse.addTextCard(textCard);
            chatBotResponse.addQuickButton(ButtonName.이전으로.name(), ButtonAction.블럭이동,BlockId.FIND_NOTICES.getBlockId());
            return chatBotResponse;
        }

        if(notice.getType().equals(NoticeType.BASIC_CARD)){
            BasicCard basicCard = new BasicCard();

            String imageUrl = notice.getImageUrl();

            basicCard.setThumbnail(imageUrl);
            basicCard.setTitle(title);
            basicCard.setDescription(description);

            notice.getButtons().forEach(button -> {
                basicCard.setButton(button);
            });
            chatBotResponse.addBasicCard(basicCard);
            chatBotResponse.addQuickButton(ButtonName.이전으로.name(),ButtonAction.블럭이동,BlockId.FIND_NOTICES.getBlockId());
            return chatBotResponse;
        }
        return chatBotExceptionResponse.createException("게시글이 존재하지 않습니다. e = type");
    }
}
