package com.assesment.coffee.Process.Order.Service.repository;

import com.assesment.coffee.Process.Order.Service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}