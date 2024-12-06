package com.healthtraze.etraze.api.masters.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.dto.OrderTransporterDTO;
import com.healthtraze.etraze.api.masters.dto.TransporterTicketDTO;
import com.healthtraze.etraze.api.masters.model.OrderTransporterMapping;
import com.healthtraze.etraze.api.masters.model.Ticket;
import com.healthtraze.etraze.api.masters.model.Transport;
import com.healthtraze.etraze.api.masters.service.TransportService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@EnableAutoConfiguration
public class TransportController implements BaseCrudController<Transport, String> {

	private TransportService transportService;

	@Autowired
	public TransportController(TransportService transportService) {
		super();
		this.transportService = transportService;
	}

	@GetMapping(value = "/transportss")
	public Result<HashMap<String, Object>> findAllUser(@RequestParam int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(required = false) String value,
			@RequestParam(required = false) String sortBy, @RequestParam String sortDir) {

		return transportService.findAllTransport(page, value, sortBy, sortDir);
	}

	@Override
	@GetMapping(value = "/transports")
	public List<Transport> findAll() {
		return transportService.findAll();
	}

	@Override
	@GetMapping(value = "transport/{id}")
	public Transport findById(@PathVariable("id") String id) {

		return transportService.findById(id);
	}

	@GetMapping(value = "transport-order")
	public Map<String, String> findTransporterOrdersCount() {
		return transportService.findTransporterOrdersCount();
	}

	@GetMapping(value = "/transporter-orders")
	public List<Ticket> findTransporterOrders() {
		return transportService.findTransporterOrders();
	}

	@GetMapping(value = "/tenant-transporter-orders")
	public List<TransporterTicketDTO> findTenantTransporterOrders() {
		return transportService.findTenantTransporterOrders();
	}

	@Override
	@PostMapping(value = "/transport")
	public Result<Transport> create(@org.springframework.web.bind.annotation.RequestBody Transport t) {
		return transportService.create(t);
	}

	@Override
	@PutMapping(value = "/transport")
	public Result<Transport> update(@RequestBody Transport transport) {

		return transportService.update(transport);
	}

//	@Override
//	@DeleteMapping(value="/transport/{id}")
//	public Result<Transport> delete(@PathVariable("id") String id) {
//
//		return transportService.delete(id);
//	}

	@Override
	@DeleteMapping("/transport/{transport_id}")
	public Result<Transport> delete(@PathVariable("transport_id") String transportId) {
		return transportService.delete(transportId);
	}

	@PostMapping("/order-transporter")
	public Result<OrderTransporterMapping> orderTransporter(@RequestBody OrderTransporterDTO orderTransporterMapping) {
		return transportService.orderTransporter(orderTransporterMapping);
	}

}
