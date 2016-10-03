package net.oicp.wego.model;

import java.util.List;

import net.oicp.wego.annotations.EKey;
import net.oicp.wego.annotations.Index;
import net.oicp.wego.model.append.Authority;
import net.oicp.wego.model.base.Base;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.09.22
 * @company        WEGO
 * @description    manager group
 */
@EKey("$set")
public class Group extends Base {
	@Index(unique=true)
	private String group_id;
	@Index(unique=true)
	private String name;
	private List<Module> modules;
	private List<Authority> authorities;
	private String remark;
	public Group() {
		
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Module> getModules() {
		return modules;
	}
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	public List<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Override
	public String toString() {
		return "Group [group_id=" + group_id + ", group_name=" + name + ", authorities=" + authorities + ", remark=" + remark 
				+ ", modules=" + modules + ", Base [id=" + super.getId() + ", addy_id=" + super.getAddy_id() + ", addy_name=" 
				+ super.getAddy_name() + ", add_time=" + super.getAdd_time() + ", editors=" + super.getEditors() + ", is_del=" 
				+ super.getIs_del() + ", is_forbidden=" + super.getIs_forbidden() + "] ]";
	}
}
