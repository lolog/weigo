package net.oicp.wego.model;

import net.oicp.wego.annotations.EKey;
import net.oicp.wego.annotations.Index;
import net.oicp.wego.model.base.Base;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.09.22
 * @company        WEGO
 * @description    role
*/
@EKey("$set")
public class Role extends Base {
	@Index(unique=true)
	private String role_id;
	@Index(unique=true)
	private String mobile;
	private String company;
	private String company_tel;
	private String company_jobs;
	private String school;
	private String college;
	private String professional;
	private String clazz;
	private String family_address;
	private String family_tel;
	private String emerge_person;
	private String emergy_mobile;
	private String emergy_extra;
	private String reside_adress;
	private String reside_zip_code;
	
	public Role() {
		
	}
	
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompany_tel() {
		return company_tel;
	}
	public void setCompany_tel(String company_tel) {
		this.company_tel = company_tel;
	}
	public String getCompany_jobs() {
		return company_jobs;
	}
	public void setCompany_jobs(String company_jobs) {
		this.company_jobs = company_jobs;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public String getProfessional() {
		return professional;
	}
	public void setProfessional(String professional) {
		this.professional = professional;
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public String getFamily_address() {
		return family_address;
	}
	public void setFamily_address(String family_address) {
		this.family_address = family_address;
	}
	public String getFamily_tel() {
		return family_tel;
	}
	public void setFamily_tel(String family_tel) {
		this.family_tel = family_tel;
	}
	public String getEmerge_person() {
		return emerge_person;
	}
	public void setEmerge_person(String emerge_person) {
		this.emerge_person = emerge_person;
	}
	public String getEmergy_mobile() {
		return emergy_mobile;
	}
	public void setEmergy_mobile(String emergy_mobile) {
		this.emergy_mobile = emergy_mobile;
	}
	public String getEmergy_extra() {
		return emergy_extra;
	}
	public void setEmergy_extra(String emergy_extra) {
		this.emergy_extra = emergy_extra;
	}
	public String getReside_adress() {
		return reside_adress;
	}
	public void setReside_adress(String reside_adress) {
		this.reside_adress = reside_adress;
	}
	public String getReside_zip_code() {
		return reside_zip_code;
	}
	public void setReside_zip_code(String reside_zip_code) {
		this.reside_zip_code = reside_zip_code;
	}
	
	@Override
	public String toString() {
		return "Role [role_id=" + role_id + ", mobile=" + mobile + ", company=" + company + ", company_tel="
				+ company_tel + ", company_jobs=" + company_jobs + ", school=" + school + ", college=" + college
				+ ", professional=" + professional + ", clazz=" + clazz + ", family_address=" + family_address
				+ ", family_tel=" + family_tel + ", emerge_person=" + emerge_person + ", emergy_mobile=" + emergy_mobile
				+ ", emergy_extra=" + emergy_extra + ", reside_adress=" + reside_adress + ", reside_zip_code="
				+ reside_zip_code + "]";
	}
}
