package com.healthtraze.etraze.api.masters.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.healthtraze.etraze.api.base.controller.BaseCrudController;
import com.healthtraze.etraze.api.base.model.Result;
import com.healthtraze.etraze.api.masters.dto.TenantDTO;
import com.healthtraze.etraze.api.masters.model.Tenant;
import com.healthtraze.etraze.api.masters.model.TenantManufacture;
import com.healthtraze.etraze.api.masters.service.TenantService;
import com.healthtraze.etraze.api.security.model.User;

@RestController
public class TenantController implements BaseCrudController<Tenant, String> {
    

    
	
    private final TenantService tenantService;
    
    public TenantController(TenantService tenantService) {
    	this.tenantService=tenantService;
    }
    
//tenantcontroller
//tenantcontroller
    

    @Override
    @GetMapping(value = "/tenants")
    public List<Tenant> findAll() {
            return tenantService.findAll();
    }
    
    
    @GetMapping(value = "/tenantss")
    public Page<Tenant> findAll(@RequestParam int page, @RequestParam(required = false) String search) {
            return tenantService.findAll(page, search);
    }
    
    @GetMapping(value = "/update-tenant-manufacturer")
    public Result<TenantManufacture> updateAllManufacturer() {
        
            return tenantService.updateAllManufacturer();
    }

    @Override
    @GetMapping(value = "/tenant/{id}")
    public Tenant findById(@PathVariable("id") String id) {
        
    	return tenantService.findById(id);
    }
    
    @GetMapping(value = "/tenant-details")
    public Tenant findById() {
        
    	return tenantService.findById();
    }
    
    @GetMapping(value = "/get-login-tenant")
    public Tenant findByLogin() {
    	return tenantService.findByLogin();
    }
    
     
    @Override
    @PostMapping(value = "/tenant")
    @ResponseBody
    public Result<Tenant> create(@RequestBody Tenant t) {
        return tenantService.create(t);
    }

    
    
    @Override
    @PutMapping(value = "/tenant")
    @ResponseBody
    public Result<Tenant> update(@RequestBody Tenant t) {
        return tenantService.update(t);
    }
    
    
    
    @PutMapping("/tenantt")
    @ResponseBody
    public Result<User> updateTenantManufacture(@RequestBody List<TenantManufacture> tenantManufactures) {
        return tenantService.updateTenantManufacture(tenantManufactures);
    }
    
    
    
    @PutMapping("/tenant-status-update/{tenantId}")
    @ResponseBody
    public Result<Tenant> updateTenantStatus(@PathVariable String tenantId ) {
        return tenantService.updateTenantStatus(tenantId);
    }
    
    
    
    

    @Override
    @DeleteMapping(value = "/tenant/{id}")
    public Result<Tenant> delete(@PathVariable("id") String id) {
        return tenantService.delete(id);
    }
    
    
    @PutMapping(value = "/tenant-tenant-manufacture")
    public Result<TenantDTO> updateTenantAndTenantManufacture(@RequestBody TenantDTO t){
        return tenantService.updateTenantAndTenantManufacture(t);
    }
    
    @GetMapping(value = "/tenant-email")
    public List<Tenant> findemailByTenant(){
        return tenantService.findAllTenantWithManufacture();        
    }
    
//    @GetMapping(value = "/tenant-email")
//    public List<TenantManufacture> findemailByTenant(){
//        return tenantService.findAllTenantWithManufactureN();        
//    }
    
    @GetMapping(value = "/tenant-manufacturer")
    public List<TenantManufacture> findAllTenantManufacturer(){
        return tenantService.findAllTenantManufacturer();        
    }
    
    @PutMapping(value="/tenant-update-deadline")
    public Result<TenantManufacture> updateDeadline (@RequestBody TenantManufacture t){
    	return tenantService.updateDeadline(t);
    }
    
    @PutMapping(value="/tenant-update-configure")
    public Result<Tenant> updateTenantconfigure (@RequestBody Tenant t){
    	return tenantService.updateTenantconfigure(t);
    }
    
    @PutMapping("/tenant-manufacture-update")
    @ResponseBody
    public Result<TenantManufacture> updateTenantManufacture(@RequestBody TenantManufacture t) {
        return tenantService.updateTenantManufacturer(t);
    }   
   

}