package net.oicp.wego.model;

import net.oicp.wego.annotations.EKey;
import net.oicp.wego.annotations.Index;
import net.oicp.wego.model.base.Base;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.09.22
 * @company        WEGO
 * @description    module
 */
@EKey("$set")
public class Module extends Base {
	@Index(unique=true)
	private String module_id;
	@Index(unique=true)
	private String name;
	private String super_id;
	private Integer module_level;
	private String action_url;
	private String remark;
	private Boolean is_expand;
	
	public Module() {
		
	}
	
	public String getModule_id() {
		return module_id;
	}
	public void setModule_id(String module_id) {
		this.module_id = module_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSuper_id() {
		return super_id;
	}
	public void setSuper_id(String super_id) {
		this.super_id = super_id;
	}
	public Integer getModule_level() {
		return module_level;
	}
	public void setModule_level(Integer module_level) {
		this.module_level = module_level;
	}
	public String getAction_url() {
		return action_url;
	}
	public void setAction_url(String action_url) {
		this.action_url = action_url;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Boolean getIs_expand() {
		return is_expand;
	}
	public void setIs_expand(Boolean is_expand) {
		this.is_expand = is_expand;
	}
	
	@Override
	public String toString() {
		return "Module [module_id=" + module_id + ", name=" + name + ", super_id=" + super_id + ", module_level="
				+ module_level + ", action_url=" + action_url +", remark="+ remark + ", is_expand=" + is_expand 
				+ ", Base [id=" + super.getId() + ", addy_id=" + super.getAddy_id() + ", addy_name=" + super.getAddy_name() 
				+ ", add_time=" + super.getAdd_time()+ ", editors=" + super.getEditors() + ", is_del=" + super.getIs_del() 
				+ ", is_forbidden=" + super.getIs_forbidden() + "]]";
	}
}
