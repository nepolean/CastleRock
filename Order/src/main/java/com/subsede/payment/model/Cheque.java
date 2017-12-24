package com.subsede.payment.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class Cheque extends PaymentType {
  @Min(value = 1, message = "Cheque Number should be (greater than) > 1")
  private int chequeNumber;
  @NotBlank(message = "Payee can't be Blank")
  @NotNull(message = "Payee can't be NULL")
  @NotEmpty(message = "Payee can't be Empty")
  private String payee;
  @NotNull(message = "ChequeIssuedDate can't be NULL")
  private Date chequeIssuedDate;
  @NotNull(message = "ChequeExpiryDate can't be NULL")
  private Date chequeExpiryDate;
  @NotBlank(message = "BankName can't be Blank")
  @NotNull(message = "BankName can't be NULL")
  @NotEmpty(message = "BankName can't be Empty")
  private String bankName;
  @NotBlank(message = "PaymentRequestId can't be Blank")
  @NotNull(message = "PaymentRequestId can't be NULL")
  @NotEmpty(message = "PaymentRequestId can't be Empty")
  private String paymentRequestId;
  private int accountNumber;

  public String getPaymentRequestId() {
    return paymentRequestId;
  }

  public void setPaymentRequestId(String paymentRequestId) {
    this.paymentRequestId = paymentRequestId;
  }


  public int getChequeNumber() {
    return chequeNumber;
  }

  public void setChequeNumber(int chequeNumber) {
    this.chequeNumber = chequeNumber;
  }

  public String getPayee() {
    return payee;
  }

  public void setPayee(String payee) {
    this.payee = payee;
  }

  public Date getChequeIssuedDate() {
    return chequeIssuedDate;
  }

  public void setChequeIssuedDate(Date chequeIssuedDate) {
    this.chequeIssuedDate = chequeIssuedDate;
  }

  public Date getChequeExpiryDate() {
    return chequeExpiryDate;
  }

  public void setChequeExpiryDate(Date chequeExpiryDate) {
    this.chequeExpiryDate = chequeExpiryDate;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public int getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(int accountNumber) {
    this.accountNumber = accountNumber;
  }

  @Override
  public Enum getType() {
    return PaymentMode.CHEQUE;
  }
}
