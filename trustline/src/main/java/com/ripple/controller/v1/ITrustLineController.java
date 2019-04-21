package com.ripple.controller.v1;

import com.ripple.model.request.Transaction;
import org.springframework.http.ResponseEntity;

public interface ITrustLineController {
  ResponseEntity<String> sendTrustline(Transaction transaction);
  ResponseEntity<String> receiveTrustline(Transaction transaction);
}
