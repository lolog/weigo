package net.oicp.wego.model.append;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.09.25
 * @company        WEGO
 * @description    module authority 
*/

public class Authority {
	private String module_id;
	private String module_name;
	private Boolean all;
	private Boolean add;
	private Boolean delete;
	private Boolean update;
	private Boolean query;
	
	public Authority() {
		
	}
	
	public String getModule_id() {
		return module_id;
	}
	public void setModule_id(String module_id) {
		this.module_id = module_id;
	}
	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}
	public Boolean getAll() {
		return all;
	}
	public void setAll(Boolean all) {
		this.all = all;
	}
	public Boolean getAdd() {
		return add;
	}
	public void setAdd(Boolean add) {
		this.add = add;
	}
	public Boolean getDelete() {
		return delete;
	}
	public void setDelete(Boolean delete) {
		this.delete = delete;
	}
	public Boolean getUpdate() {
		return update;
	}
	public void setUpdate(Boolean update) {
		this.update = update;
	}
	public Boolean getQuery() {
		return query;
	}
	public void setQuery(Boolean query) {
		this.query = query;
	}
	
	@Override
	public String toString() {
		return "Authority [module_id=" + module_id + ", module_name=" + module_name + ", all="+ all + ", add=" + add 
				+ ", delete=" + delete + ", update=" + update + ", query=" + query + "]";
	}
}
