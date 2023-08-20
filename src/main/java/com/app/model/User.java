package com.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users") // to specify table name
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	private String password;
	
	private String email;
	
	private String role;
	
	private String mobile;
	
	@OneToMany(mappedBy="user",cascade = CascadeType.ALL)
	private List<Address> address=new ArrayList<>();
	
	@Embedded
	@ElementCollection
	@CollectionTable(name="payment_information", joinColumns= @JoinColumn(name="user_id"))
	private List<PaymentInformation> paymentinformation = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	private List<Rating> ratings = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	private List<Review> reviews=new ArrayList<>();
	
	private LocalDateTime createdAt;
	
	
}
