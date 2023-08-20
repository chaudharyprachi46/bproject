package com.app.service;

import java.util.List;

import com.app.exception.OrderException;
import com.app.model.Address;
import com.app.model.Order;
import com.app.model.User;

public interface OrderService {
	
	public Order createOrder(User user, Address shippingAddress); //admin
	
	public Order findOrderById(Long orderId) throws OrderException; //admin
	
	public List<Order> usersOrderHistory(Long userId); //admin/user
	
	public Order placedOrder(Long orderId) throws OrderException; //admin
	
	public Order confirmedOrder(Long orderId) throws OrderException; //admin

	public Order shippedOrder(Long orderId) throws OrderException; //admin
	
	public Order dileveredOrder(Long orderId) throws OrderException; //admin

	public Order cancledOrder(Long orderId) throws OrderException; //user/admin
	
	public List<Order> getAllOrders(); //user
	
	public void deleteOrder(Long orderId) throws OrderException; //admin


}
