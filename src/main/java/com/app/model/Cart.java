package com.app.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart") // to specify table name
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	
	@OneToMany(mappedBy="cart", cascade= CascadeType.ALL, orphanRemoval = true)
	@Column(name="cart_items")
	private Set<CartItem> cartItems = new HashSet<>();
	
	@Column(name="total_item")
	private double totalItem;
	
	@Column(name="total_price")
	private double totalPrice;
	
	private int totalDiscountedPrice;
	
	private int discount;
	
}
