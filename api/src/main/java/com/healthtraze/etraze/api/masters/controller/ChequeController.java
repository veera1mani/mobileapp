package com.healthtraze.etraze.api.masters.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.dto.ChequeDTO;
import com.healthtraze.etraze.api.masters.dto.ChequeData;
import com.healthtraze.etraze.api.masters.dto.ChequeDetailDTO;
import com.healthtraze.etraze.api.masters.dto.ChequeDropDownDTO;
import com.healthtraze.etraze.api.masters.dto.PaginationDTO;
import com.healthtraze.etraze.api.masters.dto.StockistChequeDetail;
import com.healthtraze.etraze.api.masters.model.Bank;
import com.healthtraze.etraze.api.masters.model.Cheque;
import com.healthtraze.etraze.api.masters.model.ChequeCardView;
import com.healthtraze.etraze.api.masters.model.ChequeStatusHistory;
import com.healthtraze.etraze.api.masters.service.ChequeService;

@RestController
public class ChequeController implements BaseCrudController<Cheque, Long> {

    private final ChequeService chequeService;  
    
    @Autowired
    public ChequeController(ChequeService chequeService) {
        this.chequeService = chequeService;
    }

    @Override
    @GetMapping(value = "/cheques")
    public List<Cheque> findAll() {
        return chequeService.findAll();
    }
    
   
    
    
    @GetMapping(value = "/stockist-cheque-details/{stockistId}/{sapId}")
    public StockistChequeDetail getStockistChequeDetail(@PathVariable(name = "stockistId") String stockistId,@PathVariable(name = "sapId") String sapId) {
        return chequeService.getStockistChequeDetail(stockistId,sapId);
    }
    
    @GetMapping(value = "/stockist-details/{stockistId}/{sapId}")
    public Result<ChequeDTO> getStockistsCheques(@PathVariable(name = "stockistId") String stockistId,@PathVariable(name = "sapId") String sapId) {
        return chequeService.getStockistsCheques(stockistId,sapId);
    }
    
    @GetMapping("/advance-cheques/{stockistId}/{sapId}")    
    public Result<Object> findAdvanceCheques(@PathVariable(name = "stockistId") String stockistId,@PathVariable(name = "sapId") String sapId,
    		@RequestParam("page") int page,@RequestParam("search") String search) {
        
    	return chequeService.findAdvanceCheques(stockistId , sapId , page,search);
    }
    
    @GetMapping("/security-cheques/{stockistId}/{sapId}")    
    public Result<Object> findSecurityCheques(@PathVariable(name = "stockistId") String stockistId,@PathVariable(name = "sapId") String sapId,
    		@RequestParam("page") int page,@RequestParam("search") String search) {
        
    	return chequeService.findSecurityCheques(stockistId , sapId , page, search);
    }
    
    @GetMapping("/cancelled-cheques/{stockistId}/{sapId}")    
    public Result<Object> findCancelledCheques(@PathVariable(name = "stockistId") String stockistId,@PathVariable(name = "sapId") String sapId,
    		@RequestParam("page") int page,@RequestParam("search") String search) {
        
    	return chequeService.findCancelledCheques(stockistId , sapId , page, search);
    }
    
    @GetMapping("/outward-cheques/{stockistId}/{sapId}")    
    public Result<Object> findOutwardCheques(@PathVariable(name = "stockistId") String stockistId,@PathVariable(name = "sapId") String sapId,
    		@RequestParam("page") int page,@RequestParam("search") String search) {
        
    	return chequeService.findOutwardCheques(stockistId , sapId , page,search);
    }
    
    

    @Override
    @GetMapping(value = "/cheque/{id}")
    public Cheque findById(@PathVariable Long id) {
        return chequeService.findById(id);
    }
    
    @GetMapping(value = "/cheque-status-id/{id}")
    public List<ChequeStatusHistory> findByChequeId(@PathVariable Long id) {
        return chequeService.findByChequeId(id);
    }
    
    @GetMapping(value = "/all-cheque")
    public  Map<String,Object> getAllChequeNumberByUser(@RequestParam(required = false) String number) {
        return chequeService.getAllChequeNumberByUser(number);
    }
    
    @GetMapping(value = "/cheque-details-id/{id}")
    public Result<ChequeDetailDTO> findChequDetails(@PathVariable Long id) {
        return chequeService.findChequDetails(id);
    }
    
    @GetMapping(value = "/cheque-status-chequenumber/{id}")
    public List<ChequeStatusHistory> findByChequeNumber(@PathVariable String id) {
        return chequeService.findByChequeNumber(id);
    }
    
