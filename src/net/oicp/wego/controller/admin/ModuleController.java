package net.oicp.wego.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.oicp.wego.controller.AdminBaseController;
import net.oicp.wego.dao.ModuleDao;
import net.oicp.wego.model.Brand;
import net.oicp.wego.model.Group;
import net.oicp.wego.model.Module;
import net.oicp.wego.model.parameter.DataTable;
import net.oicp.wego.model.parameter.Order;
import net.oicp.wego.model.parameter.Search;
import net.oicp.wego.tools.Errors;
import net.oicp.wego.tools.JsonConvert;
import net.oicp.wego.tools.Tool;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.09.24
 * @company        WEGO
 * @description    module controller
 */
@Controller
@RequestMapping("/admin/module/")
@SuppressWarnings({"unchecked","unused"})
public class ModuleController extends AdminBaseController {
	@RequestMapping("manageView")
	public ModelAndView manageView () {
		return view("zh/module_manageView");
	}
	
	@RequestMapping("addView")
	public ModelAndView addView () {
		Map<String, Object> filter = new HashMap<String, Object>();
		
		// {"is_del":{"$ne":true},"is_forbidden":{"$ne":true}}
		Object[][]          isDelFilter    = {{"is_del","is_forbidden"},{"$ne","$ne"},{true,true}};
		List<Object>        filterValue    = Tool.arrayXMap(isDelFilter);
		filter.put("$and", filterValue);
		
		// filter field
		String includeFields = "module_id,name";
		// query
		List<Module> modules = moduleDao
								.filter(filter)
								.include(includeFields)
								.query();
		models().put("modules", modules);
		
		return view("zh/module_addView");
	}
	
	@RequestMapping("editView")
	public ModelAndView editView (@RequestParam(value="module_id",required=false) String module_id) {
		Map<String, Object> filter = new HashMap<String, Object>();
		
		// {"is_del":{"$ne":true}}
		Object[][]          isDelFilter    = {{"is_del","is_forbidden"},{"$ne","$ne"},{true,true}};
		List<Object>        filterValue    = Tool.arrayXMap(isDelFilter);
		filter.put("$and", filterValue);
		
		// filter field
		String includeFields = "module_id,name";
		// query
		List<Module> modules = moduleDao
									.filter(filter)
									.include(includeFields)
									.query();
		// filter
		filter.clear();
		filter.put("module_id", module_id);
		// query
		Module module = (Module) moduleDao
								.filter(filter)
								.queryOne();
		
		models().put("this", module);
		models().put("modules", modules);
		return view("zh/module_editView");
	}
	
	@RequestMapping(value="loadModuleData")
	public ModelAndView loadModuleData (@RequestParam(value="data", required=false) String data) {
		
		// request parameters
		List<DataTable> dataTables = null;
		Search          search     = null;
		List<Order>     orders     = null;
		
		dataTables = (List<DataTable>) dealJsonData(data, DataTable.class, true, "columns");
		search     = (Search)          dealJsonData(data, Search.class,    false);
		orders     = (List<Order>)     dealJsonData(data, Order.class,     true,  "order");
		
		
		if(search == null
			|| search.getStart() < 0
			|| search.getLength() < 1
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// sort
		Map<String, Object> sort       = new HashMap<String, Object>();
		String[]            orderField = {"name","super_id","module_level","action_url","is_expand","add_time","is_forbidden"}; 
		if((orders != null 
			&& orders.size() > 0 
			&& orders.get(0).getColumn() > 1
			&& (orders.get(0).getColumn() < (orderField.length + 2))) == true) {
			Order order = orders.get(0);
			if (order.getDir() != null 
				&& "desc".equalsIgnoreCase(order.getDir().trim()) == true) {
				sort.put(orderField[order.getColumn() - 2], -1);
			}
			else {
				sort.put(orderField[order.getColumn() - 2], 1);
			}
		}
		
		Map<String, Object> filter = new HashMap<String, Object>();
		
		// {"is_del":{"$ne":true}}
		Object[][]          isDelFilter    = {{"$ne"},{true}};
		filter.put("is_del", Tool.arrayToMap(isDelFilter));
		
		if (search != null 
			&& search.getSearch() != null 
			&& search.getSearch().get("value") != null
			&& search.getSearch().get("value").toString().trim().length() > 0) {
			
			String  value = search.getSearch().get("value").toString();
			Boolean regex = (Boolean) search.getSearch().get("regex");
			
			// name
			Map<String, Object> regexMap  = null;
			
			Object[][] searchArray  = {{"name","action_url","remark"},{"$regex","$regex","$regex"},{value,value,value}};
			List<Object> serachList = Tool.arrayXMap(searchArray);
			
			filter.put("$or", serachList);
		}
		
		// query fields
		String[] includes = {"module_id","name","super_id","module_level","action_url","remark","is_expand","add_time","is_forbidden"};		
		// query currency
		List<Document> groups = moduleDao
				.filter(filter)
				.include(includes)
				.skip(search.getStart())
				.sort(sort)
				.limit(search.getLength())
				.queryDocument();
		
		Long totle = moduleDao.count();
		
		// set json data
		setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_LOAD_ZH, Errors.ERROR_CODE_NONE);
		
		jsons().put("draw", search.getDraw());
		jsons().put("data", groups);
		jsons().put("recordsTotal", totle);
		jsons().put("recordsFiltered", totle);
		
		return json();
	}
	
