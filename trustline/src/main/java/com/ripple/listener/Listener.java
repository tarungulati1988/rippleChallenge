package com.ripple.listener;

import com.ripple.event.EventTypes;
import com.ripple.event.TransactionEvent;
import com.ripple.model.data.AccountInfo;
import com.ripple.service.v1.impl.TrustLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class Listener implements ApplicationListener<TransactionEvent> {

  @Autowired
  private TrustLineService service;

  @EventListener(ApplicationReadyEvent.class)
  public void doSomethingAfterStartup() {
    service.broadcastAccountCreation();
  }

  @Override
  public void onApplicationEvent(TransactionEvent transactionEvent) {
    AccountInfo accountInfo = AccountInfo.getInstance();
    // TODO:: cleanup design below with factory of visitors

//    if (transactionEvent.getEventType() == EventTypes.AMOUNT_SENT
//        && accountInfo.getUuid().compareTo(transactionEvent.getSender()) = 0) {
//      System.out.println("SEND event");
//    } else
      if (transactionEvent.getEventType() == EventTypes.AMOUNT_SENT
               && accountInfo.getUuid()
                      .compareTo(UUID
                          .fromString(transactionEvent.getTransaction().getDestinationAccount())) == 0) {
      System.out.println("RECEIVE event");
      service.receiveMoney(transactionEvent.getTransaction());
    } else if (transactionEvent.getEventType() == EventTypes.BALANCE
               && accountInfo.getUuid().compareTo(transactionEvent.getSender()) == 0) {
      System.out.println("BALANCE event");
      service.myTrustLine(transactionEvent.getTransaction());
    }
  }
}