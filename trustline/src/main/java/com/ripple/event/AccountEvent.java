package com.ripple.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
@Setter
public class AccountEvent extends ApplicationEvent {
  private EventTypes eventType;
  private UUID account;

  public AccountEvent(Object source, EventTypes eventType, UUID account) {
    super(source);
    this.eventType = eventType;
    this.account = account;
  }
}
