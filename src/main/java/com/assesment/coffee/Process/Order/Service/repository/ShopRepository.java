package com.assesment.coffee.Process.Order.Service.repository;

import com.assesment.coffee.Process.Order.Service.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {
}