package com.example.orderservice.event;

import com.example.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

	private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

	@Value("${app.kafka.order-events-topic:order-events}")
	private String orderEventsTopic;

	public void publishOrderCreated(Order order) {
		try {
			OrderEvent event = OrderEvent.from(order);
			kafkaTemplate.send(orderEventsTopic, order.getId(), event);
			log.info("Published order-created event for orderId={}", order.getId());
		} catch (Exception ex) {
			log.error("Failed to publish order-created event for orderId={}", order.getId(), ex);
		}
	}
}
