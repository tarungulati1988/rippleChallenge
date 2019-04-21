package com.ripple.event;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

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
