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
import net.oicp.wego.dao.FestivalDao;
import net.oicp.wego.model.Currency;
import net.oicp.wego.model.Festival;
import net.oicp.wego.model.ProductClassify;
import net.oicp.wego.model.parameter.DataTable;
import net.oicp.wego.model.parameter.Order;
import net.oicp.wego.model.parameter.Search;
import net.oicp.wego.tools.Errors;
import net.oicp.wego.tools.JsonConvert;
import net.oicp.wego.tools.Tool;

/** 
 * @author         lolog
 * @version        V1.0  
 * @Date           2016.08.23
 * @Company        WEIGO
 * @Description    festival controller
*/
@Controller
@SuppressWarnings({"unchecked", "unused"})
@RequestMapping("/admin/festival/")
public class FestivalContoller extends AdminBaseController {
	@RequestMapping(value="manageView")
	public ModelAndView manageView () {
		return view("zh/festival_manageView");
	}
	
	@RequestMapping(value="loadFestival")
	public ModelAndView loadFestival (@RequestParam("data") String data) {
		// request parameters
		List<DataTable> dataTables = null;
		Search          search     = null;
		List<Order>     orders     = null;
		
		dataTables = (List<DataTable>) dealJsonData(data, DataTable.class, true, "columns");
		search     = (Search)          dealJsonData(data, Search.class,    false);
		orders     = (List<Order>)     dealJsonData(data, Order.class,     true,  "order");
		
		
		if(search == null
			|| search.getStart() < 0
			|| search.getLength() < 1) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// sort
		Map<String, Object> sort       = new HashMap<String, Object>();
		String[]            orderField = {"name", "start_string", "end_string","add_time","is_forbidden"}; 
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
			Object[][] nameArray = {{"name", "start_string","end_string"}, {"$regex", "$regex","$regex"},{value, value,value}};
			filter.put("$or",  Tool.arrayXMap(nameArray));
		}
		
		// query fields
		String[] includes = {"festival_id","name","start_string","end_string","add_time","is_forbidden"};		
		// query product classify
		List<Document> currencys = festivalDao
				.filter(filter)
				.include(includes)
				.skip(search.getStart())
				.sort(sort)
				.limit(search.getLength())
				.queryDocument();
		
		Long totle = festivalDao.count();
		
		// set json data
		setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_LOAD_ZH, Errors.ERROR_CODE_NONE);
		
		jsons().put("draw", search.getDraw());
		jsons().put("data", currencys);
		jsons().put("recordsTotal", totle);
		jsons().put("recordsFiltered", totle);
		
		return json();
	}
	
	@RequestMapping("addFestival")
	public ModelAndView addFestival (@RequestParam(value="data", required=false) String data) {
		// get the parameters
		Festival festival = (Festival) JsonConvert.jsonToObject(Festival.class, data);
		// date validate
		Date startTime = Tool.dateStringToDate("2016-"+festival.getStart_string(), "yyyy-MM-dd HH:mm:ss");
		Date endTime   = Tool.dateStringToDate("2016-"+festival.getEnd_string(), "yyyy-MM-dd HH:mm:ss");
		
		if(festival == null 
			|| festival.getName() == null
			|| startTime == null
			|| endTime   == null
			|| startTime.after(endTime)
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_NONE);
			return json(jsons());
		}
		
		
		// currency id
		String festival_id = getIDS("festival_id");
		festival.setFestival_id(festival_id);
		// add time
		Date add_time = new Date();
		festival.setAdd_time(add_time);
		
		// filter field
		String[] includeFields = {"festival_id","name","start_string","end_string","add_time","is_forbidden"};
		
		Boolean flag = false;
		// save object
		try {
			flag = festivalDao.include(includeFields).data(festival).insert();
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
	
	@RequestMapping("editFestival")
	public ModelAndView editFestival (@RequestParam("data") String data) {
		// get the parameters
		Festival festival = (Festival) JsonConvert.jsonToObject(Festival.class, data);
		// date validate
		Date startTime = Tool.dateStringToDate("2016-"+festival.getStart_string(), "yyyy-MM-dd HH:mm:ss");
		Date endTime   = Tool.dateStringToDate("2016-"+festival.getEnd_string(), "yyyy-MM-dd HH:mm:ss");
		
		if(festival == null 
			|| festival.getName() == null
			|| startTime == null
			|| endTime   == null
			|| startTime.after(endTime)
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_NONE);
			return json(jsons());
		}
		
		// forbidden setting
		if (festival.getIs_forbidden() == null) {
			festival.setIs_forbidden(false);
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("festival_id", festival.getFestival_id());
		
		// query whether exists
		Festival festivalData = (Festival) festivalDao.filter(filter).queryOne();
		
		// data not exists
		if(festivalData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		// update
		String[] include = {"name", "start_string", "end_string", "is_forbidden"};
		Boolean status = festivalDao.filter(filter).data(festival).include(include).update();
		
		if(status == true) {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_UPDATE);
		}
		
		return json();
	}
	
	@RequestMapping("deleteFestival")
	public ModelAndView deleteFestival (@RequestParam("data") String data) {
		Festival festival = (Festival) dealJsonData(data, Festival.class, false);
		if (festival.getFestival_id() == null
			|| festival.getFestival_id().length() < 1) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("festival_id", festival.getFestival_id());
		
		// query whether exists
		Festival festivalData = (Festival) festivalDao.filter(filter).queryOne();
		
		// data not exists
		if(festivalData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		String[] include = {"is_del"};
		// delete currency
		festival.setIs_del(true);
		
		Boolean status = festivalDao.filter(filter).include(include).data(festival).update();
		
		if (status == true) {
			setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_DELETE);
		}
		
		return json();
	}
}
