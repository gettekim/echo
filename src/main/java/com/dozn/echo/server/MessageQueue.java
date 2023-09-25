package com.dozn.echo.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
@Slf4j
@Component
public class MessageQueue {
    public final int QUEUE_SIZE = 10;
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(QUEUE_SIZE);

    public void addMessage(String message) throws InterruptedException {
        queue.offer(message);
    }

    public String delMessage() throws InterruptedException {
        // 큐가 꽉 찼을 경우만 첫 번째 요소 제거
        if (queue.size() < QUEUE_SIZE) {
            return "";
        }
        return queue.poll();
    }

    //반환할 메세지 생성
    public String getResultMessage(String message, String delMessage){
        StringBuilder sb = new StringBuilder();
        sb.append(message);

        if(delMessage != "") {
            sb.append(" 삭제된 메세지: ");
            sb.append(delMessage);
        }
        return sb.toString();
    }

    public String proceed(String message) throws  InterruptedException{
        // 메세지 삭제
        String resultMessage = delMessage();
        // 메세지 추가
        addMessage(message);

        return getResultMessage(message,resultMessage);
    }

    public int size(){
        return queue.size();
    }

    public void getQueueStatus() {
        log.info("큐 : {}",queue);
        log.info("큐 개수 : {}", queue.size());
    }

}
