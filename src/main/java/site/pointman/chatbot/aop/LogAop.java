package site.pointman.chatbot.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import site.pointman.chatbot.domain.log.Log;
import site.pointman.chatbot.domain.request.ChatBotRequest;
import site.pointman.chatbot.domain.response.ChatBotResponse;
import site.pointman.chatbot.service.LogService;

@Aspect
@Component
@Slf4j
public class LogAop {


    LogService logService;

    public LogAop(LogService logService) {
        this.logService = logService;
    }

    @Pointcut("execution(* site.pointman.chatbot.controller.kakaochatbot.*.*(..)) && !@annotation(site.pointman.chatbot.annotation.SkipLogging)") //Pointcut
    public void chatBotControllerPointcut() {
    }
//    @Before("controllerPointcut()")
//    public void before(JoinPoint joinPoint) throws Exception {
//
//        //실행되는 함수 이름을 가져오고 출력
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        System.out.println(method.getName() + "메서드 실행");
//
//        //메서드에 들어가는 매개변수 배열을 읽어옴
//        Object[] args = joinPoint.getArgs();
//
//        //매개변수 배열의 종류와 값을 출력
//        for(Object obj : args) {
//            System.out.println("type : "+obj.getClass().getSimpleName());
//            ChatBotRequest chatBotRequest = (ChatBotRequest) obj;
//            logService.insert(chatBotRequest);
//        }
//    }

//    @AfterReturning(value = "controllerPointcut()",returning = "chatBotResponse")
//    public void after(JoinPoint joinPoint,ChatBotResponse chatBotResponse) throws Exception {
//
//        //실행되는 함수 이름을 가져오고 출력
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        System.out.println(method.getName() + "메서드 실행");
//
//        //logService.insert(chatBotResponse);
//    }


    @Around("chatBotControllerPointcut()")
    public ChatBotResponse log(ProceedingJoinPoint joinPoint) throws Throwable {
        Log logEntity = new Log();

        String controllerName = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        log.info("==== AOP JOINPOINT ====");
        log.info("controllerName={}",controllerName);
        log.info("methodName={}",methodName);
        //메서드에 들어가는 매개변수 배열을 읽어옴
        Object[] args = joinPoint.getArgs();

        //매개변수 배열의 종류와 값을 출력
        for(Object obj : args) {
            System.out.println("type : "+obj.getClass().getSimpleName());
            ChatBotRequest chatBotRequest = (ChatBotRequest) obj;
            logEntity = logService.insert(chatBotRequest);
        }

        ChatBotResponse chatBotResponse = (ChatBotResponse) joinPoint.proceed();
        log.info("==== AOP ENDPOINT ====");
        logService.insert(logEntity,chatBotResponse);
        return chatBotResponse;
    }
}
