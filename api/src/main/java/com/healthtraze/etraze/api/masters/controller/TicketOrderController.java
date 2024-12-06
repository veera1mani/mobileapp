package com.healthtraze.etraze.api.masters.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.file.model.FileResponse;
import com.healthtraze.etraze.api.masters.dto.InvoiceDetails;
import com.healthtraze.etraze.api.masters.dto.InvoicedItemsDto;
import com.healthtraze.etraze.api.masters.dto.OrderDTO;
import com.healthtraze.etraze.api.masters.dto.OrderMobileDTO;
import com.healthtraze.etraze.api.masters.dto.OrderTransporterDTO;
import com.healthtraze.etraze.api.masters.dto.TicketInvoiceDTO;
import com.healthtraze.etraze.api.masters.dto.TicketOrderDTO;
import com.healthtraze.etraze.api.masters.dto.TicketReportsDTO;
import com.healthtraze.etraze.api.masters.model.TicketOrder;
import com.healthtraze.etraze.api.masters.model.TicketOrderInvoice;
import com.healthtraze.etraze.api.masters.service.TicketOrderService;

@RestController
public class TicketOrderController implements BaseCrudController<TicketOrder,String>{
    
  
    private final TicketOrderService ticketOrderService;
    
    
    @Autowired
	public TicketOrderController(TicketOrderService ticketOrderService) {
		this.ticketOrderService = ticketOrderService;
	}

	@Override
	public List<TicketOrder> findAll() {
		
		return Collections.emptyList();
	}
	
	@GetMapping(value = "/all-order-invoices")
	public List<TicketOrderInvoice> findAllTicketOrderInvoice() {
		return ticketOrderService.findAllTicketOrderInvoice();
	}
	
	
	@GetMapping(value = "/all-order")
	public List<TicketReportsDTO> findAllTicketOrder(@RequestParam String search) {
		return ticketOrderService.findAllTicketOrder(search);
	}
	
	@GetMapping(value = "/transporter-assigned-order")
	public List<OrderTransporterDTO> getAllTransporterAssignedOrders(@RequestParam String search) {
		return ticketOrderService.getAllTransporterAssignedOrders(search);
	}
	
	
	
	@GetMapping(value = "/all-packed-order")
	public HashMap<String , Object> findAllPackedOrder(@RequestParam String search,@RequestParam String location) {
		return ticketOrderService.findAllPackedOrder(search,location);
	}
	
	@GetMapping(value = "/all-order-qr")
	public HashMap<String, Object> findAllTicketOrderQR(@RequestParam(required = false) String search,@RequestParam(required = false) int page,@RequestParam(required = false) String  sortBy,@RequestParam(required = false) String sortDir) {
		return ticketOrderService.findAllTicketOrderQr(search,page,sortBy,sortDir);
	}

    @Override
    public TicketOrder findById(String id) {
        return null;
    }

    @Override
    public Result<TicketOrder> create(TicketOrder t) {
        return null;
    }

    @Override
    public Result<TicketOrder> update(TicketOrder t) {
        return null;
    }

	@Override
	public Result<TicketOrder> delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GetMapping(value = "/ticket-order-invoices/{id}")
	public Result<TicketOrderDTO> findTicketOrders(@PathVariable("id") String id) {		
		return ticketOrderService.findTicketOrders(id);
	}
	
	@PostMapping(value = "/ticket-order-invoice")
	public Map<String,Object> findTicketOrders(@RequestBody OrderMobileDTO t) {		
		return ticketOrderService.findTicketOrderInvoice(t);
	}
	
	@ResponseBody
	@PutMapping(value = "/update-invoice")
	public Result<TicketOrderInvoice> updateTicketOrderInvoice(@RequestBody TicketOrderInvoice t) {
		return ticketOrderService.updateTicketOrderInvoice(t);
	}
	
	@PutMapping(value = "/splice-merged-order")
	public Result<TicketReportsDTO> spliceMergedInvoice(@RequestBody TicketReportsDTO t) {
		return ticketOrderService.spliceMergedInvoice(t);
	}
	
	
	@ResponseBody
	@PutMapping("/picked-list")
    public List<TicketOrderInvoice> createPickedList(@RequestBody List<TicketOrderInvoice> toi) {
        return ticketOrderService.createPickedList(toi);
    }
	
	@ResponseBody
	@PutMapping(value = "/edit-invoice")
	public Result<TicketOrderInvoice> editInvoice(@RequestBody TicketInvoiceDTO t) {
		return ticketOrderService.editInvoice(t);
	}
	
	@GetMapping(value = "/ticket-order-invoiced")
	public Result<List<TicketOrderInvoice>> findInvoicedList(@RequestParam("search") String  search) {		
		return ticketOrderService.findInvoicedList(search);
	}
	
	@GetMapping(value = "/ticket-order-picked")
	public List<List<TicketOrderInvoice>> findPickedList(@RequestParam("search") String  search) {		
		return ticketOrderService.findPickedList(search);
	}
	
