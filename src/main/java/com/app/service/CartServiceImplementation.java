package com.app.service;

import org.springframework.stereotype.Service;

import com.app.exception.ProductException;
import com.app.model.Cart;
import com.app.model.CartItem;
import com.app.model.Product;
import com.app.model.User;
import com.app.repository.CartRepository;
import com.app.request.AddItemRequest;

@Service
public class CartServiceImplementation implements CartService {
	
	private CartRepository cartRepository;
	private CartItemService cartItemService; 
	private ProductService productService;
	
	

	public CartServiceImplementation(CartRepository cartRepository, CartItemService cartItemService,
			ProductService productService) {
		this.cartRepository = cartRepository;
		this.cartItemService = cartItemService;
		this.productService = productService;
	}

	@Override
	public Cart createCart(User user) {
		Cart cart= new Cart();
		cart.setUser(user);
		return cartRepository.save(cart);
	}

	@Override
	public String addCartItem(Long userId, AddItemRequest req) throws ProductException {
		
		 // Find the user's cart
		Cart cart= cartRepository.findByUserId(userId);
		// Find the product by its ID
		Product product= productService.findProductById(req.getProductId());
		
		// Check if the same cart item already exists in the cart
		CartItem isPresent= cartItemService.isCartItemExist(cart, product, userId);
		
		// If the cart item is not already present, add it to the cart
		if(isPresent==null) {
			CartItem cartItem=new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(req.getQuantity());
			cartItem.setUserId(userId);
		
			// Calculate the price for the cart item (quantity * discounted price)
			int price= req.getQuantity() * product.getDiscountedPercent();
			cartItem.setPrice(price);
			
			CartItem createdCartItem= cartItemService.createCartItem(cartItem);
			
			// Add the new cart item to the cart's list of cart items
			cart.getCartItems().add(createdCartItem);
		}
		
		return "Item Added to Cart";
	}

	@Override
	public Cart findUserCart(Long userId) {
		Cart cart= cartRepository.findByUserId(userId);
		
		int totalPrice=0;
		int totalDiscountedPrice=0;
		int totalItem=0;
		
		// Loop through each cart item in the user's cart to calculate total price 
		for(CartItem cartItem : cart.getCartItems()) {
			totalPrice += cartItem.getPrice();
			totalDiscountedPrice += cartItem.getDiscountedPrice();
			totalItem += cartItem.getQuantity();
		}
		
		cart.setTotalItem(totalItem);
		cart.setTotalPrice(totalPrice);
		cart.setDiscount(totalPrice - totalDiscountedPrice);
		
		return cartRepository.save(cart);
	}

	
}
