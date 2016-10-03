package net.oicp.wego.model.append;

import java.util.Date;

/** 
 * @author         lolog
 * @version        V1.0  
 * @Date           2016.08.02
 * @Company        WEGO
 * @Description    festival
*/
public class Festival {
	// festival 
	private String festival_id;
	// festival name
	private String name;
	// promotion start time
	private Date start_time;
	// promotion end_time
	private Date end_time;
	// product discount
	private Double discount;
	// promotions money
	private Integer discount_money;
	private String information;
	
	public Festival() {
		
	}
	
	public String getFestival_id() {
		return festival_id;
	}
	public void setFestival_id(String festival_id) {
		this.festival_id = festival_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Integer getDiscount_money() {
		return discount_money;
	}
	public void setDiscount_money(Integer discount_money) {
		this.discount_money = discount_money;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}

	@Override
	public String toString() {
		return "Festival [festival_id=" + festival_id + ", name=" + name + ", start_time=" + start_time + ", end_time="
				+ end_time + ", discount=" + discount + ", discount_money=" + discount_money + ", information="
				+ information + "]";
	}
}
