package com.dozn.echo.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class MessageQueueTest {

    private final MessageQueue messageQueue = new MessageQueue();

    @Test
    public void 최대10개까지_입력되는지확인() throws InterruptedException {
        for(int x = 0; x < 11; x ++){
            messageQueue.addMessage("테스트 메세지"+x);
        }
        log.info(String.valueOf(messageQueue.size()));
        assertThat(messageQueue.size()).isEqualTo(10);
    }

    @Test
    public void 큐사이즈가10개미만일경우_반환메세지확인() throws InterruptedException {
        for(int a= 0; a<3; a++){
            messageQueue.addMessage("테스트 메세지"+a);
        }
        String message = "테스트 메세지3";
        String result = messageQueue.proceed(message);
        assertThat(result).isEqualTo(message);
    }

    @Test
    public void 큐사이즈가10개이상일경우_반환메세지확인() throws InterruptedException {
        for(int x = 0; x < 11; x++){
            messageQueue.addMessage("테스트 메세지"+x);
        }
        String message = "테스트 메세지11";
        //첫번째 값을 삭제하고 최근 10개 메세지를 저장
        String resultMessage = messageQueue.getResultMessage(message,"테스트 메세지0");
        String result = messageQueue.proceed(message);
        assertThat(result).isEqualTo(resultMessage);
    }

}
