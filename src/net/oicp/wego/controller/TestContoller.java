package net.oicp.wego.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/** 
 * @author         00013052
 * @version        V1.0  
 * @Date           2016.06.22
 * @Company        WEIGO
 * @Description    Spring Controller test 
 */
@Controller
@RequestMapping("/admin/test")
public class TestContoller extends AdminBaseController {
	
	@RequestMapping("/view")
	public ModelAndView tests (Map<String, Object> model) {
		clearModel(model);
		model.put("xxx", "value");
		model.put("path", contextPath);
		return view("test/index");
	}
	
	@RequestMapping("/re")
	public ModelAndView test (HttpServletRequest request,@RequestParam("columns") String columns) {
		System.out.println("+++++++++++++++++++++++++++++++");
		System.out.println(request.getParameterValues("p"));
		System.out.println(request.getParameterMap());
		System.out.println(columns);
		System.out.println("+++++++++++++++++++++++++++++++");
		return null;
	}
}
