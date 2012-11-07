package com.hendyirawan.cxf4571bug.rs;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ceefour
 *
 */
public class Product implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private BigDecimal price;
	
	public Product(String name, BigDecimal price) {
		super();
		this.name = name;
		this.price = price;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
