package net.oicp.wego.model;

import java.util.Date;

import net.oicp.wego.annotations.EKey;
import net.oicp.wego.annotations.Index;
import net.oicp.wego.model.base.Base;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.08.25
 * @company        WEGO
 * @description    festival
*/
@EKey("$set")
public class Festival extends Base {
	@Index(unique=true,name="festival_id")
	private String festival_id;
	@Index(unique=true,name="name")
	private String name;
	private String start_string;
	private Date start_time;
	private String end_string;
	private Date end_time;
	
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
	public String getStart_string() {
		return start_string;
	}
	public void setStart_string(String start_string) {
		this.start_string = start_string;
	}
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public String getEnd_string() {
		return end_string;
	}
	public void setEnd_string(String end_string) {
		this.end_string = end_string;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	
	@Override
	public String toString() {
		return "Festival [festival_id=" + festival_id + ", name=" + name + ", start_string=" + start_string
				+ ", start_time=" + start_time + ", end_string=" + end_string + ", end_time=" + end_time + "]";
	}
}
