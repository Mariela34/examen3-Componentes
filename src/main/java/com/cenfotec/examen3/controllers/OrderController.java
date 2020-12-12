package com.cenfotec.examen3.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cenfotec.examen3.entities.Client;
import com.cenfotec.examen3.entities.Order;
import com.cenfotec.examen3.repositories.ClientRepository;
import com.cenfotec.examen3.repositories.OrderRepository;

@RestController
@RequestMapping({ "/orders" })
public class OrderController {
	private OrderRepository orderRepo;
	private ClientRepository clientRepo;

	public OrderController(OrderRepository orderRepo, ClientRepository clientRepo) {
		this.orderRepo = orderRepo;
		this.clientRepo = clientRepo;
	}

	@GetMapping
	public List<Order> getAllOrders() {
		List<Order> orders = this.orderRepo.findAll();
		ArrayList<Order> listOrders = new ArrayList<>();
		for(Order o: orders) {
			Client c = o.getClient();
			c.setOrders(null);
			o.setClient(c);
			listOrders.add(o);
		}
		
		return listOrders;
	}

	
	
	@PostMapping(value = { "/listType/{type}" })
	public List<Order> findAllOrderItem(@PathVariable("type") String type) {
		List<Order> orders = this.orderRepo.findAll().stream().filter(o -> o.getTypeProduct().equals(type.toLowerCase()))
				.collect(Collectors.toList());
		ArrayList<Order> listOrders = new ArrayList<Order>();
		for(Order o: orders) {
			Client c = o.getClient();
			c.setOrders(null);
			o.setClient(c);
			listOrders.add(o);
		}
		
		return listOrders;
	}

	@PostMapping(value = "/generateOrder/{id_cliente}")
	public Object create(@PathVariable("id_cliente") int id_cliente, @RequestBody Order order) {
		Order viewOrder = new Order();
		
		Optional<Client> c = this.clientRepo.findById(id_cliente);
		if(c.isPresent()) {
			int cost;
			switch(order.getTypeProduct().toLowerCase()) {
			case "tasa":
				cost = 15;
			case "camiseta":
				cost = 9;
				break;
			case "almohadon":
				cost = 8;
				break;
			case "vaso":
				cost = 13;
				break;
				default:
					return "Tipo invalido";
			}
			
			order.setCost(cost);
			order.setTotalOrder(cost*order.getCantProd());
							
			order.setClient(c.get());
			c.get().getOrders().add(order);
			viewOrder = order;
			this.clientRepo.save(c.get());
			this.orderRepo.save(order);
		}
		order.getClient().setOrders(null);
		viewOrder.setClient(order.getClient());
		
		return "Resultado: \"Orden generada\" \n" + viewOrder;
	}

	@PutMapping(value = "/updateCantProd/{id_cliente}/{id_item}")
	public Object updateCantProd(@PathVariable("id_cliente") int id_cliente,
			@PathVariable("id_item") int id_item,
			@RequestBody Order order) {
		
		Optional<Client> client = this.clientRepo.findById(id_cliente).filter(c -> c.getStatus().equals("Activo"));
		
		if(client.isPresent() && this.orderRepo.findById(id_item).isPresent()) {
			return this.orderRepo.findById(id_item).map(record -> {
				record.setCantProd(order.getCantProd());
				record.setTotalOrder(record.getCost()*order.getCantProd());	
				Order oUpdated = this.orderRepo.save(record);
				oUpdated.getClient().setOrders(null);
				return ResponseEntity.ok().body("Modificado\n\n" + oUpdated );
			}).orElse(ResponseEntity.notFound().build());
		}
		
		return "Cliente no se encuentra registrado o su orden no existe";
		
	}
	
	@PutMapping(value = "/updateTypeProd/{id_cliente}/{id_item}")
	public Object updateTypeProd(@PathVariable("id_cliente") int id_cliente,
			@PathVariable("id_item") int id_item, 
			@RequestBody Order order) {
		
		Optional<Client> client = this.clientRepo.findById(id_cliente).filter(c -> c.getStatus().equals("Activo"));

		if(client.isPresent() && this.orderRepo.findById(id_item).isPresent()) {
			
			 this.orderRepo.findById(id_item).map(record -> {
				record.setTypeProduct(order.getTypeProduct().toLowerCase());
				int cost;
				switch(record.getTypeProduct()) {
				case "tasa":
					cost = 15;
				case "camiseta":
					cost = 9;
					break;
				case "almohadon":
					cost = 8;
					break;
				case "vaso":
					cost = 13;
					break;
					default:
						return "Tipo de producto invalido";
				}
				
				record.setCost(cost);
				record.setTotalOrder(cost*record.getCantProd());			
				Order oUpdated = this.orderRepo.save(record);
				record.getClient().setOrders(null);
				return ResponseEntity.ok().body("Modificado\n\n" + oUpdated );
			}).orElse(ResponseEntity.notFound().build());
			
			
		}
		
		return "Cliente no se encuentra registrado o su orden no existe";
		
	}

}
