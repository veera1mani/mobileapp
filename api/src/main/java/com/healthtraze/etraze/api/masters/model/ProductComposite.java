package com.healthtraze.etraze.api.masters.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductComposite implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String productCode;
	private String manufacturer;
	private String tenantId;
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductComposite that = (ProductComposite) o;
        return Objects.equals(productCode, that.productCode) && Objects.equals(manufacturer, that.manufacturer) && Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, manufacturer,tenantId);
    }

}
