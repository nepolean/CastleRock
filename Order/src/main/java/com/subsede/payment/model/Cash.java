package com.subsede.payment.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class Cash extends PaymentType {
  @Min(value = 1, message = "Receipt Number should be (greater than) > 1")
  private int receiptNumber;
  @NotBlank(message = "ReceivedBy can't be Blank")
  @NotNull(message = "ReceivedBy can't be NULL")
  @NotEmpty(message = "ReceivedBy can't be Empty")
  private String receivedBy;
  @NotBlank(message = "ReceivedFrom can't be Blank")
  @NotNull(message = "ReceivedFrom can't be NULL")
  @NotEmpty(message = "ReceivedFrom can't be Empty")
  private String receivedFrom;
  @NotNull(message = "ReceiptGeneratedDate can't be NULL")
  private Date receiptGeneratedDate;
  @NotBlank(message = "PaymentRequestId can't be Blank")
  @NotNull(message = "PaymentRequestId can't be NULL")
  @NotEmpty(message = "PaymentRequestId can't be Empty")
  private String paymentRequestId;

  public String getPaymentRequestId() {
    return paymentRequestId;
  }

  public void setPaymentRequestId(String paymentRequestId) {
    this.paymentRequestId = paymentRequestId;
  }

  public int getReceiptNumber() {
    return receiptNumber;
  }

  public void setReceiptNumber(int receiptNumber) {
    this.receiptNumber = receiptNumber;
  }

  public String getReceivedBy() {
    return receivedBy;
  }

  public void setReceivedBy(String receivedBy) {
    this.receivedBy = receivedBy;
  }

  public String getReceivedFrom() {
    return receivedFrom;
  }

  public void setReceivedFrom(String receivedFrom) {
    this.receivedFrom = receivedFrom;
  }

  public Date getReceiptGeneratedDate() {
    return receiptGeneratedDate;
  }

  public void setReceiptGeneratedDate(Date receiptGeneratedDate) {
    this.receiptGeneratedDate = receiptGeneratedDate;
  }

  @Override
  public Enum getType() {
    return PaymentMode.CASH;
  }
}
