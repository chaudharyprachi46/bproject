package com.app.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.app.exception.CartItemException;
import com.app.exception.UserException;
import com.app.model.Cart;
import com.app.model.CartItem;
import com.app.model.Product;
import com.app.model.User;
import com.app.repository.CartItemRepository;
import com.app.repository.CartRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Service
public class CartItemServiceImplemention implements CartItemService {

	private CartItemRepository cartItemRepository;
	private UserService userService;
	private CartRepository cartRepository;
	
	

	@Override
	public CartItem createCartItem(CartItem cartItem) {
		cartItem.setQuantity(1);
		cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
		cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());

		CartItem createdCartItem = cartItemRepository.save(cartItem);

		return createdCartItem;
	}

	@Override
	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
		// Find the existing cart item by its ID
		CartItem item = findCartItemById(id);
		// Find the user by their ID
		User user = userService.findUserById(userId);

		// user which created cart only that can update the cart
		// Check if the user ID matches the ID of the user who created the cart item
		if (user.getId().equals(userId)) {
			// Update the quantity of the cart item based on the new quantity from the
			// provided cartItem
			item.setQuantity(cartItem.getQuantity());
			item.setPrice(item.getQuantity() * item.getProduct().getPrice());
			item.setDiscountedPrice(item.getQuantity() * item.getProduct().getPrice());
		}

		return cartItemRepository.save(item);
	}

	@Override
	public CartItem isCartItemExist(Cart cart, Product product, Long userId) {

		CartItem cartItem = cartItemRepository.isCartItemExsit(cart, product, userId);
		return cartItem;
	}

	@Override
	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
		// Find the existing cart item by its ID
		CartItem cartItem = findCartItemById(cartItemId);
		// Find the user who created the cart item
		User user = userService.findUserById(cartItem.getUserId());
		// Find the user who is currently logged in
		User reqUser = userService.findUserById(userId); // the user which is logged in

		// Check if the user who is logged in has the same ID as the user who created
		// the cart item
		if (user.getId().equals(reqUser.getId())) {
			cartItemRepository.deleteById(cartItemId);
		} else {
			throw new UserException("You can't remove another user's item");
		}
	}

	@Override
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		Optional<CartItem> opt = cartItemRepository.findById(cartItemId);

		if (opt.isPresent()) {
			return opt.get();
		}
		throw new CartItemException("cartItem not found with id - " + cartItemId);
	}

}
