package com.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "orders") // to specify table name
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="order_id")
	private String orderId;
	
	@ManyToOne   //1 user has multiple orders
	private User user;
	
	@OneToMany(mappedBy="order", cascade = CascadeType.ALL)//1 order has multiple items
	private List<OrderItem> orderItems= new ArrayList<>();
	
	private LocalDateTime orderDate;
	
	private LocalDateTime deliveryDate;
	
	@OneToOne   //on 1order can be delivered to 1address only
	private Address shippingAddress;
	
	@Embedded
	private PaymentDetails paymentDetails= new PaymentDetails();
	
	
	private double totalPrice;
	
	private Integer totalDiscountedPrice;
	
	private Integer discount;
	
	private String orderStatus;
	
	private int totalItem;
	
	private LocalDateTime createdAt;
	
	

}
