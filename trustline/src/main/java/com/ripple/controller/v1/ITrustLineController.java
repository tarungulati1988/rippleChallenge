package com.ripple.controller.v1;

import com.ripple.model.request.Transaction;
import com.ripple.model.response.TransactionResponse;
import org.springframework.http.ResponseEntity;

public interface ITrustLineController {
  ResponseEntity<TransactionResponse> sendTrustline(Transaction transaction);

  ResponseEntity<TransactionResponse> receiveTrustline(Transaction transaction);
}
