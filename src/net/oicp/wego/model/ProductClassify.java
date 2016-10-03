package net.oicp.wego.model;

import net.oicp.wego.annotations.EKey;
import net.oicp.wego.annotations.Index;
import net.oicp.wego.model.base.Base;

/**
 * @author lolog
 * @version V1.0
 * @date 2016.8.13
 * @company WEGO
 * @description product classify
 */

@EKey("$set")
public class ProductClassify extends Base {
	@Index(unique = true, name = "name")
	private String name;
	@Index(unique = true, name = "classify_id")
	private String classify_id;
	private String intro;

	public ProductClassify() {

	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassify_id() {
		return classify_id;
	}
	public void setClassify_id(String classify_id) {
		this.classify_id = classify_id;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}

	@Override
	public String toString() {
		return "ProductClassify [name=" + name + ", classify_id=" + classify_id + ", intro=" + intro + ", Base [id="
				+ getId() + ", addy_id=" + getAddy_id() + ", addy_name=" + getAddy_name() + ", add_time="
				+ getAdd_time() + ", editors=" + getEditors() + ", is_del=" + getIs_del() + ", is_forbidden="
				+ getIs_forbidden() + "]]";
	}
}
