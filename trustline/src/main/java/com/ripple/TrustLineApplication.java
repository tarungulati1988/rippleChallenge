package com.ripple;

import com.ripple.model.data.AccountInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrustLineApplication {
  public static void main(String[] args) {
    SpringApplication.run(TrustLineApplication.class, args);
    AccountInfo accountInfo = AccountInfo.getInstance();
    System.out.println("User account details");
    System.out.println("UUID: " + accountInfo.getUuid().toString());
    System.out.println("Balance: " + accountInfo.getBalance());
  }
}
