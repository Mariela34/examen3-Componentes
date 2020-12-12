package com.cenfotec.examen3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cenfotec.examen3.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
