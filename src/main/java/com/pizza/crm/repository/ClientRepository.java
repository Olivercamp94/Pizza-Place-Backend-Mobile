package com.pizza.crm.repository;

import com.pizza.crm.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    Optional<Client> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsById(Long id);
	void deleteById(Long id);
}
