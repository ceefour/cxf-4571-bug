package com.hendyirawan.cxf4571bug.rs;

import java.math.BigDecimal;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("product")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductSysResource {
	
	@GET
	public Product getProduct() {
		return new Product("Tas Batik", new BigDecimal(450000));
	}

}
