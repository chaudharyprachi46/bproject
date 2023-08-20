package com.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews") // to specify table name
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String review;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "product_id", nullable=false)
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable=false)
	private User user;
	
	private LocalDateTime createdAt;

}
