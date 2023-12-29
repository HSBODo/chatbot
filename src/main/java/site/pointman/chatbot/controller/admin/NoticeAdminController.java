package site.pointman.chatbot.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.constant.ResultCode;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.dto.notice.NoticeDto;
import site.pointman.chatbot.service.NoticeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping(value = "admin/notice")
public class NoticeAdminController {
    NoticeService noticeService;

    public NoticeAdminController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Notice add(@RequestBody Notice notice){
        return noticeService.addNotice(notice);
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.GET)
    public List<NoticeDto> getNotices(){
        List<NoticeDto> noticeDtoList = new ArrayList<>();

        List<Notice> noticeAll = noticeService.getNoticeAll();
        noticeAll.forEach(notice -> {
            noticeDtoList.add(notice.toDto());

        });

        return noticeDtoList;
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.GET)
    public Object getNotice(@PathVariable Long noticeId){
        Optional<Notice> mayBeNotice = noticeService.getNotice(noticeId);

        if (mayBeNotice.isEmpty()) return new Response(ResultCode.FAIL,"게시글이 존재하지 않습니다.");

        NoticeDto noticeDto = mayBeNotice.get().toDto();

        return noticeDto;
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.DELETE)
    public Response deleteNotice(@PathVariable Long noticeId){
        return noticeService.removeNotice(noticeId);
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response updateNotice(@PathVariable Long noticeId, @RequestBody NoticeDto noticeDto){
        return noticeService.updateNotice(noticeId,noticeDto);
    }
}
