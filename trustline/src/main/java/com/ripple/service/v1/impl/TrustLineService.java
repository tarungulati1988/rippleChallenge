package com.ripple.service.v1.impl;

import static com.ripple.event.EventTypes.*;
import com.ripple.event.AccountEvent;
import com.ripple.event.TransactionEvent;
import com.ripple.model.data.AccountInfo;
import com.ripple.model.data.TrustLine;
import com.ripple.model.request.Transaction;
import com.ripple.service.v1.ITrustLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TrustLineService implements ITrustLineService, ApplicationEventPublisherAware {

  @Value("${trustline.service.url}")
  String serviceURL;
  @Value("${trustline.service.endpoint}")
  String endpoint;
  @Value("${client.port}")
  String port;
  @Autowired
  private ApplicationEventPublisher publisher;

  @Override
  public ResponseEntity<String> sendMoney(Transaction transaction) {
    AccountInfo accountInfo = AccountInfo.getInstance();
    System.out.println(transaction.getSource() + " is paying " + transaction.getDestinationAccount() + " : " + transaction.getAmount());

    this.publisher.publishEvent(new TransactionEvent(this, AMOUNT_SENT, transaction, accountInfo.getUuid()));

    accountInfo.setBalance(accountInfo.getBalance().subtract(transaction.getAmount()));
    accountInfo.getTransactions().add(transaction);


    this.publisher.publishEvent(new TransactionEvent(this, BALANCE, transaction, accountInfo.getUuid()));
    return sendToRecepient(transaction);
  }

  @Override
  public ResponseEntity<String> receiveMoney(Transaction transaction) {
    // TODO validate if correct account receiving money
    AccountInfo accountInfo = AccountInfo.getInstance();
    System.out.println(transaction.getSource() + " paid " + transaction.getDestinationAccount() + " : " + transaction.getAmount());
    this.publisher.publishEvent(new TransactionEvent(this, AMOUNT_RECEIVED, transaction, accountInfo.getUuid()));

    accountInfo.setBalance(accountInfo.getBalance().add(transaction.getAmount()));
    accountInfo.getTransactions().add(transaction);
    this.publisher.publishEvent(new TransactionEvent(this, BALANCE, transaction, accountInfo.getUuid()));
    return ResponseEntity.ok().body("success");
  }

  @Override
  public void myTrustLine() {
    AccountInfo accountInfo = AccountInfo.getInstance();
    System.out.println("Trustline balance for: " + accountInfo.getUuid() + " is : " + accountInfo.getBalance());
  }


  @Override
  public void broadcastAccountCreation() {
    TrustLine trustLine = TrustLine.getInstance();
    AccountInfo accountInfo = AccountInfo.getInstance();
    trustLine.addAccount(accountInfo.getUuid());
    this.publisher
        .publishEvent(new AccountEvent(this, ACCOUNT_CREATED, accountInfo.getUuid()));
  }

  @Async
  @Override
  public ResponseEntity<String> sendToRecepient(Transaction transaction) {
    String uri = endpoint + ":" + port + serviceURL + "/receive";
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.postForEntity(uri, transaction, String.class);
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.publisher = applicationEventPublisher;
  }

}
