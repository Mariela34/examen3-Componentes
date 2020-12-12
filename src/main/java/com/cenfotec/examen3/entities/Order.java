package com.cenfotec.examen3.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "orders")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String typeProduct;
	private String pathImage;
	private int cost;
	private int cantProd;
	private int totalOrder;
	@ManyToOne
	@JoinColumn(name = "id_client", nullable = false)
	private Client client;
	@Override
	public String toString() {
		return "Order [\n"
				+ "\"id\":" + id + 
				",\n \"typeProduct\":'" + typeProduct +
				"',\n \"pathImage\": '" + pathImage + 
				"',\n \"cost\": " + cost + 
				",\n \"cantProd\": " + cantProd + 
				",\n \"totalOrder\": " + totalOrder + 
				",\n " + client + 
				"\n]";
	}

}
