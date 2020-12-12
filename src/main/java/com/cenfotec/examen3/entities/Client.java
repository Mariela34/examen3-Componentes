package com.cenfotec.examen3.entities;


import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Entity
@Table(name="clients")
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "lastName", nullable = false)
	private String lastName;
	@Column(name = "address", nullable = false)
	private String address;
	@Column(name = "addressBilling", nullable = false)
	private String addressBilling;
	@Column(name = "numCard", nullable = false)
	private int numCard;
	@Column(name = "expirationDate", nullable = false)
	private String expirationDate;
	@Column(name = "status", nullable = false)
	private String status;	
	@OneToMany(fetch=FetchType.LAZY,  mappedBy="client")
	private List<Order> orders;
	@Override
	public String toString() {
		return "Client [\n"
				+ "\"id\": " + id + 
				",\n   \"name\": '" + name + 
				"' ,\n   \"lastName\": '" + lastName + 
				"' ,\n   \"address\": '" + address + 
				"' ,\n   \"addressBilling\": '" + addressBilling + 
				"' ,\n   \"numCard\": " + numCard + 
				",\n   \"expirationDate\": '" + expirationDate+"' \n  ]";
	}
}
