package com.assesment.coffee.Process.Order.Service.repository;

import com.assesment.coffee.Process.Order.Service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByShopIdAndQueueNumberAndStatus(Long shopId, Integer queueNumber, String status);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.shop.id = :shopId AND o.queueNumber = :queueNumber AND o.status = 'WAITING'")
    Integer countActiveOrdersInQueue(@Param("shopId") Long shopId, @Param("queueNumber") Integer queueNumber);
}