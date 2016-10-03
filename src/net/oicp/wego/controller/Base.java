package net.oicp.wego.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

import net.oicp.wego.dao.pool.MongoPool;
import net.oicp.wego.util.PropertyContext;

/** 
 * @author         lolog
 * @version        v1.0
 * @date           2016.07.15
 * @company        WEIGO
 * @description    base
*/
public class Base extends WebApplicationObjectSupport {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	// global variable
	protected static ApplicationContext  applicationContext;
	protected static ServletContext      servletContext;
	protected static String              contextPath;
	
	@Autowired
	protected MongoPool mongoPool;
	
	@Autowired
	protected PropertyContext propertyContext;
	@Autowired
	protected VelocityLayoutViewResolver velocityLayoutViewResolver;
	
	static protected ThreadLocal<HttpServletRequest>   requests = new ThreadLocal<HttpServletRequest>();
	static protected ThreadLocal<HttpServletResponse>  respones = new ThreadLocal<HttpServletResponse>();
	static protected ThreadLocal<HttpSession>          sessions = new ThreadLocal<HttpSession>();
	static protected ThreadLocal<Map<String, Object>>  jsons    = new ThreadLocal<Map<String, Object>>();
	static protected ThreadLocal<Map<String, Object>>  models    = new ThreadLocal<Map<String, Object>>();
	
	public Base () {
		
	}
	
	@ModelAttribute
	protected void initialMapping (
			HttpServletRequest request, 
			HttpServletResponse response, 
			HttpSession session,
			Map<String, Object> model
	) {
		
		Map<String, Object> json = new HashMap<String, Object>();
		
		requests.set(request);
		respones.set(response);
		sessions.set(session);
		jsons.set(json);
		models.set(model);
		
		applicationContext = super.getApplicationContext();
		servletContext     = super.getServletContext();
		contextPath        = servletContext.getContextPath();
		
		// velocity
		String requestUrl = request.getRequestURI();
		// application name
		String appPath = requestUrl.substring(requestUrl.indexOf(contextPath) + contextPath.length());
		
		// manager file path
		if (appPath.indexOf(propertyContext.getAdmin()) < 2) {
			// layout name of manager
			String adminLayoutName = (String) session.getAttribute("adminLayoutName");
			// judge
			if (adminLayoutName == null) {
				adminLayoutName = propertyContext.getLayoutDefault()+".vm";
			}
			
			// manager path
			String adminLayout = propertyContext.getAdminLayout();
			if(adminLayout.endsWith("/") == true) {
				adminLayout.substring(0, adminLayout.length() - 1);
			}
			if(adminLayout.startsWith("/") == true) {
				adminLayout.substring(1, adminLayout.length());
			}
			
			// adminLayout layout file path
			String layoutUrl = "/" + adminLayout + "/" + adminLayoutName;
			// setting
			velocityLayoutViewResolver.setLayoutUrl(layoutUrl);
		}
		
		// home file path
		else {
			
		}
	}
	
	// display view
	protected ModelAndView view (String viewName) {
		// create ModelAndView Object
		ModelAndView view = new ModelAndView();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// setting page variable
		map.put("page_title", "WEGO");
		
		// velocity
		String requestUrl = request().getRequestURI();
		// application name
		String appPath    = requestUrl.substring(requestUrl.indexOf(contextPath) + contextPath.length());
		
		map.put("common", contextPath + "/common");
		// app
		map.put("app", contextPath);
		
		// manager file path
		if (appPath.indexOf(propertyContext.getAdmin()) < 2) {
			String admin = propertyContext.getAdmin();
			if(admin.endsWith("/") == true) {
				admin.substring(0, admin.length() - 1);
			}
			if(admin.startsWith("/") == true) {
				admin.substring(1, admin.length());
			}
			
			// manager layout file path
			map.put("domain", contextPath + "/" + admin);
			map.put("js",  contextPath + "/" + admin + "/js");
			map.put("css", contextPath + "/" + admin + "/css");
			map.put("img", contextPath + "/" + admin + "/img");
			map.put("vjs", contextPath + "/" + admin + "/view/vjs");
			
			// set view path
			view.setViewName("/" + admin + "/view/" + viewName);
			
		}
		else {
			map.put("domain", "/" + propertyContext.getHome());
			
			// set view path
			view.setViewName(viewName);
		}
		
		view.addAllObjects(map);
		
		// clear garbage
		gc();
				
		return view;
	}
	
