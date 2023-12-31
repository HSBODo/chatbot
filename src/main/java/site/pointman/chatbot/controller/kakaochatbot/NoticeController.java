package site.pointman.chatbot.controller.kakaochatbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.view.kakaochatobotview.NoticeChatBotView;


@Slf4j
@Controller
@RequestMapping(value = "/kakaochatbot/notice")
public class NoticeController {

    NoticeChatBotView noticeChatBotResponseService;

    public NoticeController(NoticeChatBotView noticeChatBotResponseService) {
        this.noticeChatBotResponseService = noticeChatBotResponseService;
    }

    @ResponseBody
    @PostMapping(value = "GET" , headers = {"Accept=application/json; UTF-8"})
    public Object getNotices(@RequestBody ChatBotRequest chatBotRequest){
        int pageNumber = chatBotRequest.getPageNumber();

        return noticeChatBotResponseService.noticeListPage(pageNumber);
    }

    @ResponseBody
    @PostMapping(value = "GET/byNoticeId" , headers = {"Accept=application/json; UTF-8"})
    public Object getNotice(@RequestBody ChatBotRequest chatBotRequest){
        String noticeId = chatBotRequest.getNoticeId();

        return noticeChatBotResponseService.noticeDetailPage(noticeId);
    }
}
