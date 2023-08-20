package com.app.request;

import java.time.LocalDateTime;
import java.util.List;

import com.app.model.Category;
import com.app.model.Rating;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//defining product details
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateProductRequest {

	private String title;
	
	private String description;
	
	private int price;
	
	private int discountedPrice;
	
	private int discountedPercent;
	
	private int quantity;
	
	private String brand;
	
	private String color;
	
	private String imageUrl;
	
	private String topLevelCategory; 
	private String secondLevelCategory; 
	private String thirdLevelCategory; 
	
	
	
}


