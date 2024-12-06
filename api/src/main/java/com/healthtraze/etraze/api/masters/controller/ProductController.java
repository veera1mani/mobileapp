package com.healthtraze.etraze.api.masters.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
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
import com.healthtraze.etraze.api.masters.dto.ItemsDto;
import com.healthtraze.etraze.api.masters.model.Invoice;
import com.healthtraze.etraze.api.masters.model.InvoiceLineItems;
import com.healthtraze.etraze.api.masters.model.Items;
import com.healthtraze.etraze.api.masters.model.Product;
import com.healthtraze.etraze.api.masters.service.ProductService;

@RestController
public class ProductController implements BaseCrudController<Product, String> {

	
	private final ProductService productService;
	
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	

	@PostMapping(value = "/invoice-details")
	public Result<Invoice> create(@RequestBody Invoice inv){
		return productService.createInvoice(inv);
		
	}
	
	@PostMapping(value = "/invoice-details-manual")
	public Result<Invoice> createInvoiceManual(@RequestBody Invoice inv){
		return productService.createInvoiceManual(inv);
		
	}
	
	@PostMapping(value = "/invoice-detail")
	public List<Map<String, Object>> getInvoices(@RequestBody List<String> invoices) {
	    return productService.getItemsByInvoiceNumber(invoices);
	}
	
	@GetMapping(value="/products")
	public Result<Page<Product>> findAllProduct(@RequestParam int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit, @RequestParam(required = false) String value, @RequestParam(required = false) String sortBy ,
			@RequestParam String sortDir) {
		return productService.findAllProduct(page,value,sortBy,sortDir);
	}

	@Override
	@GetMapping(value="/product")
	public List<Product> findAll() {
		return productService.findAll();
	}

	@Override
	public Product findById(String id) {
	
		return null;
	}




	@Override
	@PostMapping(value = "/product")
	public Result<Product> create(@RequestBody Product t) {
		return productService.create(t);
	}

	@Override
	@PutMapping(value = "/product")
	public Result<Product> update(@RequestBody Product t) {
		return productService.update(t);
	}

	@Override
	public Result<Product> delete(String id) {
		return null;
	}
	
	@PutMapping(value = "/invoice-line-items")
	public Result<InvoiceLineItems> updateInvoiceLineItems(@RequestBody InvoiceLineItems t) {
		return productService.updateInvoiceLineItems(t);
	}
	
	@PostMapping("/product/fileReader/{sheetIndex}")
	public Result<Object> fileReader(@RequestPart(name = "file" , required = true) 
	MultipartFile file , @PathVariable() String sheetIndex) throws IOException  {
		if (file == null || StringUtils.isBlank(file.getOriginalFilename()) || !StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), ".xlsx")) {
			return null;
		}
			return productService.fileReader(file , sheetIndex);
	}
	
	@GetMapping("/invoice-items")
    public Map<String, List<Items>> invoiceItems(@RequestParam("search") String  search) {
        return productService.invoiceItems(search);
    }

	@PutMapping(value = "/update-single-quantity")
	public Result<InvoiceLineItems> updateQuantity(@RequestBody InvoiceLineItems i){
		return productService.updateQuantity(i);
	}
	
	@PutMapping(value = "/update-multiple-quantity")
	public Result<List<ItemsDto>> updateQuantityMultiple(@RequestBody InvoiceLineItems inv){
		return productService.updateQuantityMultiple(inv);
	}
	
	@PutMapping(value = "/partial-pick")
	public Result<List<InvoiceLineItems>> updatePartialPickItems(@RequestParam("status") String status , @RequestBody List<InvoiceLineItems> i){
		return productService.updatePartialPickItems(status ,i);
	}
	
	@GetMapping(value= "/all-picked")
	public Result<String> allPicked(@RequestParam ("inv") String inv ){
		return productService.allPicked(inv);
	}
	

}
