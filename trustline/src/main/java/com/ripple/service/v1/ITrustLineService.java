package com.ripple.service.v1;

import com.ripple.model.request.Transaction;
import com.ripple.model.response.TransactionResponse;
import org.springframework.http.ResponseEntity;

public interface ITrustLineService {
  ResponseEntity<TransactionResponse> sendMoney(Transaction transaction);
  ResponseEntity<TransactionResponse> receiveMoney(Transaction transaction);
  void myTrustLine();
  void broadcastAccountCreation();
  ResponseEntity<String> sendToRecepient(Transaction transaction);
}
