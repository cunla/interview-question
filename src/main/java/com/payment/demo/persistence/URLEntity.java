package com.payment.demo.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "TBL_URL")
public class URLEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "url")
	private String url;
	
	@Column(name = "hashCode")
	private String hashCode;

	// Setters and getters

	@Override
	public String toString() {
		return "URLEntity [url=" + url + ", hashCode=" + hashCode;
	}
}