	// display view
	protected ModelAndView view (String viewName, Map<String, Object> map) {
		// create ModelAndView Object
		ModelAndView view = new ModelAndView();
		
		if(map == null) {
			map = new HashMap<String, Object>();
		}
		
		// setting page variable
		if(map.get("page_title") == null) {
			map.put("page_title", "WEGO");
		}
		
		// velocity
		String requestUrl = request().getRequestURI();
		// application name
		String appPath    = requestUrl.substring(requestUrl.indexOf(contextPath) + contextPath.length());
		
		map.put("common", contextPath + "/common");
		// app
		map.put("app", contextPath);
		
		// manager file path
		if (appPath.indexOf(propertyContext.getAdmin()) < 2) {
			String admin = propertyContext.getAdmin();
			if(admin.endsWith("/") == true) {
				admin.substring(0, admin.length() - 1);
			}
			if(admin.startsWith("/") == true) {
				admin.substring(1, admin.length());
			}
			// manager layout file path
			map.put("domain", contextPath + "/" + admin);
			map.put("js",  contextPath + "/" + admin + "/js");
			map.put("css", contextPath + "/" + admin + "/css");
			map.put("img", contextPath + "/" + admin + "/img");
			map.put("vjs", contextPath + "/" + admin + "/view/vjs");
			
			// set view path
			view.setViewName(contextPath + "/" + admin + "/view/" + viewName);
			
		}
		else {
			map.put("domain", contextPath + "/" + propertyContext.getHome());
			
			// set view path
			view.setViewName(viewName);
		}
		
		// clear garbage
		gc();
		
		// model
		view.addAllObjects(map);
		return view;
	}
	
	// display view
	protected ModelAndView view (String viewName, String modelName, Map<String, Object> map) {
		ModelAndView view = null;
		
		if(map == null) {
			map = new HashMap<String, Object>();
		}
		
		// setting page variable
		if(map.get("page_title") == null) {
			map.put("page_title", "WEGO");
		}
		
		// velocity
		String requestUrl = request().getRequestURI();
		// application name
		String appPath    = requestUrl.substring(requestUrl.indexOf(contextPath) + contextPath.length());
		
		map.put("common", contextPath + "/common");
		// app
		map.put("app", contextPath);
		
		// manager file path
		if (appPath.indexOf(propertyContext.getAdmin()) < 2) {
			String admin = propertyContext.getAdmin();
			if(admin.endsWith("/") == true) {
				admin.substring(0, admin.length() - 1);
			}
			if(admin.startsWith("/") == true) {
				admin.substring(1, admin.length());
			}
			// manager layout file path
			map.put("domain", contextPath + "/" + admin);
			map.put("js",  contextPath + "/" + admin + "/js");
			map.put("css", contextPath + "/" + admin + "/css");
			map.put("img", contextPath + "/" + admin + "/img");
			map.put("vjs", contextPath + "/" + admin + "/view/vjs");
			
			view = new ModelAndView(contextPath + "/" + admin + "/view/" + viewName, modelName, map);
			
		}
		else {
			map.put("domain", contextPath + "/" + propertyContext.getHome());
			
			view = new ModelAndView(viewName, modelName, map);
		}
		
		// clear garbage
		gc();
		
		return view;
	}
	
	// return JSON
	protected ModelAndView json () {
		// create ModelAndView Object
		ModelAndView json = new ModelAndView();
		// setting return JSON
		json.setView(new MappingJackson2JsonView());
		// add data
		json.getModelMap().addAllAttributes(jsons());
		
		// clear garbage
		gc();
		
		// return JSON ModelAndView
		return json;
	}
	
	// return JSON
	protected ModelAndView json (Map<String, Object> model) {
		// create ModelAndView Object
		ModelAndView json = new ModelAndView();
		// setting return JSON
		json.setView(new MappingJackson2JsonView());
		// add data
		json.getModelMap().addAllAttributes(model);
		
		// clear garbage
		gc();
				
		// return JSON ModelAndView
		return json;
	}
	
	// clear Model value
	protected Boolean clearModel (Map<String, Object> model) {
		if (model == null) {
			return false;
		}
		
		// clear values
		model.clear();
		
		return true;
	}
	
	// get current thread HttpServletRequest Object
	protected HttpServletRequest request() {
		return requests.get();
	}
	
	// get current thread HttpServletResponse Object 
	protected HttpServletResponse response () {
		return respones.get();
	}
	
	// get current thread HttpSession Object 
	protected HttpSession session () {
		return sessions.get();
	}
	
	// get current json data object
	protected Map<String, Object> jsons () {
		return jsons.get();
	}
	
	protected Map<String, Object> models () {
		return models.get();
	}
	// gc 
	protected void gc () {
		requests.remove();
		respones.remove();
		sessions.remove();
		jsons.remove();
		models.remove();
	}
}
 