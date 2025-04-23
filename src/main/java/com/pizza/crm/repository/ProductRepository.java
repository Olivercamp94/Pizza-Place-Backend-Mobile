package com.pizza.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pizza.crm.model.Product;

@Repository
public interface ProductRepository extends JpaRepository <Product, Long>{
    
}
