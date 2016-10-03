package net.oicp.wego.model;

import net.oicp.wego.annotations.EKey;
import net.oicp.wego.annotations.Index;
import net.oicp.wego.model.base.Base;

/** 
 * @author         lolog
 * @version        v1.0  
 * @date           2016.08.28
 * @company        WEGO
 * @description    product brand
*/
@EKey("$set")
public class Brand extends Base {
	@Index(unique=true, name="brand_id")
	private String brand_id;
	@Index(unique=true, name="name")
	private String name;
	private String information;
	
	public Brand() {
		
	}
	
	public String getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(String brand_id) {
		this.brand_id = brand_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	
	@Override
	public String toString() {
		return "Brand [brand_id=" + brand_id + ", name=" + name + ", information=" + information + "]";
	}
}
