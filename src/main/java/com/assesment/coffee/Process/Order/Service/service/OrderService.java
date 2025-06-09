package com.assesment.coffee.Process.Order.Service.service;

import com.assesment.coffee.Process.Order.Service.dto.OrderRequest;
import com.assesment.coffee.Process.Order.Service.dto.OrderResponse;
import com.assesment.coffee.Process.Order.Service.dto.QueueStatusResponse;
import com.assesment.coffee.Process.Order.Service.exception.OrderProcessingException;
import com.assesment.coffee.Process.Order.Service.model.*;
import com.assesment.coffee.Process.Order.Service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

/**
 * Service class for processing coffee orders.
 * Handles order creation, status retrieval, cancellation, and queue management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
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

        try {
            // Validate customer
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> {
                        String userMessage = "We couldn't find your account";
                        String technicalDetail = String.format("Customer not found with ID: %d",
                                request.getCustomerId());

                        log.error("Customer validation failed - {}", technicalDetail);
                        log.debug("Full customer lookup context - CustomerID: {},",
                                request.getCustomerId());

                        return new OrderProcessingException(userMessage, technicalDetail);
                    });

            log.debug("Found customer: {} (ID: {})", customer.getName(), customer.getId());

            // Validate shop
            Shop shop = shopRepository.findById(request.getShopId())
                    .orElseThrow(() -> {
                        String userMessage = "This coffee shop doesn't exist in our system";
                        String technicalDetail = String.format("Shop not found with ID: %d | Customer: %s (%d)",
                                request.getShopId(), customer.getName(), customer.getId());

                        log.error("Shop validation failed - {}", technicalDetail);
                        log.debug("Full shop lookup context - ShopID: {}, Customer: {}",
                                request.getShopId(), customer);

                        return new OrderProcessingException(userMessage, technicalDetail);
                    });

            log.info("Order processing for shop: {} (Queue: {})", shop.getName(), request.getQueueNumber());
            // Check shop hours
            LocalTime now = LocalTime.now();
            if (now.isBefore(shop.getOpeningTime()) || now.isAfter(shop.getClosingTime())) {
                OrderProcessingException ex =  new OrderProcessingException(
                        "Sorry, this shop is currently closed. Open hours: "
                                + shop.getOpeningTime() + " to " + shop.getClosingTime(),
                        "Order attempted outside business hours. Current time: " + now
                );
                log.warn(ex.getTechnicalDetail());
                throw ex;
            }

            // Validate queue number
            if (request.getQueueNumber() > shop.getMaxQueues()) {
                OrderProcessingException ex = new OrderProcessingException(
                        "This queue doesn't exist. Please choose between 1-" + shop.getMaxQueues(),
                        String.format("Invalid queue number %d. Shop %s only has %d queues",
                                request.getQueueNumber(),
                                shop.getName(),
                                shop.getMaxQueues())
                );
                log.warn(ex.getTechnicalDetail());
                throw ex;
            }

            // Check queue capacity
            int queuePosition = orderRepository.countActiveOrdersInQueue(shop.getId(), request.getQueueNumber());
            if (queuePosition >= shop.getQueueSize()) {
                OrderProcessingException ex = new OrderProcessingException(
                        "This queue is currently full. Please try another queue or come back later",
                        String.format("Queue %d at shop %s is full (%d/%d orders)",
                                request.getQueueNumber(),
                                shop.getName(),
                                queuePosition,
                                shop.getQueueSize())
                );
                log.warn(ex.getTechnicalDetail());
                throw ex;
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

            log.info("Order Saved Successfully for order id : {}", savedOrder.getId());

            return mapToResponse(savedOrder);

        } catch (OrderProcessingException e) {
            log.error("Order processing failed: {}", e.getTechnicalDetail());
            throw e;
        }
    }

    /**
     * Retrieves the status of a specified order.
     *
     * @param orderId the ID of the order
     * @return the response containing order status information
     */
    public OrderResponse getOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    String userMessage = "We couldn't find your Order to show the status";
                    String technicalDetail = String.format("Order not found with ID: %d", orderId);

                    log.error("GetOrderStatus validation failed - {}", technicalDetail);
                    log.debug("Getting status for - Order Id : {}",orderId);

                    return new OrderProcessingException(userMessage, technicalDetail);
                });
        return mapToResponse(order);
    }

    /**
     * Cancels an existing order if it is in the waiting status.
     *
     * @param orderId the ID of the order to cancel
     */
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    String userMessage = "We couldn't find your Order to cancel";
                    String technicalDetail = String.format("Order not found with ID: %d", orderId);

                    log.error("Order Cancellation failed - {}", technicalDetail);
                    log.debug("Order cancel for - Order Id : {}",orderId);

                    return new OrderProcessingException(userMessage, technicalDetail);
                });

        if (!"WAITING".equals(order.getStatus())) {
            OrderProcessingException ex = new OrderProcessingException("Only waiting orders can be cancelled",
                    "This order already cancelled or something error in status");
            log.warn(ex.getTechnicalDetail());
            throw ex;
        }


        order.setStatus("CANCELLED");

        orderRepository.save(order);
        log.info("Order Canceled Successfully for order id : {}", orderId);
    }

    /**
     * Retrieves the queue status for a specific shop and queue number.
     * Provides queue length and estimated wait time.
     *
     * @param shopId      the ID of the shop
     * @param queueNumber the specific queue number
     * @return the response containing queue status information
     */
    public QueueStatusResponse getQueueStatus(Long shopId, Integer queueNumber) {
        try {
            log.debug("Fetching queue status for shop {} queue {}", shopId, queueNumber);

            // Validate shop exists
            Shop shop = shopRepository.findById(shopId)
                    .orElseThrow(() -> {
                        String userMessage = "This coffee shop doesn't exist in our system";
                        String technicalDetail = String.format("Shop not found with ID: %d", shopId);

                        log.error("Shop validation failed for get queue status - {}", technicalDetail);
                        return new OrderProcessingException(userMessage, technicalDetail);
                    });

            // Handle default queue number
            if (queueNumber == null) {
                queueNumber = 1;
                log.debug("Using default queue number 1");
            }

            // Validate queue number
            if (queueNumber > shop.getMaxQueues()) {
                String userMessage = String.format("This queue doesn't exist. Please choose between 1-%d",
                        shop.getMaxQueues());
                String technicalDetail = String.format("Invalid queue number %d. Shop %s only has %d queues",
                        queueNumber, shop.getName(), shop.getMaxQueues());

                log.warn("Invalid queue number attempt - {}", technicalDetail);
                throw new OrderProcessingException(userMessage, technicalDetail);
            }

            // Get active orders
            List<Order> activeOrders = orderRepository.findByShopIdAndQueueNumberAndStatus(
                    shopId, queueNumber, "WAITING");

            log.debug("Found {} active orders in queue {} at shop {}",
                    activeOrders.size(), queueNumber, shopId);

            // Prepare response
            QueueStatusResponse response = new QueueStatusResponse();
            response.setQueueNumber(queueNumber);
            response.setQueueLength(activeOrders.size());
            response.setEstimatedWaitMinutes(activeOrders.size() * 2); // 2 mins per order

            log.info("Returning queue status for shop {} queue {}: {} orders ({} min wait)",
                    shopId, queueNumber, activeOrders.size(), response.getEstimatedWaitMinutes());

            return response;

        } catch (OrderProcessingException e) {
            log.error("Queue status check failed: {}", e.getTechnicalDetail());
            throw e;
        }
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