package com.real.order.model;

public class OnlinePayment extends PaymentType {
  @Override
  public Enum getType() {
    return PaymentMode.ONLINE;
  }
}
