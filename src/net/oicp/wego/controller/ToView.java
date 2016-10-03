package net.oicp.wego.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/** 
 * @author         00013052
 * @version        V1.0  
 * @Date           2016.o6.21
 * @Company        WEIGO
 * @Description    ModelAndView
 */

public class ToView extends ModelAndView {
	// 日志文件对象
	protected Logger logger = Logger.getLogger(this.getClass());
	
	public ToView () {
		super();
	}
	
	public ToView (String viewName) {
		if ("json".equals(viewName)) {
			// 页面返回JSON形式
			setView(new MappingJackson2JsonView());
		}
		else {
			// 页面跳转
			setViewName(viewName);
		}
	}
	
	public ToView (String viewName, Map<String, ?> model) {
		if ("json".equals(viewName)) {
			// return JSON data
			setView(new MappingJackson2JsonView());
			getModelMap().addAllAttributes(model);
		}
		else {
			// return view
			setViewName(viewName);
			if (model != null) {
				getModelMap().addAllAttributes(model);
			}
		}
	}
	
	
	/**
	 * @Title             setVar 
	 * @author            lolog
	 * @Description       set template variable
	 * @param key         key
	 * @param value       value
	 * @Date              2016.06.21 10:58:38
	 */
	public void setVar (String key, String value) {
		super.addObject(key, value);
	}
}
