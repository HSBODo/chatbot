package site.pointman.chatbot.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pointman.chatbot.constant.ResultCode;
import site.pointman.chatbot.domain.notice.Notice;
import site.pointman.chatbot.domain.response.Response;
import site.pointman.chatbot.domain.notice.dto.NoticeDto;
import site.pointman.chatbot.domain.notice.service.NoticeService;

import java.nio.charset.Charset;
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
    public ResponseEntity add(@RequestBody NoticeDto noticeDto){
        HttpHeaders headers = getHeaders();

        Response response = noticeService.addNotice(noticeDto);

        return new ResponseEntity<>(response,headers, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity getNotices(){
        HttpHeaders headers = getHeaders();
        List<NoticeDto> noticeDtoList = new ArrayList<>();

        List<Notice> noticeAll = noticeService.getNoticeAll();
        if (noticeAll.isEmpty()) return new ResponseEntity<>(new Response(ResultCode.EXCEPTION,"게시글이 존재하지 않습니다"),headers,HttpStatus.OK);

        noticeAll.forEach(notice -> {
            noticeDtoList.add(notice.toDto());

        });

        return new ResponseEntity<>(noticeDtoList,headers,HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.GET)
    public ResponseEntity getNotice(@PathVariable Long noticeId){
        HttpHeaders headers = getHeaders();

        Optional<Notice> mayBeNotice = noticeService.getNotice(noticeId);

        if (mayBeNotice.isEmpty()) return new ResponseEntity<>(new Response(ResultCode.FAIL,"게시글이 존재하지 않습니다."),headers,HttpStatus.OK);

        NoticeDto noticeDto = mayBeNotice.get().toDto();

        return new ResponseEntity<>(noticeDto,headers,HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.DELETE)
    public ResponseEntity deleteNotice(@PathVariable Long noticeId){
        HttpHeaders headers = getHeaders();

        Response response = noticeService.removeNotice(noticeId);

        return new ResponseEntity<>(response,headers,HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "{noticeId}",method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateNotice(@PathVariable Long noticeId, @RequestBody NoticeDto noticeDto){
        HttpHeaders headers = getHeaders();

        Response response = noticeService.updateNotice(noticeId, noticeDto);

        return new ResponseEntity<>(response,headers,HttpStatus.OK);
    }


    private HttpHeaders getHeaders(){
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return headers;
    }
}