	@RequestMapping("addModule")
	public ModelAndView addModule (@RequestParam(value="data", required=false) String data) {
		// get the parameters
		Module module = (Module) JsonConvert.jsonToObject(Module.class, data);
		if(module == null 
			|| module.getName() == null
			|| module.getRemark() == null
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_NONE);
			return json(jsons());
		}
		
		// currency id
		String module_id = getIDS("module_id");
		module.setModule_id(module_id);
		
		// set module level
		if (module.getModule_level() == null) {
			// default super manager
			module.setModule_level(1);
		}
		
		// add time
		Date add_time = new Date();
		module.setAdd_time(add_time);
		
		// filter field
		String[] includeFields = {"module_id","name","super_id","module_level","action_url","remark","is_expand","add_time","is_forbidden"};
		
		Boolean flag = false;
		// save object
		try {
			flag = moduleDao.include(includeFields).data(module).insert();
		} catch (Exception e) {
			
		}
		
		if (flag == true) {
			setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_ADD_ZH,Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_ADD_ZH,Errors.ERROR_CODE_ADD);
		}
		
		return json();
	}
	
	@RequestMapping("editModule")
	public ModelAndView editModule (@RequestParam("data") String data) {
		// get the parameters
		Module module = (Module) JsonConvert.jsonToObject(Module.class, data);
		
		if (module == null
			|| module.getName() == null
			|| module.getRemark() == null 
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// forbidden setting
		if (module.getIs_forbidden() == null) {
			module.setIs_forbidden(false);
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("module_id", module.getModule_id());
		
		// query whether exists
		Module moduleData = (Module) moduleDao.filter(filter).queryOne();
		
		// data not exists
		if(moduleData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		// update
		String[] include = {"name", "super_id", "module_level", "action_url", "remark", "is_expand", "is_forbidden"};
		Boolean status = moduleDao.filter(filter).data(module).include(include).update();
		
		if(status == true) {
			setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_UPDATE);
		}
		
		return json();
	}
	
	@RequestMapping("deleteModule")
	public ModelAndView deleteModule (@RequestParam(value="data",required=false) String data) {
		Module module = (Module) dealJsonData(data, Module.class, false);
		if (module.getModule_id() == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("module_id", module.getModule_id());
		
		// query whether exists
		Module moduleData = (Module) moduleDao.filter(filter).queryOne();
		
		// data not exists
		if(moduleData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		String[] include = {"is_del"};
		// delete currency
		module.setIs_del(true);
		
		Boolean status = moduleDao.filter(filter).include(include).data(module).update();
		
		if (status == true) {
			setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_DELETE);
		}
		
		return json();
	}
	
	@RequestMapping("existModuleField")
	public ModelAndView existModuleField (@RequestParam(value="data",required=false) String data) {
		Module module = (Module) dealJsonData(data, Module.class, false);
		
		Map<String, Object> filter = new HashMap<String, Object>();
		
		if (module != null && module.getModule_id() != null) {
			filter.put("module_id", module.getModule_id());
		}
		else if (module != null && module.getName() != null) {
			filter.put("name", module.getName());
		}
		else {
			
		}
		
		// query
		if (filter.isEmpty() == false) {
			Module module2 = (Module) moduleDao.filter(filter).queryOne();
			if (module2 == null) {
				jsons().put("exist", false);
			}
			else {
				jsons().put("exist", true);
			}
		}
		
		setJson(true, Errors.SUCCESS_MESSAGE_SELECT_ZH, Errors.ERROR_CODE_NONE);
		
		return json();
	}
}
