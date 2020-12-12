package com.cenfotec.examen3.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.examen3.entities.*;
import com.cenfotec.examen3.repositories.*;

@RestController
@RequestMapping({ "/clients" })
public class ClientController {
	private ClientRepository clientRepo;
	private OrderRepository orderRepo;


	public ClientController(ClientRepository clientRepo, OrderRepository orderRepo) {
		this.clientRepo = clientRepo;
		this.orderRepo = orderRepo;
	}

	@GetMapping
	public List<Client> getAllClients() {
		
		List<Client> listClients = this.clientRepo.findAll().stream().filter(c -> c.getStatus().equals("Activo"))
				.collect(Collectors.toList());
		
		for(Client cliente: listClients){
			ArrayList<Order> listOrders = new ArrayList<Order>();
			if(cliente.getOrders()!=null) {
				for(Order o: cliente.getOrders()) {
					Client c = o.getClient();
					c.setOrders(null);
					o.setClient(c);
					listOrders.add(o);
				}			
				cliente.setOrders(listOrders);
			}
		}	
		return listClients;
		
		
	}

	@GetMapping(path = { "/{id}" })
	public Object findById(@PathVariable int id) {
		if(clientRepo.findById(id).get().getStatus().equals("Activo")) {
			
			return clientRepo.findById(id).map(record ->
			{
				if(record.getOrders() != null)
					record.getOrders().stream().forEach(order -> order.setClient(null));
				return ResponseEntity.ok().body(record);
			}).orElse(ResponseEntity.notFound().build());
			
		}
		return "No encontrado";
	}
	

	@PostMapping
	public Client create(@RequestBody Client client) {
		client.setStatus("Activo");
		return this.clientRepo.save(client);
	}

	@PutMapping(value = "/{id}")
	public Object update(@PathVariable("id") int id, 
			@RequestBody Client client) {
		return this.clientRepo.findById(id).map(record -> {
			record.setName(client.getName());
			record.setLastName(client.getLastName());
			record.setAddress(client.getAddress());
			record.setAddressBilling(client.getAddressBilling());
			record.setNumCard(client.getNumCard());
			record.setExpirationDate(client.getExpirationDate());
			Client cUpdated = this.clientRepo.save(record);
			if(cUpdated.getOrders() != null)cUpdated.getOrders().stream().forEach(order -> order.setClient(null));
			return ResponseEntity.ok().body("Modificado\n\n" + cUpdated);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = "/{id}")
	public String  delete(@PathVariable("id") int id) {
	
		List<Order> ordersFiltered =
			    this.orderRepo.findAll()
			        .stream()
			        .filter(o -> o.getClient().getId() == id)
			        .collect(Collectors.toList());
		
		
		if (ordersFiltered.isEmpty() || ordersFiltered == null) {
			this.clientRepo.findById(id).map(record -> {
				this.clientRepo.delete(record);
				return "Eliminado";
			}).get();
		}else {

			Client c = this.clientRepo.findById(id).get();
			c.setStatus("Eliminado");
			
			this.clientRepo.save(c);

		}

		

		return "Eliminado";
	}
	
	
	//Busqueda de clientes por aproximacion de apellidos
	
	@PostMapping(path= {"/byLastName"})
	public List<Client> getAllClientLastName(@RequestBody Client client) {
		ArrayList<Client> listClients = new ArrayList<Client>();
		for(Client c: this.clientRepo.findAll()) {
			if(c.getLastName().toLowerCase().contains(client.getLastName().toLowerCase())) {
				
				String apellido = c.getLastName().toLowerCase();
				
				int pposicion = apellido.indexOf(client.getLastName().toLowerCase());
				int uposicion = apellido.indexOf(client.getLastName())
						+ client.getLastName().length();
				
				String newLastName = "";
				
				for(int i = 0; i < apellido.length(); i++) {
					if((i >= pposicion && i < uposicion) || i == 0) {
						newLastName += apellido.substring(i,i+1).toUpperCase();
					} else {
						newLastName += apellido.substring(i,i+1);
					}
				}
				c.setLastName(newLastName);
				if(c.getOrders()!= null)
					c.getOrders().stream().forEach(record -> record.setClient(null));
				listClients.add(c);	
			}
									
		}

		return listClients;
	}
	
	
	//Busqueda de clientes por aproximacion de direccion de cobro
	
		@PostMapping(path= {"/byAddressBilling"})
		public List<Client> getAllClientAddBilling(@RequestBody Client client) {
			ArrayList<Client> listClients = new ArrayList<Client>();
			for(Client c: this.clientRepo.findAll()) {
				if(c.getAddressBilling().toLowerCase()
						.contains(client.getAddressBilling().toLowerCase())) {
					
					String direccionCobro = c.getAddressBilling().toLowerCase();
					
					int pposicion = direccionCobro.indexOf(client.getAddressBilling().toLowerCase());
					int uposicion = direccionCobro.indexOf(client.getAddressBilling())
							+ client.getAddressBilling().length();
					String newAddBilling = "";
					
					for(int i = 0; i < direccionCobro.length(); i++) {
						
						if((i >= pposicion && i < uposicion) || i == 0) {
							newAddBilling += direccionCobro.substring(i,i+1).toUpperCase();
						} else {
							newAddBilling += direccionCobro.substring(i,i+1);
						}
					}
					if(c.getOrders()!= null)
						c.getOrders().stream().forEach(record -> record.setClient(null));
					c.setAddressBilling(newAddBilling);
					listClients.add(c);	
				}
										
			}

			return listClients;
		}

}
