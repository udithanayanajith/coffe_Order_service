package com.assesment.coffee.Process.Order.Service.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


/**
 * Entity representing a coffee order in the order processing system.
 * Maps to the "coffee_order" table in the database.
 */
@Entity
@Data
@Table(name = "coffee_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    private String itemName;
    private Double itemPrice;
    private Integer queueNumber = 1;
    private Integer queuePosition;
    private String status = "WAITING";

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}