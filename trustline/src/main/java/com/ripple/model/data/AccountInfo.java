package com.ripple.model.data;

import com.ripple.model.request.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AccountInfo implements Serializable {
  private static AccountInfo accountInfo = null;
  private BigDecimal balance = new BigDecimal(0);
  private List<Transaction> transactions = new ArrayList<>();
  private String accountId = new String();
  private UUID uuid = UUID.randomUUID();

  private AccountInfo(){
  }

  synchronized public static AccountInfo getInstance() {
    if (accountInfo == null) {
      accountInfo = new AccountInfo();
    }
    return accountInfo;
  }
}
