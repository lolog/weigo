package net.oicp.wego.model;

import net.oicp.wego.annotations.Index;

/** 
 * @author         lolog
 * @version        V1.0  
 * @Date           2016年7月8日
 * @Company        CIMCSSC
 * @Description    TODO(文件描述) 
*/
public class Test {
	@Index(name="index")
	private String index;
	
	public Test() {
	}
	
	public void setIndex(String index) {
		this.index = index;
	}
	public String getIndex() {
		return index;
	}
	@Override
	public String toString() {
		return "Test [index=" + index + "]";
	}
}
