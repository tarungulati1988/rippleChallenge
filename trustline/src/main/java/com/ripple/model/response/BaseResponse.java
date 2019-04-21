package com.ripple.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseResponse<T> implements Serializable {
  @JsonProperty("success")
  private Boolean success;

  /**
   * To be used to define custom error codes and error response for the future.
   * T to be replaced with the error model type
   */
  private List<T> errors;

  BaseResponse(Boolean success) {
    if (!success.booleanValue()) {
      throw new RuntimeException("failure!");
    }
    this.success = success;
  }
}
