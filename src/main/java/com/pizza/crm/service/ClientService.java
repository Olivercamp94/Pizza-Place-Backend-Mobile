package com.pizza.crm.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;


import com.pizza.crm.model.Client;
import com.pizza.crm.repository.ClientRepository;

@Service
public class ClientService  {

    // Lista simulando um banco de dados temporário
    
    private final ClientRepository clientRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public ClientService(ClientRepository clientRepository) {
    	this.clientRepository = clientRepository;
    }
    
    // // CREATE - Adicionar um novo cliente
    // public boolean addClient(Client client) {
    //     try {
    //         // Hashear a senha do cliente antes de salvar
    //         String hashedPassword = passwordEncoder.encode(client.getPassword());
    //         client.setPassword(hashedPassword);
            
    //         // Salvar o cliente no repositório
    //         clientRepository.save(client);
            
    //         // Se o cliente for salvo com sucesso, retorna true
    //         return true;
    //     } catch (Exception e) {
    //         // Em caso de erro, exibe o erro (opcional) e retorna false
    //         e.printStackTrace(); // Opcional: log da exceção
    //         return false;
    //     }
    // }

    public String addClient(Client client) {
        try {
            // Verifica se o cliente com o mesmo e-mail já existe
            if (clientRepository.existsByEmail(client.getEmail())) {
                // Se já existir, retorna false ou pode lançar uma exceção customizada
                return "Email exists in DB";
            }

            // Hashea a senha antes de salvar
            String hashedPassword = passwordEncoder.encode(client.getPassword());
            client.setPassword(hashedPassword);

            // Salva o cliente
            clientRepository.save(client);
            return "Ok";
        } catch (DataIntegrityViolationException e) {
            // Caso aconteça algum erro relacionado à integridade dos dados (ex: chave única violada)
            // Pode ser uma violação de restrição de chave única
            return "Erro ao salvar o cliente, verifique os dados e tente novamente.";
        } catch (Exception e) {
            // Caso ocorra qualquer outro erro
            e.printStackTrace();
            return "Ocorreu um erro inesperado ao tentar adicionar o cliente.";
        }
    }

    // READ - Buscar um cliente pelo e-mail
    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
    
    // READ - Buscar cliente por ID
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }
    
    // UPDATE - Atualizar dados de um cliente
    public Client updateClient(Long id, Client updatedClient) {
    	Optional<Client> clientExists = clientRepository.findById(id);
    	if(clientExists.isPresent()) {
    		Client client = clientExists.get();
    		client.setEmail(updatedClient.getEmail());
    		client.setName(updatedClient.getName());
    		client.setPhone(updatedClient.getPhone());
    		return clientRepository.save(client);
    	}else {
    		throw new RuntimeException("Client id: " + id + " not found.");
    	}
    }

    // DELETE - Remover um cliente
    @Transactional
    public boolean deleteClient(Long id) {
    	if(clientRepository.existsById(id)) {
    		clientRepository.deleteById(id);
    		return true;
    	}else {
    		throw new RuntimeException("Client id: " + id + " not found.");
    	}
    }

    public <Opitional>Client loginClient(String email, String password) {
        // Search client in repo
        Client client = clientRepository.findByEmail(email).orElse(null);

        if(client != null && passwordEncoder.matches(password, client.getPassword())){
            // if pass matches returns client
            return client;
        }
        return null;
    }

}
