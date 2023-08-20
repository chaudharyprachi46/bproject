package com.app.service;

import java.time.LocalDateTime;
import java.util.List;

import com.app.exception.ProductException;
import com.app.model.Product;
import com.app.model.Review;
import com.app.model.User;
import com.app.repository.ProductRepository;
import com.app.repository.ReviewRepository;
import com.app.request.ReviewRequest;

public class ReviewServiceImplementation implements ReviewService{

	private ReviewRepository reviewRepository;
	private ProductService productService;
	
	public ReviewServiceImplementation(ReviewRepository reviewRepository, ProductService productService,
			ProductRepository productRepository) {
		super();
		this.reviewRepository = reviewRepository;
		this.productService = productService;
	}

	@Override
	public Review createReview(ReviewRequest req, User user) throws ProductException {
		Product product= productService.findProductById(req.getProductId());
		
		Review review= new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReview(req.getReview());
		review.setCreatedAt(LocalDateTime.now());
		
		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getAllReview(Long productId) {
		
		return reviewRepository.getAllProductsReview(productId);
	}
	
	

}
