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
import net.oicp.wego.model.Currency;
import net.oicp.wego.model.Label;
import net.oicp.wego.model.parameter.DataTable;
import net.oicp.wego.model.parameter.Order;
import net.oicp.wego.model.parameter.Search;
import net.oicp.wego.tools.Errors;
import net.oicp.wego.tools.JsonConvert;
import net.oicp.wego.tools.Tool;

/** 
 * @author         lolog
 * @version        V1.0  
 * @Date           2016.08.14
 * @Company        WEIGO
 * @Description    Label Setting
*/
@Controller
@RequestMapping("/admin/label/")
@SuppressWarnings({"unchecked", "unused"})
public class LabelController extends AdminBaseController {
	
	@RequestMapping("manageView")
	public ModelAndView manageView () {
		return view("zh/label_manageView");
	}
	
	@RequestMapping(value="loadLabelData")
	public ModelAndView loadLabelData (@RequestParam(value="data", required=false) String data) {
		
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
		Map<String, Object> sort = new HashMap<String, Object>();
		String[] orderField = {"name", "information", "add_time","is_forbidden"}; 
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
			
			Object[][] searchArray = {{"name","information"},{"$regex","$eq"},{value,value}};
			List<Object> serachList = Tool.arrayXMap(searchArray);
			
			filter.put("$or", serachList);
		}
		
		// query fields
		String[] includes = {"label_id","name","information","add_time","is_forbidden"};		
		// query currency
		List<Document> currencys = labelDao
				.filter(filter)
				.include(includes)
				.skip(search.getStart())
				.sort(sort)
				.limit(search.getLength())
				.queryDocument();
		
		Long totle = labelDao.count();
		
		// set json data
		setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_LOAD_ZH, Errors.ERROR_CODE_NONE);
		
		jsons().put("draw", search.getDraw());
		jsons().put("data", currencys);
		jsons().put("recordsTotal", totle);
		jsons().put("recordsFiltered", totle);
		
		return json();
	}
	
	@RequestMapping("addLabel")
	public ModelAndView addCurrency (@RequestParam(value="data", required=false) String data) {
		// get the parameters
		Label label = (Label) JsonConvert.jsonToObject(Label.class, data);
		if(label == null 
				|| label.getName() == null
				|| label.getInformation() == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_NONE);
			return json(jsons());
		}
		
		// currency id
		String label_id = getIDS("label_id");
		label.setLabel_id(label_id);
		// add time
		Date add_time = new Date();
		label.setAdd_time(add_time);
		
		// filter field
		String[] includeFields = {"label_id","name","information","add_time","is_forbidden"};
		
		Boolean flag = false;
		// save object
		try {
			flag = labelDao.include(includeFields).data(label).insert();
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
	
	@RequestMapping("editLabel")
	public ModelAndView editLabel (@RequestParam("data") String data) {
		// get the parameters
		Label label = (Label) JsonConvert.jsonToObject(Label.class, data);
		
		if (label == null
			|| label.getName() == null
			|| label.getInformation() == null 
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// forbidden setting
		if (label.getIs_forbidden() == null) {
			label.setIs_forbidden(false);
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("label_id", label.getLabel_id());
		
		// query whether exists
		Label labelData = (Label) labelDao.filter(filter).queryOne();
		
		// data not exists
		if(labelData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		// update
		String[] include = {"name", "information", "is_forbidden"};
		Boolean status = labelDao.filter(filter).data(label).include(include).update();
		
		if(status == true) {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_UPDATE);
		}
		
		return json();
	}
	
	@RequestMapping("deleteLabel")
	public ModelAndView deleteLabel (@RequestParam("data") String data) {
		Label label = (Label) dealJsonData(data, Label.class, false);
		if (label.getLabel_id() == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("label_id", label.getLabel_id());
		
		// query whether exists
		Label labelData = (Label) labelDao.filter(filter).queryOne();
		
		// data not exists
		if(labelData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		String[] include = {"is_del"};
		// delete currency
		label.setIs_del(true);
		
		Boolean status = labelDao.filter(filter).include(include).data(label).update();
		
		if (status == true) {
			setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_DELETE);
		}
		
		return json();
	}
}
