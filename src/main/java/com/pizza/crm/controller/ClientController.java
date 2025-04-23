package com.pizza.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pizza.crm.model.Client;
import com.pizza.crm.service.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/add")
    public ResponseEntity<String> addClient(@RequestBody Client client) {
    	String savedClient = clientService.addClient(client);
    	return ResponseEntity.ok(savedClient);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Client> getClientByEmail(@PathVariable String email) {
    	return clientService.getClientByEmail(email)
    			.map(ResponseEntity::ok)
    			.orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Client> getClientByEmail(@PathVariable Long id) {
    	return clientService.getClientById(id)
    			.map(ResponseEntity::ok)
    			.orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client updatedClient) {        
    	try {
    		Client updated = clientService.updateClient(id, updatedClient);
    		return ResponseEntity.ok(updated);
    	}catch (RuntimeException e){
    		return ResponseEntity.notFound().build();
    	}
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (clientService.deleteClient(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
