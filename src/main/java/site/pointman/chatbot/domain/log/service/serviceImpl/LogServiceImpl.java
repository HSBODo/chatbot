package site.pointman.chatbot.domain.log.service.serviceImpl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.pointman.chatbot.domain.log.ChatBotLog;
import site.pointman.chatbot.domain.chatbot.request.ChatBotRequest;
import site.pointman.chatbot.repository.LogRepository;
import site.pointman.chatbot.domain.log.service.LogService;

@Service
@Slf4j
public class LogServiceImpl implements LogService {
    LogRepository logRepository;
    Gson gson = new Gson();


    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public ChatBotLog insertChatBotRequestLog(ChatBotRequest chatBotRequest) throws Exception {

        String userKey = chatBotRequest.getUserKey();
        String blockId = chatBotRequest.getIntent().getId();
        String blockName = chatBotRequest.getIntent().getName();
        String skillId = chatBotRequest.getAction().getId();
        String skillName = chatBotRequest.getAction().getName();
        String requestJson = gson.toJson(chatBotRequest);

        ChatBotLog log = ChatBotLog.builder()
                .userKey(userKey)
                .blockId(blockId)
                .blockName(blockName)
                .skillId(skillId)
                .skillName(skillName)
                .requestJson(requestJson)
                .build();

        logRepository.save(log);
        return log;
    }

    @Override
    public void insertChatBotResponseLog(ChatBotLog log, Object chatBotResponse) {
        String responseJson = gson.toJson(chatBotResponse);
        log.setResponseJson(responseJson);

        logRepository.save(log);
    }
}
