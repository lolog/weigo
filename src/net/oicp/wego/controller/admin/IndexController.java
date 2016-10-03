package net.oicp.wego.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.oicp.wego.controller.AdminBaseController;

/** 
 * @author         lolog
 * @version        V1.0  
 * @Date           2016.08.13
 * @Company        WEIGO
 * @Description    manager index controller 
*/

@Controller
@RequestMapping("/admin/")
public class IndexController extends AdminBaseController {
	@RequestMapping("index")
	public ModelAndView index () {
		return view("zh/Index_index");
	}
}
