package com.ripple.controller.v1.impl;

import com.ripple.controller.v1.ITrustLineController;
import com.ripple.model.request.Transaction;
import com.ripple.model.response.TransactionResponse;
import com.ripple.service.v1.impl.TrustLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1/trustline")
public class TrustlineController implements ITrustLineController {

  @Autowired
  private TrustLineService service;

  @Override
  @PostMapping(value = "/send", produces = "application/json", consumes = "application/json")
  @ResponseBody
  public ResponseEntity<TransactionResponse> sendTrustline(@RequestBody Transaction transaction) {
    return service.sendMoney(transaction);
  }

  @Override
  @PostMapping(value = "/receive", produces = "application/json", consumes = "application/json")
  @ResponseBody
  public ResponseEntity<TransactionResponse> receiveTrustline(@RequestBody Transaction transaction) {
    return service.receiveMoney(transaction);
  }


}
