package com.healthtraze.etraze.api.masters.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.dto.OrderDTO;
import com.healthtraze.etraze.api.masters.dto.OrderSummary;
import com.healthtraze.etraze.api.masters.dto.TicketDTO;
import com.healthtraze.etraze.api.masters.dto.TicketDetails;
import com.healthtraze.etraze.api.masters.dto.TicketStatus;
import com.healthtraze.etraze.api.masters.model.ManagerManufacturerMapping;
import com.healthtraze.etraze.api.masters.model.Ticket;
import com.healthtraze.etraze.api.masters.model.TicketStatusHistory;
import com.healthtraze.etraze.api.masters.repository.TicketRepository;
import com.healthtraze.etraze.api.masters.service.TicketReportService;
import com.healthtraze.etraze.api.masters.service.TicketService;

@Controller
public class TicketController implements BaseCrudController<Ticket, String> {

    private final TicketService ticketService;
    
    
    private  final TicketRepository ticketRepository;
    
    private final  TicketReportService ticketreport;


 @Autowired
    public TicketController(TicketService ticketService, TicketRepository ticketRepository,
			TicketReportService ticketreport) {
		super();
		this.ticketService = ticketService;
		this.ticketRepository = ticketRepository;
		this.ticketreport = ticketreport;
	}

	
    @GetMapping("/tickets")
    @Override
    public List<Ticket> findAll() {
        return ticketService.findAll();
    }
    
    @GetMapping("/order-id")
    public List<Map<String,Object>> findAllOrderId() {
        return ticketService.findAllOrderId();
    }
    
    @GetMapping("/tickets-id")
    public List<String> findAllTicketId() {
        return ticketService.findAllTicketId();
    }
    
   
    
    @GetMapping("/ticket/{id}")
    @Override
    public Ticket findById(@PathVariable String id) {
        return ticketService.findById(id);
    }
    
    @GetMapping("/ticket-details/{id}")
    public Result<TicketDetails> findTicketById(@PathVariable String id) {
        return ticketService.findTicketById(id);
    }

    @PostMapping("/ticket")
    @Override
    public Result<Ticket> create(@RequestBody Ticket t) {
        return ticketService.create(t);
    }
    @PostMapping("/ticket-manual")
    public Result<Ticket> createManual(@RequestBody Ticket t) {
        return ticketService.createManual(t);
    }
    
    @PostMapping("/order")
    public Result<OrderDTO> createOrder(@RequestBody OrderDTO t) {
        return ticketService.creatOrder(t);
    }
    
    @PostMapping("/add-invoice")
    public Result<OrderDTO> addingInvoice(@RequestBody OrderDTO t) {
        return ticketService.addingInvoice(t);
    }
    
    @PutMapping("/save-invoice")
    public Result<Object> uploadTenant(@RequestParam("invoice") String inv) {
        return ticketService.uploadTenant(inv);
    }
    

    @PutMapping("/ticket")
    @Override
    public Result<Ticket> update(@RequestBody Ticket t) {
        return ticketService.update(t);
    }
    
    
    @PutMapping("/ticket/{tenantId}")
    public Result<Ticket> updateStatus(@PathVariable String tenantId, @RequestBody TicketStatus t) {
        return ticketService.updateStatus(tenantId, t);
    }
    
    @PutMapping("add-tickets")
     public Result<Ticket> addTickets(@RequestParam String ticketId,@RequestParam int count){   
        return ticketService.addTickets(ticketId, count);
    }
    @DeleteMapping("/ticket/{id}")
    @Override
    public Result<Ticket> delete(@PathVariable String id) {
        return ticketService.delete(id);
    }

    @PutMapping("/update-ticket")
    public Result<Ticket> updatePhysicalChecks(@RequestBody Ticket t) {
        return ticketService.updateTicket(t);
    }
    
    @PutMapping("/update-ticket-status")
    public Result<Ticket> updateTicketStatus(@RequestBody Ticket t) {
        return ticketService.updateTicketStatus(t);
    }
    
    @PutMapping("/update-ticket-multiple")
    public Result<Ticket> updatePhysicalChecksMultiple(@RequestBody List<Ticket> t) {
        return ticketService.updateTicketMultiple(t);
    }
    
    @PutMapping("/update-status")
    public Result<Ticket> updateOrder(@RequestBody OrderDTO t) {
        return ticketService.updateOrderNonProrities(t);
    }
    
