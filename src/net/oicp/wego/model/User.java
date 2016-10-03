package net.oicp.wego.model;

import java.util.List;
import java.util.Map;

import net.oicp.wego.annotations.Index;

/**
 * @author lolog
 * @version V1.0
 * @Date 2016年7月6日
 * @Company CIMCSSC
 * @Description TODO(文件描述)
 */
public class User extends Test {
	@Index(id = true)
	private String objectid;
	@Index(background = true, name = "so")
	private Integer id;
	private String name;
	private String passsword;

	private List<Test> list;
	private Map<String, Object> doc;

	public User() {
	}

	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}

	public String getObjectid() {
		return objectid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasssword() {
		return passsword;
	}

	public void setPasssword(String passsword) {
		this.passsword = passsword;
	}

	public void setList(List<Test> list) {
		this.list = list;
	}

	public List<Test> getList() {
		return list;
	}
	
	public void setDoc(Map<String, Object> doc) {
		this.doc = doc;
	}
	
	public Map<String, Object> getDoc() {
		return doc;
	}

	@Override
	public String toString() {
		return "User [objectid=" + objectid + ", id=" + id + ", name=" + name + ", passsword=" + passsword + ", list="
				+ list + ", doc=" + doc +", super=[index=" + getIndex() + "]]";
	}
}
