package com.healthtraze.etraze.api.masters.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.healthtraze.etraze.api.masters.dto.PaginationDTO;
import com.healthtraze.etraze.api.masters.model.Stockist;
import com.healthtraze.etraze.api.masters.service.StockistService;

@RestController
public class StockistController implements BaseCrudController<Stockist, String> {

    private final StockistService stockistServices;

    
    
    @Autowired
    public StockistController(StockistService customerServices) {
        this.stockistServices = customerServices;
    }

    @Override
    @GetMapping("/stockists")
    public List<Stockist> findAll() {
        return stockistServices.findAll();
    }

    @Override
    @PostMapping(value = "/stockist")
    public Result<Stockist> create(@RequestBody Stockist t) {
        return stockistServices.create(t);
    }

    @Override
    @PutMapping(value = "/stockist")
    public Result<Stockist> update(@RequestBody Stockist t) {
        return stockistServices.update(t);
    }

    @Override
    @DeleteMapping("/stockist/{id}")
    public Result<Stockist> delete(@PathVariable String id) {
        return stockistServices.delete(id);
    }

    @GetMapping(value = "/stockist/{id}")
    @Override
    public Stockist findById(@PathVariable String id) {
        return stockistServices.findById(id);
    }

    @GetMapping(value = "/stockist/{id}/{sap}")
    public Stockist findStockistBySap(@PathVariable String id, @PathVariable String sap) {
        return stockistServices.findStockistBySap(id, sap);
    }


	@GetMapping(value = "/stickist-list")
	public List<Map<String, Object>> findStockistList() {
		return stockistServices.findStockistByManufacture();
	}

    @GetMapping(value = "/stickist-list-user")
    public List<Stockist> stockistList(@RequestParam String userId) {
        return stockistServices.findStockistByManufacturer(userId);

    }

    @GetMapping("/stockist-all")
    public Result<Page<Stockist>> getAllStockists(@RequestParam("page") int page,
            @RequestParam("value") String value, @RequestParam("sortBy") String sortBy,
            @RequestParam("sortDir") String sortDir) {   	
    	PaginationDTO paging = new PaginationDTO(value, sortBy, sortDir,page);
        return stockistServices.getAllStockist(paging);
    }

    @GetMapping("/stockist-city")
    public boolean cityChange(@RequestParam String stockistName, @RequestParam String cityId,
            @RequestParam String tenantId) {
        return stockistServices.cityChange(stockistName, cityId, tenantId);
    }

	@PostMapping("/stockist/fileReader/{sheetIndex}")
	public Result<Map<String, Object>> fileReader(@RequestPart(name = "file", required = true) MultipartFile file,
			@PathVariable() String sheetIndex) throws FileNotFoundException, IOException {
		if (!file.getOriginalFilename().endsWith(".xlsx")) {
			
			Result<List<Stockist>> result = new Result<>();
			result.setCode("1111");
			result.setMessage("Invalid data");
			result.setData(new ArrayList<>());
		}
		return stockistServices.fileReader(file, sheetIndex);
	}
	
	
	@GetMapping ("/stockist-manufacture")
	public List<Object> stockisMnaufacture(@RequestParam(required=false,value="userId") String userId){
		return stockistServices.stockisMnaufacture(userId);
	}
	
	
	
}