    @PutMapping("/assign-transporter")
    public Result<Ticket> assignTransporter(@RequestBody OrderDTO t) {
        return ticketService.assignTransporter(t);
    }
    
    @PutMapping("/update-status-priority")
    public Result<Ticket> updateOrderProrities(@RequestBody OrderDTO t) {
        return ticketService.updateOrderProrities(t);
    }
    
    @PutMapping("/update-order-dispatch")
    public Result<Ticket> updateOrderDispatch(@RequestBody OrderDTO t) {
        return ticketService.updateOrderDispatch(t);
    }
    
    @PutMapping("/reassign-ticket")
    public Result<Ticket> reassignTicket(@RequestBody Ticket t) {
        return ticketService.reassignTicket(t);
    }
    
    @PutMapping(value="/block-ticket")
    public Result<Ticket> blockTicket(@RequestParam String ticketId,@RequestParam String remarks,
    		@RequestParam(required = false) String stockistId){
        return ticketService.blockTicket(ticketId,remarks,stockistId);
    }
    
    @PutMapping(value="/cancel-ticket")
    public Result<Ticket> cancelTicket(@RequestParam String ticketId,@RequestParam String remarks){
        return ticketService.cancelTicket(ticketId,remarks);
    }
    
    @PutMapping("/assign-ticket")
    public Result<Ticket> assignTicket(@RequestBody Ticket t) {
        return ticketService.assignTicket(t);
    }

    @GetMapping(value = "/ticketss")
    public Result<HashMap<String, Object>> findAllUser(@RequestParam int page,
            @RequestParam(required = false) String value,@RequestParam(required = false) String status, @RequestParam(required = false) String sortBy ,
            @RequestParam String sortDir) {
        
        return ticketService.getAllTicket(page, value,status,sortBy,sortDir);
    }
    
    @GetMapping(value = "/orders")
    public List<Ticket> findAllOrders() {       
        return ticketRepository.getAllOrder();
    }
    
    @GetMapping(value = "/manager-manufacture")
    public List<ManagerManufacturerMapping> findAllMangerManufacture() {     
        return ticketService.getAllMangerManufacture();
    }
    
    @PutMapping(value="/audit-remarks")
    public List<TicketStatusHistory> updateTicketWithRemarks(@RequestParam String ticketId,@RequestParam String status,@RequestParam String remarks){
        return ticketService.auditRemarks(ticketId,status,remarks);
    }
    
    @GetMapping("/order-summary")
    public OrderSummary orderSummary() {
        return ticketService.orderSummary();
    }
    
    @GetMapping(value = "/ticketsByOrder")
    public Result<HashMap<String, Object>> getAllOrder(@RequestParam int page,
             @RequestParam(required = false) String value,@RequestParam(required = false) String status, @RequestParam(required = false) String sortBy ,
            @RequestParam String sortDir) {
        
        return ticketService.getAllOrders(page,value,status,sortBy,sortDir);
    }
    

    
    @GetMapping("/orders-app")    
    public List<Ticket> findAllOrdersApp() {
        return ticketService.findAllOrders();
    }
    @GetMapping(value="/userlistReport")
    public List<TicketDTO> findUserListReport(){
        return ticketreport.findUserListReport();
    }

    @GetMapping("/transport-orders")    
    public Result<HashMap<String, Object>> findTransportOrders(@RequestParam("search") String search, @RequestParam("page") int page,
    		@RequestParam(required = false) String sortDir,@RequestParam(required = false) String sortBy, @RequestParam(required = false) String status,
    		@RequestParam(required = false) String stockist,@RequestParam(required = false) String manufacturer,@RequestParam(required = false) String trans) {
        
    	return ticketService.findTransportOrders(search,page, sortDir , sortBy,status,stockist, manufacturer,trans);
    }
    @GetMapping("/ticket-invoice/{id}")
    public List<TicketStatusHistory> findTicketByIdS(@PathVariable String id) {
        return ticketService.findTicketByIds(id);
    }
    
    @GetMapping("/find-login-ticket")
    public Result<Ticket> findBylogin(@RequestParam String id) {
        return ticketService.findBylogin(id);
    }

    
    @GetMapping("/find-eml")
    public Result<Object> findTicketsEml(@RequestParam String id) {
        return ticketService.findTicketsEml(id);
    }
}