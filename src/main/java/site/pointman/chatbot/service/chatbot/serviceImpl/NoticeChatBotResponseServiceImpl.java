package site.pointman.chatbot.service.chatbot.serviceImpl;

import org.springframework.stereotype.Service;
import site.pointman.chatbot.constant.*;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.domain.response.property.common.ListItem;
import site.pointman.chatbot.domain.response.property.components.BasicCard;
import site.pointman.chatbot.domain.response.property.components.ListCard;
import site.pointman.chatbot.service.chatbot.NoticeChatBotResponseService;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeChatBotResponseServiceImpl implements NoticeChatBotResponseService {
    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    @Override
    public ChatBotResponse getNoticesSuccessChatBotResponse(List<Notice> notices) {
        ChatBotResponse chatBotResponse = new ChatBotResponse();

        if (notices.isEmpty()) return chatBotExceptionResponse.createException("등록된 게시글이 없습니다.");

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
    public ChatBotResponse getNoticeSuccessChatBotResponse(Notice notice) {
        try {

            ChatBotResponse chatBotResponse = new ChatBotResponse();

            String title = notice.getTitle();
            String description = notice.getDescriptionTypeOfChatBot();

            if(notice.getType().equals(NoticeType.TEXT_CARD)){
                chatBotResponse.addTextCard(title,description);
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
        }catch (Exception e) {
            return chatBotExceptionResponse.createException();
        }
    }
}
