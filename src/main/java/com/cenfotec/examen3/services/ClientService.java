package com.cenfotec.examen3.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cenfotec.examen3.entities.Client;
import com.cenfotec.examen3.repositories.ClientRepository;

@Service
public class ClientService {
	@Autowired
	ClientRepository clientRepo;	

	public List<Client> getAllClients(int count){
		return this.clientRepo.findAll().stream().limit(count).collect(Collectors.toList());
	}
	
	public Optional<Client> getClient(int id) {
		return this.clientRepo.findById(id);
	}
	
	public Client createClient(String name, String lastName, String address, String addressBilling, 
			int numCard, String expirationDate) {
		Client c = new Client();
		c.setName(name);
		c.setLastName(lastName);
		c.setAddress(address);
		c.setAddressBilling(addressBilling);
		c.setNumCard(numCard);
		c.setExpirationDate(expirationDate);
		c.setStatus("Activo");
		return this.clientRepo.save(c);
	}
	
	public Client updateClient(int id, String name, String lastName, String address, String addressBilling, 
			int numCard, String expirationDate) {
		Client c = this.getClient(id).get();
		c.setName(name);
		c.setLastName(lastName);
		c.setAddress(address);
		c.setAddressBilling(addressBilling);
		c.setNumCard(numCard);
		c.setExpirationDate(expirationDate);
		return this.clientRepo.save(c);
	}
	
	public String deleteClient(int id) {
		String mensaje = "";
		try {
			this.clientRepo.deleteById(id);
			mensaje="Eliminado exitosamente";
		}catch(Exception e) {
			mensaje = "Hubo un error al eliminar: " + e.getMessage();
		}
		return mensaje;
	}
	
}
