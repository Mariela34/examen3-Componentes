package com.cenfotec.examen3.mutation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cenfotec.examen3.services.ClientService;
import com.cenfotec.examen3.entities.*;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;


@Component
public class ClientMutation implements GraphQLMutationResolver {

	@Autowired
	private ClientService clienteService;
	
	
	public Client createClient(String name, String lastName, String address, String addressBilling,
			int numCard, String expDate) {
		return this.clienteService.createClient(name, lastName, address, addressBilling, numCard, expDate);
	}
	
	public Client updateClient(int id, String name, String lastName, String address, String addressBilling,
			int numCard, String expirationDate) {
		return this.clienteService.updateClient(id, name, lastName, address, addressBilling, numCard, expirationDate);
	}
	
	public String deleteClient(int id) {
		return this.clienteService.deleteClient(id);
	}
}
