package com.ripple.event;

import lombok.Getter;

@Getter
public enum EventTypes {

  AMOUNT_SENT(1, "AMOUNT_SENT", "Amount sent"),
  AMOUNT_RECEIVED(2, "AMOUNT_RECEIVED", "Amount received"),
  BALANCE(3, "BALANCE", "Balance"),
  ACCOUNT_CREATED(4, "ACCOUNT_CREATED", "Account creation");

  private Integer id;
  private String value;
  private String displayValue;

  EventTypes(int id, String value, String displayValue) {
    this.id = id;
    this.value = value;
    this.displayValue = displayValue;
  }

  public static EventTypes fromValue(String v) {
    EventTypes[] var1 = values();
    int var2 = var1.length;

    for(int var3 = 0; var3 < var2; ++var3) {
      EventTypes c = var1[var3];
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}
