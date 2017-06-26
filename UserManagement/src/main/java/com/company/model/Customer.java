package com.company.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.company.model.user.User;

@Document(collection="user")
public class Customer extends User {

	private String address;
	
	public Customer() {
		super.setUserType("CUSTOMER");
	}
	
	public Customer(String username, String password, String address) {
		super(username,password);
		super.setUserType("CUSTOMER");
		this.address = address;
	}
    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
    public String toString() {
        return String.format(
                "Customer[ username='%s', password='%s, address=%s']",
                this.getUsername(), this.getPassword(), this.getAddress());
    }

}