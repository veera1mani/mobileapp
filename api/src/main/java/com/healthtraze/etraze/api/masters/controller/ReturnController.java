package com.healthtraze.etraze.api.masters.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.dto.ReturnDetails;
import com.healthtraze.etraze.api.masters.dto.ReturnQrDTO;
import com.healthtraze.etraze.api.masters.model.Return;
import com.healthtraze.etraze.api.masters.model.ReturnNote;
import com.healthtraze.etraze.api.masters.service.ReturnService;


@Controller
public class ReturnController implements BaseCrudController<Return, String>{
	
	
	private final ReturnService returnService;
	
	@Autowired
	public ReturnController(ReturnService returnService) {
		this.returnService = returnService;
	}

	@GetMapping("/returns")
	@Override
	public List<Return > findAll() {
		return returnService.findAll();
	}
	
	@GetMapping("/returns-id")
	public List<Object[]> findAllReturnId() {
		return returnService.findAllReturnId();
	}
	
	@GetMapping("/returns-mobile")
	public List<Return > mobLogin(@RequestParam("status") String status,@RequestParam("type") String type) {
		return returnService.mobLogin(status,type);
	}
	
	@GetMapping("/all-returns")
	public List<ReturnQrDTO > findAllReturns(@RequestParam("search") String search) {
		return returnService.findAllReturns(search);
	}
	
	@GetMapping("/serial-number")
	public Result<String> serialNumber() {
		return returnService.serialNumber();
	}
	
	@GetMapping("/returnss")
	public Result<HashMap<String, Object>> getAllReturns(@RequestParam("page") int page,@RequestParam("sortBy") String  sortBy,@RequestParam("sortDir") String sortDir,@RequestParam("search") String search,@RequestParam("status") String status ) {
		return returnService.getAllReturnsByUser(page, sortBy, sortDir,search,status);
	}
	

	@GetMapping("/return/{id}")
	@Override
	public Return findById(@PathVariable String id) {
		return returnService.findById(id);
	}
	
	@GetMapping("/return-details/{id}")
	public ReturnDetails findReturnDetails(@PathVariable String id) {
		return returnService.findReturnDetails(id);
	}
	
	@GetMapping("/all-return-document")
	public List<Return> findAllReturnDetails() {
		return returnService.findAllReturnDetails();
	}
	
	
	
	

	@PostMapping("/return")
    @Override
    public Result<Return> create(@RequestBody Return t) {
        return returnService.create(t);
    }

	
	
    @PutMapping("/return-claim")
    public Result<Return> createReturn(@RequestBody Return t) {
        return returnService.createReturn(t);
    }
    
    
    @PutMapping("/return")
    @Override
    public Result<Return> update(@RequestBody Return t) {
        return returnService.update(t);
    }
    
    @PutMapping("/update-return-physical")
    public Result<Return> updatePhysicalChecks(@RequestParam String type,@RequestBody Return t) { 
        if(type.equals("Saleable")) {
    		return returnService.updatePhysicalChecksSalable(t);
    	}
    	else if (type.equals("NonSaleable")) {
    		return returnService.updatePhysicalChecksNonSalable(t);
		}
    	else if (type.equals("Both")) {
    		return returnService.updatePhysicalChecksBoth(t);
		}
        
    	return new Result<>();
    }
    
    @PutMapping("/update-return-physical-web")
    public Result<Return> updatePhysicalChecksWeb(@RequestBody Return t) {
        return returnService.updatePhysicalChecksWeb(t);
    }
    
    @PutMapping("/update-return-received-web")
    public Result<Return> updatePhysicalReturnsWeb(@RequestBody Return t) {
        return returnService.updatePhysicalReturnsWeb(t);
    }
    
    
    @PutMapping("/update-return-note")
    public Result<Return> updateReturnNote(@RequestBody List<ReturnNote> returnNotes) {
        return returnService.updateReturnNote(returnNotes);
    }
    
    @PutMapping("/update-return-notes")
    public Result<Return> updateReturn(@RequestParam String type,@RequestBody List<ReturnNote> returnNotes) {
    	if(type.equals("Saleable")) {
    		return returnService.updateReturnSalable(returnNotes);
    	}
    	else if (type.equals("NonSaleable")) {
    		return returnService.updateReturnNonSalable(returnNotes);
		}
    	return new Result<>();
        
    }
    

	@DeleteMapping("/return/{id}")
	@Override
	public Result<Return> delete(@PathVariable String id) {
		return returnService.delete(id);
	}
	
    @GetMapping("/transport-stockist")
	public Result<Object> getAllTransportStockist(){
	
	return returnService.getAllTransportStockist();
    }
	
    @PostMapping("/return/fileReader/{sheetIndex}")
    public Result<Object> fileReader(@RequestPart(name = "file" , required = true)
    MultipartFile file , @PathVariable() String sheetIndex) throws IOException  {
        if(!file.getOriginalFilename().endsWith(".xlsx")) {
            return null;
        }
            return returnService.fileReader(file , sheetIndex);
    }
	
}
