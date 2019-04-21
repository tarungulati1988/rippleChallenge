package com.ripple.controller.v1;

import com.ripple.model.request.Transaction;
import org.springframework.http.ResponseEntity;

public interface ITrustLineController {
  ResponseEntity<String> getTrustLine(String accountId);
  ResponseEntity<String> updateOrCreateTrustline(Transaction transaction);
}
