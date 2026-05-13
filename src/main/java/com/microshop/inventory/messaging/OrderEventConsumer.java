package com.microshop.inventory.messaging;

import com.microshop.inventory.dto.OrderEvent;
import com.microshop.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final InventoryService inventoryService;

    public OrderEventConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RabbitListener(queues = "order_queue")
    public void consumeOrderEvent(OrderEvent event) {
        log.info("Recibido evento de pedido: {} para pedido ID: {}", event.getEvent(), event.getOrder_id());

        try {
            if ("order_created".equals(event.getEvent())) {
                if (event.getItems() != null) {
                    event.getItems().forEach(item -> {
                        inventoryService.reduceStock(item.getProduct_id(), item.getQuantity());
                        log.info("Stock reducido para producto {}: {}", item.getProduct_id(), item.getQuantity());
                    });
                }
            } else if ("order_cancelled".equals(event.getEvent())) {
                if (event.getItems() != null) {
                    event.getItems().forEach(item -> {
                        inventoryService.restoreStock(item.getProduct_id(), item.getQuantity());
                        log.info("Stock restaurado para producto {}: {}", item.getProduct_id(), item.getQuantity());
                    });
                }
            }
        } catch (Exception e) {
            log.error("Error procesando evento de pedido: {}", e.getMessage());
        }
    }
}
