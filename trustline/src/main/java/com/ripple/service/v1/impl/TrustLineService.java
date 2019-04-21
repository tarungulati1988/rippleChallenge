package com.ripple.service.v1.impl;

import static com.ripple.event.EventTypes.ACCOUNT_CREATED;
import static com.ripple.event.EventTypes.AMOUNT_RECEIVED;
import static com.ripple.event.EventTypes.AMOUNT_SENT;
import static com.ripple.event.EventTypes.BALANCE;

import com.ripple.event.AccountEvent;
import com.ripple.event.TransactionEvent;
import com.ripple.model.data.AccountInfo;
import com.ripple.model.data.TrustLine;
import com.ripple.model.request.Transaction;
import com.ripple.service.v1.ITrustLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

@Service
public class TrustLineService implements ITrustLineService, ApplicationEventPublisherAware {

  @Autowired
  private ApplicationEventPublisher publisher;

  public String sendMoney(Transaction transaction) {
    AccountInfo accountInfo = AccountInfo.getInstance();
    System.out.println(transaction.getSource() + " is paying " + transaction.getDestinationAccount() + " : " + transaction.getAmount());

    this.publisher.publishEvent(new TransactionEvent(this, AMOUNT_SENT, transaction, accountInfo.getUuid()));

    System.out.println("Sent");

    accountInfo.setBalance(accountInfo.getBalance().subtract(transaction.getAmount()));
    accountInfo.getTransactions().add(transaction);

    this.publisher.publishEvent(new TransactionEvent(this, BALANCE, transaction, accountInfo.getUuid()));
    return "success";
  }


  public String receiveMoney(Transaction transaction) {
    // TODO validate if correct account receiving money
    AccountInfo accountInfo = AccountInfo.getInstance();
    System.out.println(transaction.getSource() + " paid " + transaction.getDestinationAccount() + " : " + transaction.getAmount());
    this.publisher.publishEvent(new TransactionEvent(this, AMOUNT_RECEIVED, transaction, accountInfo.getUuid()));
    System.out.println("Received");
    accountInfo.setBalance(accountInfo.getBalance().add(transaction.getAmount()));
    accountInfo.getTransactions().add(transaction);
    this.publisher.publishEvent(new TransactionEvent(this, BALANCE, transaction, accountInfo.getUuid()));
    return "success";
  }


  public void myTrustLine(Transaction transaction) {
    AccountInfo accountInfo = AccountInfo.getInstance();
    System.out.println("Trustline balance for: " + accountInfo.getUuid() + " is : " + accountInfo.getBalance());
  }


  public void broadcastAccountCreation() {
    TrustLine trustLine = TrustLine.getInstance();
    AccountInfo accountInfo = AccountInfo.getInstance();
    trustLine.addAccount(accountInfo.getUuid());
    this.publisher
        .publishEvent(new AccountEvent(this, ACCOUNT_CREATED, accountInfo.getUuid()));
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.publisher = applicationEventPublisher;
  }

}