package com.subsede.payment.model;

public class OnlinePayment extends PaymentType {
  @Override
  public Enum getType() {
    return PaymentMode.ONLINE;
  }
}
