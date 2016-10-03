package net.oicp.wego.model;

import net.oicp.wego.annotations.EKey;
import net.oicp.wego.annotations.Index;
import net.oicp.wego.model.base.Base;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.08.26
 * @company        WEGO
 * @description    shop
*/

@EKey("$set")
public class Shop extends Base{
	@Index(unique=true, name="shop_id")
	private String shop_id;
	@Index(unique=true, name="name")
	private String name;
	private String money;
	private Double discount_money;
	private String address;
	private String information;
	
	public Shop() {
		
	}
	
	public String getShop_id() {
		return shop_id;
	}
	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public Double getDiscount_money() {
		return discount_money;
	}
	public void setDiscount_money(Double discount_money) {
		this.discount_money = discount_money;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	
	@Override
	public String toString() {
		return "Shop [shop_id=" + shop_id + ", name=" + name + ", money=" + money + ", discount_money=" + discount_money
				+ ", address=" + address + ", information=" + information + "]";
	}
}
