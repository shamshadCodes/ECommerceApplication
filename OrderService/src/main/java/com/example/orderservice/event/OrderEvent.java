package com.example.orderservice.event;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {

	private String id;
	private String userId;
	private BigDecimal totalAmount;
	private OrderStatus status;
	private LocalDateTime orderDate;
	private String paymentMethod;
	private String shippingAddress;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static OrderEvent from(Order order) {
		return OrderEvent.builder()
				.id(order.getId())
				.userId(order.getUserId())
				.totalAmount(order.getTotalAmount())
				.status(order.getStatus())
				.orderDate(order.getOrderDate())
				.paymentMethod(order.getPaymentMethod())
				.shippingAddress(order.getShippingAddress())
				.createdAt(order.getCreatedAt())
				.updatedAt(order.getUpdatedAt())
				.build();
	}
}
