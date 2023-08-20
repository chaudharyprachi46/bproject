package com.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items") // to specify table name
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	@JsonIgnore
	@ManyToOne   //1 user has multiple orders
	private Order order;
	
	@ManyToOne
	private Product product;
	
	private String size;
	
	private int quantity;
	
	private Integer price; 
	
	private Integer discountedPrice;
	
	private Long userId;
	
	private LocalDateTime deliveryDate;
	

}
