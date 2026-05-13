package com.microshop.inventory.dto;

import java.util.List;

public class OrderEvent {
    private String event;
    private Long order_id;
    private List<OrderItemDto> items;

    public OrderEvent() {
    }

    public OrderEvent(String event, Long order_id, List<OrderItemDto> items) {
        this.event = event;
        this.order_id = order_id;
        this.items = items;
    }

    // Getters and Setters
    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }

    public Long getOrder_id() { return order_id; }
    public void setOrder_id(Long order_id) { this.order_id = order_id; }

    public List<OrderItemDto> getItems() { return items; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }

    public static class OrderItemDto {
        private Long product_id;
        private Integer quantity;

        public OrderItemDto() {
        }

        public OrderItemDto(Long product_id, Integer quantity) {
            this.product_id = product_id;
            this.quantity = quantity;
        }

        public Long getProduct_id() { return product_id; }
        public void setProduct_id(Long product_id) { this.product_id = product_id; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}