    @GetMapping(value = "/cheque-number/{id}")
    public List<Cheque> findChequeByChequeNumber(@PathVariable String id) {
        return chequeService.findChequeByChequeNumber(id);
    }

    @Override
    @PostMapping(value = "/cheque")
    public Result<Cheque> create(@RequestBody Cheque t) {
        return chequeService.create(t);
    }
    
    
    @PostMapping(value = "/cheque-outward")
    public Result<Cheque> createOutward(@RequestBody Cheque t) {
        return chequeService.createOutward(t);
    }

    @PostMapping(value = "/create_inword_cheque")
    public Result<ChequeData> createBulkCheque(@RequestBody ChequeData t) {
        t.setStatus("INWARD");
        return chequeService.createCheques(t);
    }

    @PostMapping(value = "/create_security_cheque")
    public Result<ChequeData> createBulkSecurityCheque(@RequestBody ChequeData t) {
        t.setStatus("SECURITY");
        return chequeService.createCheques(t);
    }

    @Override
    @PutMapping(value = "/cheque")
    public Result<Cheque> update(@RequestBody Cheque t) {
        return chequeService.update(t);
    }
    
    
    @PutMapping(value = "/cheque-cancel-return")
    public Result<Cheque> chequeCancelAndReturn(@RequestBody Cheque t) {
        return chequeService.chequeCancelAndReturn(t);
    }
    
    
    @PutMapping(value = "/cheque-hold")
    public Result<Cheque> chequeHold(@RequestBody Cheque t) {
        return chequeService.chequeHold(t);
    }
    
 
    @PutMapping(value = "/cheque-unhold")
    public Result<Cheque> unHold(@RequestBody Cheque t) {
        return chequeService.unHold(t);
    }
    
  
    @PutMapping(value = "/edit-cheque")
    public Result<Cheque> editCheque(@RequestBody Cheque t){
        return chequeService.editCheque(t);
    }
    

    @Override
    @DeleteMapping(value = "/cheque/{id}")
    public Result<Cheque> delete(@PathVariable Long id) {
        return chequeService.delete(id);
    }

    @GetMapping(value = "/cheques/{stockistId}/{sapId}/{status}")
    public List<Cheque> getChequeByStatus(@PathVariable(value = "stockistId") String stockistId,
            @PathVariable(value = "sapId") String sapId,
            @PathVariable(value = "status") String status) {
        return chequeService.getChequeByStatus(stockistId, sapId,status);
    }
    
    
    @GetMapping(value = "/cheque/{stockistId}/{sapId}/{status}")
    public Bank getSecurituChequeBank(@PathVariable(value = "stockistId") String stockistId,
            @PathVariable(value = "sapId") String sapId,
            @PathVariable(value = "status") String status) {
        return chequeService.getSecurituChequeBank(stockistId, sapId);
    }

    @GetMapping(value = "/stockist-cheques")
    public Result<HashMap<String, Object>> getChequeByStockist(@RequestParam("page") int page, @RequestParam("sortBy") String  sortBy,@RequestParam("sortDir") String sortDir,
            
            @RequestParam("stockist") String  stockist,
            @RequestParam("location") String  location,
            @RequestParam("sapId") String  sapId,
            @RequestParam("manufacturer") String  manufacturer,
            @RequestParam("Status") String  status){
    
    PaginationDTO paging = new PaginationDTO(manufacturer, sortBy, sortDir, page, status);	
        return chequeService.getCheque(paging,stockist, location, sapId);
    }
    
    
    
    @GetMapping(value = "/check-card-view")
    public ChequeCardView getChequeCardView(@RequestParam("Status") String  status) {
        return chequeService.getChequeCardView(status);
    }
    
    @GetMapping(value = "/check-cheques")
    public Result<String> checkCheques(@RequestParam String chequeNumber){
        return chequeService.checkChequeNumberAlerdyExist(chequeNumber);
    }
    
    @PostMapping("/cheque/fileReader/{sheetIndex}")
    public Result<Object> fileReader(@RequestPart(name = "file" , required = true) 
    MultipartFile file , @PathVariable() String sheetIndex) throws IOException  {
        if(!file.getOriginalFilename().endsWith(".xlsx")) {
            return null;
        }
            return chequeService.fileReader(file , sheetIndex);
    }
    
    @GetMapping(value = "/diff-security")
    public Result<Object> securityMail() {
        return chequeService.differentInward();
    }
    
    @GetMapping(value = "/cheque-limit")
    public int getLimit(){
        return chequeService.getLimit();
    }
    
    @PutMapping(value = "/cheque-limit/{limit}")
    public int setLimit( @PathVariable int limit){
        return chequeService.setLimit(limit);
    }

}