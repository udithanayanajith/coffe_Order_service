package com.assesment.coffee.Process.Order.Service.service;

import com.assesment.coffee.Process.Order.Service.dto.OrderRequest;
import com.assesment.coffee.Process.Order.Service.dto.OrderResponse;
import com.assesment.coffee.Process.Order.Service.dto.QueueStatusResponse;
import com.assesment.coffee.Process.Order.Service.exception.OrderProcessingException;
import com.assesment.coffee.Process.Order.Service.model.*;
import com.assesment.coffee.Process.Order.Service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

/**
 * Service class for processing coffee orders.
 * Handles order creation, status retrieval, cancellation, and queue management.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;

    /**
     * Processes a new coffee order and validates customer and shop details.
     * Checks shop hours and queue capacity before saving the order.
     *
     * @param request the order request details
     * @return the response containing the saved order information
     */
    public OrderResponse processOrder(OrderRequest request) {
        // Validate customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new OrderProcessingException("Customer not found"));

        // Validate shop
        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new OrderProcessingException("Shop not found"));

        // Check shop hours
        LocalTime now = LocalTime.now();
        if (now.isBefore(shop.getOpeningTime()) || now.isAfter(shop.getClosingTime())) {
            throw new OrderProcessingException("Shop is currently closed");
        }

        // Validate queue number
        if (request.getQueueNumber() > shop.getMaxQueues()) {
            throw new OrderProcessingException("Invalid queue number");
        }

        // Check queue capacity
        int queuePosition = orderRepository.countActiveOrdersInQueue(shop.getId(), request.getQueueNumber());
        if (queuePosition >= shop.getQueueSize()) {
            throw new OrderProcessingException("Queue is full");
        }

        // Create and save order
        Order order = new Order();
        order.setCustomer(customer);
        order.setShop(shop);
        order.setItemName(request.getItemName());
        order.setItemPrice(request.getItemPrice());
        order.setQueueNumber(request.getQueueNumber());
        order.setQueuePosition(queuePosition + 1);

        Order savedOrder = orderRepository.save(order);

        // Update customer loyalty
        customer.setLoyaltyScore(customer.getLoyaltyScore() + 1);
        customerRepository.save(customer);

        return mapToResponse(savedOrder);
    }

    /**
     * Retrieves the status of a specified order.
     *
     * @param orderId the ID of the order
     * @return the response containing order status information
     */
    public OrderResponse getOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderProcessingException("Order not found"));
        return mapToResponse(order);
    }

    /**
     * Cancels an existing order if it is in the waiting status.
     *
     * @param orderId the ID of the order to cancel
     */
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderProcessingException("Order not found"));

        if (!"WAITING".equals(order.getStatus())) {
            throw new OrderProcessingException("Only waiting orders can be cancelled");
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }

    /**
     * Retrieves the queue status for a specific shop and queue number.
     * Provides queue length and estimated wait time.
     *
     * @param shopId the ID of the shop
     * @param queueNumber the specific queue number
     * @return the response containing queue status information
     */
    public QueueStatusResponse getQueueStatus(Long shopId, Integer queueNumber) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new OrderProcessingException("Shop not found"));

        if (queueNumber == null) queueNumber = 1;
        if (queueNumber > shop.getMaxQueues()) {
            throw new OrderProcessingException("Invalid queue number");
        }

        List<Order> activeOrders = orderRepository.findByShopIdAndQueueNumberAndStatus(
                shopId, queueNumber, "WAITING");

        QueueStatusResponse response = new QueueStatusResponse();
        response.setQueueNumber(queueNumber);
        response.setQueueLength(activeOrders.size());
        response.setEstimatedWaitMinutes(activeOrders.size() * 2); // 2 mins per order

        return response;
    }

    /**
     * Maps an Order entity to an OrderResponse DTO.
     *
     * @param order the order entity to map
     * @return the mapped OrderResponse
     */
    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setCustomerName(order.getCustomer().getName());
        response.setShopName(order.getShop().getName());
        response.setItemName(order.getItemName());
        response.setItemPrice(order.getItemPrice());
        response.setQueueNumber(order.getQueueNumber());
        response.setQueuePosition(order.getQueuePosition());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setEstimatedWaitMinutes(
                order.getQueuePosition() != null ? order.getQueuePosition() * 2 : 0);

        return response;
    }
}