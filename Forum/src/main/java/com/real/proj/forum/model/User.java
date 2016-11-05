package com.real.proj.forum.model;

import com.real.proj.forum.model.Forum;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
	@Id
	String Id;
	String firstName;
	String lastName;
	String email;
	String mobileNo;
	List<String> subscriptions;

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public void addSubscription(String forumId) {
		this.getSubscriptions().add(forumId);
	}

	public List<String> getSubscriptions() {
		return this.subscriptions == null ? (this.subscriptions = new ArrayList()) : this.subscriptions;
	}

	public List<Forum> getSubscriptions(PageRequest pageRequest) {
		return null;
	}
}