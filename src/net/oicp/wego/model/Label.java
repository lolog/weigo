package net.oicp.wego.model;

import net.oicp.wego.annotations.EKey;
import net.oicp.wego.annotations.Index;
import net.oicp.wego.model.base.Base;

/** 
 * @author         lolog
 * @version        V1.0  
 * @Date           2016.08.13
 * @Company        WEGO
 * @Description    product label
*/
@EKey("$set")
public class Label extends Base {
	@Index(unique=true, name="label_id")
	private String label_id;
	@Index(unique=true, name="name")
	private String name;
	private String information;
	
	public Label() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel_id() {
		return label_id;
	}
	public void setLabel_id(String label_id) {
		this.label_id = label_id;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}

	@Override
	public String toString() {
		return "Label [label_id=" + label_id + ", name=" + name + ", information=" + information + "]";
	}
}
