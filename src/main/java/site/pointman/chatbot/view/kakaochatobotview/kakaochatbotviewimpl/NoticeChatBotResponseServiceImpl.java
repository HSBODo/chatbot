package site.pointman.chatbot.view.kakaochatobotview.kakaochatbotviewimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.chatbot.constatnt.block.BlockId;
import site.pointman.chatbot.domain.chatbot.constatnt.button.ButtonAction;
import site.pointman.chatbot.domain.chatbot.constatnt.button.ButtonName;
import site.pointman.chatbot.domain.chatbot.constatnt.button.ButtonParamKey;
import site.pointman.chatbot.domain.notice.constant.NoticeType;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.chatbot.response.ChatBotExceptionResponse;
import site.pointman.chatbot.domain.chatbot.response.ChatBotResponse;
import site.pointman.chatbot.domain.chatbot.response.property.common.ListItem;
import site.pointman.chatbot.domain.chatbot.response.property.components.BasicCard;
import site.pointman.chatbot.domain.chatbot.response.property.components.ListCard;
import site.pointman.chatbot.domain.chatbot.response.property.components.TextCard;
import site.pointman.chatbot.domain.notice.service.NoticeService;
import site.pointman.chatbot.view.kakaochatobotview.NoticeChatBotView;

import java.util.Optional;

@Slf4j
@Service
public class NoticeChatBotResponseServiceImpl implements NoticeChatBotView {
    NoticeService noticeService;
    ChatBotExceptionResponse chatBotExceptionResponse = new ChatBotExceptionResponse();

    public NoticeChatBotResponseServiceImpl(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Override
    public ChatBotResponse noticeListPage(int pageNumber) {
        Page<Notice> mainNotices = noticeService.getDefaultNotices(pageNumber);


        if (mainNotices.getContent().isEmpty()) return chatBotExceptionResponse.createException("게시글이 존재하지 않습니다.");


        ChatBotResponse chatBotResponse = new ChatBotResponse();

        ListCard listCard = new ListCard();
        listCard.setHeader("공지사항");

        mainNotices.getContent().forEach(notice -> {
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
        chatBotResponse.addQuickButton(ButtonName.메인메뉴.name(), ButtonAction.블럭이동,BlockId.MAIN.getBlockId());
        if (mainNotices.hasNext()){
            chatBotResponse.addQuickButton(ButtonName.더보기.name(), ButtonAction.블럭이동,BlockId.FIND_NOTICES.getBlockId(),ButtonParamKey.pageNumber,String.valueOf(++pageNumber));
        }

        return chatBotResponse;
    }

    @Override
    public ChatBotResponse noticeDetailPage(String noticeId) {
        Optional<Notice> mayBeNotice = noticeService.getNotice(Long.parseLong(noticeId));

        if (mayBeNotice.isEmpty()) return chatBotExceptionResponse.createException("게시글이 존재하지 않습니다.");

        Notice notice = mayBeNotice.get();

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
