package com.microshop.inventory.messaging;

import com.microshop.inventory.dto.OrderEvent;
import com.microshop.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final InventoryService inventoryService;

    @RabbitListener(queues = "order_queue")
    public void consumeOrderEvent(OrderEvent event) {
        log.info("Recibido evento de pedido: {} para pedido ID: {}", event.getEvent(), event.getOrder_id());

        try {
            if ("order_created".equals(event.getEvent())) {
                event.getItems().forEach(item -> {
                    inventoryService.reduceStock(item.getProduct_id(), item.getQuantity());
                    log.info("Stock reducido para producto {}: {}", item.getProduct_id(), item.getQuantity());
                });
            } else if ("order_cancelled".equals(event.getEvent())) {
                event.getItems().forEach(item -> {
                    inventoryService.restoreStock(item.getProduct_id(), item.getQuantity());
                    log.info("Stock restaurado para producto {}: {}", item.getProduct_id(), item.getQuantity());
                });
            }
        } catch (Exception e) {
            log.error("Error procesando evento de pedido: {}", e.getMessage());
            // En un sistema real, aquí manejaríamos reintentos o colas de error
        }
    }
}
