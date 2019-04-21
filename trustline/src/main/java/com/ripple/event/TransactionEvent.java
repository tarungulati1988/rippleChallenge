package com.ripple.event;

import com.ripple.model.request.Transaction;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class TransactionEvent extends ApplicationEvent {
  private EventTypes eventType;
  private Transaction transaction;
  private UUID sender;

  public TransactionEvent(Object source, EventTypes eventType, Transaction transaction, UUID sender) {
    super(source);
    this.eventType = eventType;
    this.transaction = transaction;
    this.sender = sender;
  }
}