	@GetMapping(value = "/ticket-pod-order")
	public List<List<TicketOrderInvoice>> findPodList(@RequestParam("search") String  search) {		
		return ticketOrderService.findPodList(search);
	}
	
	@GetMapping(value = "/pod-order-list")
	public List<TicketOrderInvoice> getPodRequiredList(@RequestParam("search") String  search) {		
		return ticketOrderService.getPodRequiredList(search);
	}
	
	
	@GetMapping(value = "/ticket-order-dispatched")
	public Result<List<TicketOrderInvoice>> findDispatchedList(@RequestParam("search") String  search) {		
		return ticketOrderService.findDispatchedList(search);
	}
	
	@GetMapping(value = "/order-mobile")
	public List<OrderMobileDTO> allOrderMobile(@RequestParam("status") String status) {		
		return ticketOrderService.allOrderMobile(status);
	}
	
	
	@PutMapping("/picked-list-add")
    public List<TicketOrderInvoice> addPickedList( @RequestParam("id") String id, @RequestBody List<TicketOrderInvoice> toi) {
        return ticketOrderService.addPickedList(id,toi);
    }
    
    
    @PutMapping("/picked-list-remove")
    public Result<TicketOrderInvoice> removePickedList(@RequestParam("ticketId") String ticketId,@RequestParam("invoice") String invoice) {
        return ticketOrderService.removePickedList(ticketId , invoice);
    }
    
    @GetMapping(value = "/lr-documents")
    public List<TicketInvoiceDTO> getLrDocumets(@RequestParam("ticketId") String ticketId) {        
        return ticketOrderService.getLrDocumets(ticketId);
    }
    
    @GetMapping(value = "/no-of-cases")
    public Result<Object> getNumOfCase(@RequestParam("packId") String packId) {     
        return ticketOrderService.getNumOfCase(packId);
    }
    
    @PostMapping("/order/fileReader/{sheetIndex}")
    public Result<Object> fileReader(@RequestPart(name = "file" , required = true)
    MultipartFile file , @PathVariable() String sheetIndex) throws IOException  {
        if(!file.getOriginalFilename().endsWith(".xlsx")) {
            return null;
        }
            return ticketOrderService.fileReader(file , sheetIndex);
    }
    
    @RequestMapping(value = "/upload-invoices", headers = "content-type=multipart/*", method = RequestMethod.POST)
	public Result<Map<String,Object>> uploadInvoiceFile(@RequestParam("file") MultipartFile file) {
    	if(file.getOriginalFilename().endsWith(".xlsx")) {
    		return ticketOrderService.storeInvoiceFileInAwsXlxs(file);
        } else if(file.getOriginalFilename().endsWith(".csv")) {
    		return ticketOrderService.storeInvoiceFileInAwsCsv(file);
        }
    	return null;
	}
    
    @RequestMapping(value = "/upload-invoices-id", headers = "content-type=multipart/*", method = RequestMethod.POST)
   	public Result<Map<String,Object>> uploadInvoiceFileTenant(@RequestParam("file") MultipartFile file ,@RequestParam("id") String manfId) {
       	if(file.getOriginalFilename().endsWith(".xlsx")) {
       		return ticketOrderService.storeInvoiceFileInAwsXlxsTenant(file,manfId);
           } else if(file.getOriginalFilename().endsWith(".csv") || file.getOriginalFilename().endsWith(".CSV")) {
       		return ticketOrderService.storeInvoiceFileInAwsCsvTenant(file,manfId);
           }
       	return null;
   	}
       
    @GetMapping(value = "/cartons-details")
    public List<InvoiceDetails> findCartonDetails(@RequestParam String id) {
        return ticketOrderService.findCartonDetails(id);
    }
    
    @GetMapping(value = "/getpickedinvoice")
    public Result<List<InvoicedItemsDto>> getInvoicesByTenant() {
        return ticketOrderService.getInvoicesByTenant();
    }
    
//    @GetMapping(value = "/get-delivery-merge")
//    public List<OrderDTO> getDispatchedByPackId(@RequestParam String id) {
//        return ticketOrderService.getDispatchedByPackId(id);
//    }
//    
//    @PostMapping(value = "/set-delivery-merge")
//    public Result<List<TicketReportsDTO>> createDispatchedByPackId(@RequestBody List<TicketReportsDTO> dto) {
//        return ticketOrderService.createDispatchedByPackId(dto);
//    }
//    
//    @PutMapping(value = "/update-delivery-merge")
//    public Result<List<TicketReportsDTO>> updateDispatchedByPackId(@RequestParam("id") String id , @RequestBody List<TicketReportsDTO> dto){
//    	return ticketOrderService.updateDispatchedByPackId(id ,dto);
//    }
//    
//    @PutMapping(value = "/remove-delivery-merge")
//    public Result<TicketReportsDTO> removeDispatchedByPackId(@RequestBody TicketReportsDTO dto){
//    	return ticketOrderService.removeDispatchedByPackId(dto);
//    }
}