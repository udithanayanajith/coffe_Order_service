package com.assesment.coffee.Process.Order.Service.controller;

import com.assesment.coffee.Process.Order.Service.dto.OrderRequest;
import com.assesment.coffee.Process.Order.Service.dto.OrderResponse;
import com.assesment.coffee.Process.Order.Service.dto.QueueStatusResponse;
import com.assesment.coffee.Process.Order.Service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling coffee order processing requests.
 * Manages the lifecycle of orders including creation, status retrieval, and cancellation.
 */
@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Order Processing", description = "Endpoints for coffee order processing")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    /**
     * Processes a new coffee order and queues it.
     *
     * @param request the order request details
     * @return the response containing order information
     */
    @PostMapping
    @Operation(summary = "Process new order", description = "Creates and queues a new coffee order")
    public ResponseEntity<OrderResponse> processOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.processOrder(request));
    }

    /**
     * Retrieves the current status and queue position of a specified order.
     *
     * @param orderId the ID of the order
     * @return the response containing order status
     */
    @GetMapping("/{orderId}")
    @Operation(summary = "Get order status", description = "Returns current queue position and status")
    public ResponseEntity<OrderResponse> getOrderStatus(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderStatus(orderId));
    }

    /**
     * Cancels an existing order and removes it from the queue.
     *
     * @param orderId the ID of the order to cancel
     * @return a response indicating no content
     */
    @DeleteMapping("/{orderId}")
    @Operation(summary = "Cancel order", description = "Removes an order from the queue")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves the status of the queue for a specific shop.
     * Provides queue length and wait time estimates.
     *
     * @param shopId the ID of the shop
     * @param queueNumber optional parameter for specific queue info
     * @return the response containing queue status information
     */
    @GetMapping("/queue/{shopId}")
    @Operation(summary = "Get queue status", description = "Returns queue length and wait time estimate")
    public ResponseEntity<QueueStatusResponse> getQueueStatus(
            @PathVariable Long shopId,
            @RequestParam(required = false) Integer queueNumber) {
        return ResponseEntity.ok(orderService.getQueueStatus(shopId, queueNumber));
    }
}