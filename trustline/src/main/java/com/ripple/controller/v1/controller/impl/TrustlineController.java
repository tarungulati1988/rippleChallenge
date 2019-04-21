package com.ripple.controller.v1.controller.impl;

import com.ripple.controller.v1.ITrustLineController;
import com.ripple.model.data.AccountInfo;
import com.ripple.model.request.Transaction;
import com.ripple.service.v1.impl.TrustLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/v1")
public class TrustlineController implements ITrustLineController {

  @Autowired
  private TrustLineService service;

  @Override
  @GetMapping(value = "/trustline/{account}", produces = "application/json")
  @ResponseBody
  public ResponseEntity<String> getTrustLine(@PathVariable("account") String accountId) {
    AccountInfo accountInfo = AccountInfo.getInstance();
    Transaction transaction = new Transaction();
    transaction.setAmount(new BigDecimal(1234));
    transaction.setSource(accountInfo.getUuid().toString());
    return ResponseEntity.ok().body(service.sendMoney(transaction));
  }

  @Override
  @PostMapping(value = "/trustline", produces = "application/json", consumes = "application/json")
  @ResponseBody
  public ResponseEntity<String> updateOrCreateTrustline(@RequestBody Transaction transaction) {
    return ResponseEntity.ok().body(service.sendMoney(transaction));
  }


}
