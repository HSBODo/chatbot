package site.pointman.chatbot.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pointman.chatbot.constant.NoticeType;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.service.NoticeService;

@Slf4j
@Controller
@RequestMapping(value = "admin/notice", consumes = MediaType.APPLICATION_JSON_VALUE)
public class NoticeController {
    NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.POST)
    public Response add(@RequestBody Notice notice){
        return noticeService.addNotice(notice);
    }
}
