package com.cenfotec.examen3.query;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cenfotec.examen3.entities.Client;
import com.cenfotec.examen3.services.ClientService;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;


@Component
public class ClientQuery implements GraphQLQueryResolver{
	
	@Autowired
	private ClientService clientService;

	public List<Client> getClients(int count){
		return this.clientService.getAllClients(count);
	}
	
	public Optional<Client> getClient(int id){
		return this.getClient(id);
	}
}
