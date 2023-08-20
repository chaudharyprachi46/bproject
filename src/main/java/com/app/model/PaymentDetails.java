package com.app.model;


import javax.persistence.Entity;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDetails {

	private String paymentMethod;
	
	private String status;
	
	private String paymentId;
	
	private String razorpayPaymentLinkId;
	
	private String razorpayPaymentLinkReferenceId;
	
	private String razorpayPaymentLinkStatus;
	
	private String razorpayPaymentId;
}
