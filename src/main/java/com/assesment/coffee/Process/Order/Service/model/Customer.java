package com.assesment.coffee.Process.Order.Service.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity representing a customer in the coffee order processing system.
 * Maps to the "customer" table in the database.
 */
@Entity
@Data
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String mobileNumber;
    private Integer loyaltyScore = 0;
}