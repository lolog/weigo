package net.oicp.wego.model;


import java.util.Date;
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
 * @description    manager model
*/
@EKey("$set")
public class Manager extends Base {
	@Index(unique=true)
	private String manager_id;
	private String name;
	private String real_name;
	private String header;
	@Index(unique=true)
	private String jobwork;
	private String password;
	private Integer userlevel;
	private Integer role_id;
	private Integer group_id;
	private List<Authority> authority;
	private Date login_time;
	private String login_ip;
	
	public Manager() {
	}
	
	public String getManager_id() {
		return manager_id;
	}
	public void setManager_id(String manager_id) {
		this.manager_id = manager_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getJobwork() {
		return jobwork;
	}
	public void setJobwork(String jobwork) {
		this.jobwork = jobwork;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getUserlevel() {
		return userlevel;
	}
	public void setUserlevel(Integer userlevel) {
		this.userlevel = userlevel;
	}
	public Integer getRole_id() {
		return role_id;
	}
	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public List<Authority> getAuthority() {
		return authority;
	}
	public void setAuthority(List<Authority> authority) {
		this.authority = authority;
	}
	public Date getLogin_time() {
		return login_time;
	}
	public void setLogtime(Date login_time) {
		this.login_time = login_time;
	}
	public String getLogin_ip() {
		return login_ip;
	}
	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}

	@Override
	public String toString() {
		return "Manager [manager_id=" + manager_id + ", name=" + name + ", real_name=" + real_name + ", header="
				+ header + ", jobwork=" + jobwork + ", password=" + password + ", userlevel=" + userlevel + ", role_id="
				+ role_id + ", group_id=" + group_id + ", authority=" + authority + ", login_time=" + login_time
				+ ", login_ip=" + login_ip + "]";
	}
}
