package com.assesment.coffee.Process.Order.Service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;


/**
 * Entity representing a coffee shop in the order processing system.
 * Maps to the "shop" table in the database.
 */
@Entity
@Data
@Table(name = "shop")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Integer maxQueues = 1;
    private Integer queueSize = 10;
}