package com.ripple.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction implements Serializable {
  /**
   * Amount to be transferred over
   */
  @JsonProperty("amount")
  private BigDecimal amount;

  /**
   * Destination account id
   */
  @JsonProperty("destination")
  private String destinationAccount;

  /**
   * Source account id
   */
  @JsonProperty("source")
  private String source;

  /**
   * Message
   */
  @JsonProperty("message")
  private String message;
}
