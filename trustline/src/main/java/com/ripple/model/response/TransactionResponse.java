package com.ripple.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ripple.model.request.Transaction;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse extends BaseResponse {

  private List<Transaction> trustLines;
  private String message;

  public TransactionResponse(@JsonProperty("success") Boolean success) {
    super(success);
  }
}
