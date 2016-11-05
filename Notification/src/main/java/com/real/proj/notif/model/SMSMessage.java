package com.real.proj.notif.model;

import javax.validation.constraints.NotNull;

public class SMSMessage implements java.io.Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  @NotNull (message = "Mobile number cannot be empty")
  private String mobileNo;
  @NotNull (message ="Message cannot be empty")
  private String message;
  
  public SMSMessage() {
    
  }

  public SMSMessage(String mobileNo, String message) {
    super();
    this.mobileNo = mobileNo;
    this.message = message;
  }

  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "SMSMessage [mobileNo=" + mobileNo + ", message=" + message + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((message == null) ? 0 : message.hashCode());
    result = prime * result + ((mobileNo == null) ? 0 : mobileNo.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SMSMessage other = (SMSMessage) obj;
    if (message == null) {
      if (other.message != null)
        return false;
    } else if (!message.equals(other.message))
      return false;
    if (mobileNo == null) {
      if (other.mobileNo != null)
        return false;
    } else if (!mobileNo.equals(other.mobileNo))
      return false;
    return true;
  }

  
}
