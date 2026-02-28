package com.ddd.infrastructure.user.listener;

import com.ddd.domain.user.event.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCreatedEventListener {

    /*@Resource
    private MqClient mqClient;*/

    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        log.info("事件消费 UserCreatedEvent userId: {}", event.getUserId());
        log.info("MQ send user.created.success");
        //mqClient.send("user.created.success", event);
    }
}
