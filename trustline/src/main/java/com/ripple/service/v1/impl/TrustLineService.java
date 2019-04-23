package com.ripple.service.v1.impl;

import static com.ripple.event.EventTypes.*;
import com.ripple.event.AccountEvent;
import com.ripple.event.TransactionEvent;
import com.ripple.model.data.AccountInfo;
import com.ripple.model.data.TrustLine;
import com.ripple.model.request.Transaction;
import com.ripple.model.response.TransactionResponse;
import com.ripple.service.v1.ITrustLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

  /**
   * Given a transaction, updated personal ledger.
   * @param transaction - contains amount, source and destination details for the incoming transaction
   * @return - Response entity containing the trustline
   *           and also whether the transfer was successfull or not
   */
  @Override
  public ResponseEntity<TransactionResponse> sendMoney(Transaction transaction) {
    AccountInfo accountInfo = AccountInfo.getInstance();
    System.out.println(transaction.getSource()
                       + " is paying "
                       + transaction.getDestinationAccount()
                       + " : "
                       + transaction.getAmount());
    // Emit a send amount event to notify that user/source is sending an amount out
    this
        .publisher
        .publishEvent(new TransactionEvent(this, AMOUNT_SENT, transaction, accountInfo
            .getUuid()));
    // Update personal ledger with latest transaction and also store current transaction into the history
    accountInfo.setBalance(accountInfo.getBalance().subtract(transaction.getAmount()));
    accountInfo.getTransactions().add(transaction);

    // Emit and event to notify the updated balance for the personal ledger
    this
        .publisher
        .publishEvent(new TransactionEvent(this, BALANCE, transaction, accountInfo
            .getUuid()));
    // async send the amount over to the destination
    return buildResponse(sendToRecepient(transaction), transaction);
  }

  /**
   * Given a response entity and a transaction build out the response model to be returned to the client.
   * @param responseEntity - containing the response status and the response from the service/api call
   * @param transaction - current transaction details
   * @return - updated response entity containing the response object
   */
  private ResponseEntity<TransactionResponse> buildResponse(ResponseEntity<String> responseEntity,
                                                            Transaction transaction) {
    TransactionResponse transactionResponse;
    List<Transaction> trustlines = new ArrayList<>();
    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      transactionResponse = new TransactionResponse(true);
    } else {
      transactionResponse = new TransactionResponse(false);
    }
    trustlines.add(transaction);
    transactionResponse.setTrustLines(trustlines);
    return ResponseEntity.status(responseEntity.getStatusCode()).body(transactionResponse);
  }

  /**
   * Given a transaction accept the amount into the personal ledger and emit notification events.
   * @param transaction - incoming transaction details
   * @return - Response contianing whether the update was successful or a failure
   */
  @Override
  public ResponseEntity<TransactionResponse> receiveMoney(Transaction transaction) {
    // TODO validate if correct account receiving money
    AccountInfo accountInfo = AccountInfo.getInstance();
    System.out.println(transaction.getSource()
                       + " paid "
                       + transaction.getDestinationAccount()
                       + " : "
                       + transaction.getAmount());
    // emit an event to notify that you have received the amount
    this
        .publisher
        .publishEvent(new TransactionEvent(this, AMOUNT_RECEIVED, transaction, accountInfo
            .getUuid()));

    // update personal ledger
    accountInfo.setBalance(accountInfo.getBalance().add(transaction.getAmount()));
    accountInfo.getTransactions().add(transaction);
    // emit an event to notify others on the network of the updated balance
    this
        .publisher
        .publishEvent(new TransactionEvent(this, BALANCE, transaction, accountInfo
            .getUuid()));
    // Build the response for the client
    TransactionResponse transactionResponse = new TransactionResponse(true);
    List<Transaction> trustLines = new ArrayList<>();
    trustLines.add(transaction);
    transactionResponse.setTrustLines(trustLines);
    transactionResponse.setMessage("success");
    return ResponseEntity.ok().body(transactionResponse);
  }

  /**
   * Echo out personal trustline info.
   */
  @Override
  public void myTrustLine() {
    AccountInfo accountInfo = AccountInfo.getInstance();
    System.out.println("Trustline balance for: "
                       + accountInfo.getUuid()
                       + " is : "
                       + accountInfo.getBalance());
  }


  /**
   * Broadcast to others on the network if you are a newly created account.
   */
  @Override
  public void broadcastAccountCreation() {
    TrustLine trustLine = TrustLine.getInstance();
    AccountInfo accountInfo = AccountInfo.getInstance();
    trustLine.addAccount(accountInfo.getUuid());
    this.publisher
        .publishEvent(new AccountEvent(this, ACCOUNT_CREATED, accountInfo.getUuid()));
  }

  /**
   * API call to send money to a client
   * @param transaction
   * @return
   */
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
