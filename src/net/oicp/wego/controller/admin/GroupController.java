package net.oicp.wego.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.ErrorData;

import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.swing.internal.plaf.metal.resources.metal;

import net.oicp.wego.controller.AdminBaseController;
import net.oicp.wego.model.Brand;
import net.oicp.wego.model.Group;
import net.oicp.wego.model.Module;
import net.oicp.wego.model.append.Authority;
import net.oicp.wego.model.parameter.DataTable;
import net.oicp.wego.model.parameter.Order;
import net.oicp.wego.model.parameter.Search;
import net.oicp.wego.tools.Errors;
import net.oicp.wego.tools.JsonConvert;
import net.oicp.wego.tools.Tool;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.09.22
 * @company        WEGO
 * @description    group controller
 */
@Controller
@RequestMapping("/admin/group/")
@SuppressWarnings({"unchecked","unused"})
public class GroupController extends AdminBaseController {
	@RequestMapping("manageView")
	public ModelAndView manageView () {
		return view("zh/group_manageView");
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
		
		return view("zh/group_addView");
	}
	
	@RequestMapping("editView")
	public ModelAndView editView (@RequestParam(value="group_id",required=false) String group_id) {
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
		filter.put("group_id", group_id);
		// query
		Group group = (Group) groupDao
								.filter(filter)
								.queryOne();
		
		List<Module> thisModules = new ArrayList<Module>();
		if (group != null 
			&& group.getAuthorities() != null 
			&& group.getAuthorities().isEmpty() == false) {
			// cycle
			Iterator<Authority> iterator = group.getAuthorities().iterator();
			group.setModules(new ArrayList<Module>());
			// cycle
			while (iterator.hasNext()) {
				Authority authority = iterator.next();
				
				if (authority == null 
					|| authority.getModule_id().trim().length() == 0) {
					continue;
				}
				
				Integer index = group.getAuthorities().indexOf(authority);  
				
				// {"is_del":{"$ne":true}，"is_forbidden":{"$ne":true}，"module_id":{"$eq":"xxxxx}}
				Object[][]  isFilter    = {{"is_del","is_forbidden", "module_id"},{"$ne","$ne","$eq"},{true,true,authority.getModule_id()}};
				filter                  = Tool.arrayToMap(isDelFilter);
				
				// module
				Module module = (Module) moduleDao.filter(filter).queryOne();
				
				// set value
				group.getAuthorities().get(index).setModule_name(module.getName());
				group.getModules().add(module);
			}
		}
		
		models().put("this", group);
		models().put("modules", modules);
		return view("zh/group_editView");
	}
	
	@RequestMapping(value="loadGroupData")
	public ModelAndView loadGroupData (@RequestParam(value="data", required=false) String data) {
		
		// request parameters
		List<DataTable> dataTables = (List<DataTable>) dealJsonData(data, DataTable.class, true, "columns");
		Search          search     = (Search)          dealJsonData(data, Search.class,    false);
		List<Order>     orders     = (List<Order>)     dealJsonData(data, Order.class,     true,  "order");
		
		if(search == null
			|| search.getStart() < 0
			|| search.getLength() < 1
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// sort
		Map<String, Object> sort       = new HashMap<String, Object>();
		String[]            orderField = {"name", "remark", "add_time","is_forbidden"}; 
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
			
			Object[][] searchArray = {{"name","remark"},{"$regex","$regex"},{value,value}};
			List<Object> serachList = Tool.arrayXMap(searchArray);
			
			filter.put("$or", serachList);
		}
		
		// query fields
		String[] includes = {"group_id","name","remark","add_time","is_forbidden"};		
		// query currency
		List<Document> groups = groupDao
				.filter(filter)
				.include(includes)
				.skip(search.getStart())
				.sort(sort)
				.limit(search.getLength())
				.queryDocument();
		
		Long totle = groupDao.count();
		
		// set json data
		setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_LOAD_ZH, Errors.ERROR_CODE_NONE);
		
		jsons().put("draw", search.getDraw());
		jsons().put("data", groups);
		jsons().put("recordsTotal", totle);
		jsons().put("recordsFiltered", totle);
		
		return json();
	}
	
	@RequestMapping("addGroup")
	public ModelAndView addGroup (@RequestParam(value="data", required=false) String data) {
		// get the parameters
		Group group = (Group) JsonConvert.jsonToObject(Group.class, data);
		if(group == null 
			|| group.getName() == null
			|| group.getRemark() == null
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_NONE);
			return json(jsons());
		}
		
		// currency id
		String group_id = getIDS("group_id");
		group.setGroup_id(group_id);
		// add time
		Date add_time = new Date();
		group.setAdd_time(add_time);
		
		// filter field
		String[] includeFields = {"group_id","name","remark","authorities","add_time","is_forbidden"};
		
		Boolean flag = false;
		// save object
		try {
			flag = groupDao.include(includeFields).data(group).insert();
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
	
	@RequestMapping("editGroup")
	public ModelAndView editGroup (@RequestParam("data") String data) {
		// get the parameters
		Group group = (Group) JsonConvert.jsonToObject(Group.class, data);
		
		if (group == null
			|| group.getName() == null
			|| group.getRemark() == null 
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// forbidden setting
		if (group.getIs_forbidden() == null) {
			group.setIs_forbidden(false);
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("group_id", group.getGroup_id());
		
		// query whether exists
		Group groupData = (Group) groupDao.filter(filter).queryOne();
		
		// data not exists
		if(groupData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		// update
		String[] include = {"name", "remark", "authorities", "is_forbidden"};
		Boolean status = groupDao.filter(filter).data(group).include(include).update();
		
		if(status == true) {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_UPDATE);
		}
		
		return json();
	}
	
	@RequestMapping("deleteGroup")
	public ModelAndView deleteGroup (@RequestParam(value="data",required=false) String data) {
		Group group = (Group) dealJsonData(data, Group.class, false);
		if (group.getGroup_id() == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("group_id", group.getGroup_id());
		
		// query whether exists
		Brand brandData = (Brand) brandDao.filter(filter).queryOne();
		
		// data not exists
		if(brandData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		String[] include = {"is_del"};
		// delete currency
		group.setIs_del(true);
		
		Boolean status = groupDao.filter(filter).include(include).data(group).update();
		
		if (status == true) {
			setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_DELETE);
		}
		
		return json();
	}
	
	@RequestMapping("existGroupField")
	public ModelAndView existGroupField (@RequestParam(value="data",required=false) String data) {
		Group group = (Group) dealJsonData(data, Group.class, false);
		
		Map<String, Object> filter = new HashMap<String, Object>();
		
		if (group != null && group.getGroup_id() != null) {
			filter.put("group_id", group.getGroup_id());
		}
		else if (group != null && group.getName() != null) {
			filter.put("name", group.getName());
		}
		else {
			
		}
		
		// query
		if (filter.isEmpty() == false) {
			Group group2 = (Group) groupDao.filter(filter).queryOne();
			if (group2 == null) {
				jsons().put("exist", false);
			}
			else {
				jsons().put("exist", true);
			}
		}
		
		setJson(true, Errors.ERROR_MESSAGE_SELECT_ZH, Errors.ERROR_CODE_NONE);
		
		return json();
	}
}
