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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products") // to specify table name
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
	
	//size
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String title;
	
	private String description;
	
	private int price;
	
	@Column(name="discounted_price")
	private int discountedPrice;
	
	@Column(name="discounted_percent")
	private int discountedPercent;
	
	@Column(name="quantity")
	private int quantity;
	
	private String brand;
	
	private String color;
	
	@Column(name="image_url")
	private String imageUrl;
	
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Rating> rating=new ArrayList<>();
	
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Rating> reviews=new ArrayList<>();
	
	@Column(name="num_ratings")
	private int numRatings;
	
	@ManyToOne()
	@JoinColumn(name="category_id")
	private Category category;
	
	private LocalDateTime createdAt;
	
	
}
