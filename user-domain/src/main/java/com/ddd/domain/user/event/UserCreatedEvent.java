package com.ddd.domain.user.event;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class UserCreatedEvent extends ApplicationEvent {
    private Integer userId;
    public UserCreatedEvent(Object source, Integer userId) {
        super(source);
        this.userId = userId;
        log.info("初始化发送事件 UserCreatedEvent userId: {}", userId);
    }
}
