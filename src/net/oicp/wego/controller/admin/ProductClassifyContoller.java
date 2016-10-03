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
 * @Description    product classify
*/
@Controller
@SuppressWarnings({"unchecked", "unused"})
@RequestMapping("/admin/productClassify/")
public class ProductClassifyContoller extends AdminBaseController {
	@RequestMapping(value="manageView")
	public ModelAndView manageView () {
		return view("zh/productClassify_manageView");
	}
	
	@RequestMapping(value="loadProductClassify")
	public ModelAndView loadProductClassify (@RequestParam("data") String data) {
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
		String[]            orderField = {"name", "intro", "add_time","is_forbidden"}; 
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
			Object[][] nameArray = {{"name", "intro"}, {"$regex", "$regex"},{value, value}};
			filter.put("$or",  Tool.arrayXMap(nameArray));
		}
		
		// query fields
		String[] includes = {"classify_id","name","intro","add_time","is_forbidden"};		
		// query product classify
		List<Document> productClassifys = productClassifyDao
				.filter(filter)
				.include(includes)
				.skip(search.getStart())
				.sort(sort)
				.limit(search.getLength())
				.queryDocument();
		
		Long totle = productClassifyDao.count();
		
		// set json data
		setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_LOAD_ZH, Errors.ERROR_CODE_NONE);
		
		jsons().put("draw", search.getDraw());
		jsons().put("data", productClassifys);
		jsons().put("recordsTotal", totle);
		jsons().put("recordsFiltered", totle);
		
		return json();
	}
	
	@RequestMapping("addProductClassify")
	public ModelAndView addProductClassify (@RequestParam(value="data", required=false) String data) {
		// get the parameters
		ProductClassify productClassify = (ProductClassify) JsonConvert.jsonToObject(ProductClassify.class, data);
		if(productClassify == null 
			|| productClassify.getName() == null
			|| productClassify.getIntro() == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_NONE);
			return json(jsons());
		}
		
		// currency id
		String classify_id = getIDS("classify_id");
		productClassify.setClassify_id(classify_id);
		// add time
		Date add_time = new Date();
		productClassify.setAdd_time(add_time);
		
		// filter field
		String[] includeFields = {"classify_id","name","intro","add_time","is_forbidden"};
		
		Boolean flag = false;
		// save object
		try {
			flag = productClassifyDao.include(includeFields).data(productClassify).insert();
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
	
	@RequestMapping("editProductClassify")
	public ModelAndView editProductClassify (@RequestParam("data") String data) {
		// get the parameters
		ProductClassify productClassify = (ProductClassify) JsonConvert.jsonToObject(ProductClassify.class, data);
		
		if (productClassify.getClassify_id() == null
			|| productClassify.getName() == null 
			|| productClassify.getName().length() < 1
			|| productClassify.getIntro() == null
			|| productClassify.getIntro().length() < 1) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// forbidden setting
		if (productClassify.getIs_forbidden() == null) {
			productClassify.setIs_forbidden(false);
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("classify_id", productClassify.getClassify_id());
		
		// query whether exists
		ProductClassify productClassifyData = (ProductClassify) productClassifyDao.filter(filter).queryOne();
		
		// data not exists
		if(productClassify == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		// update
		String[] include = {"name", "intro", "is_forbidden"};
		Boolean status = productClassifyDao.filter(filter).data(productClassify).include(include).update();
		
		if(status == true) {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_UPDATE);
		}
		
		return json();
	}
	
	@RequestMapping("deleteProductClassify")
	public ModelAndView deleteProductClassify (@RequestParam("data") String data) {
		ProductClassify productClassify = (ProductClassify) dealJsonData(data, ProductClassify.class, false);
		if (productClassify.getClassify_id() == null
			|| productClassify.getClassify_id().length() < 1) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("classify_id", productClassify.getClassify_id());
		
		// query whether exists
		ProductClassify productClassifyData = (ProductClassify) productClassifyDao.filter(filter).queryOne();
		
		// data not exists
		if(productClassifyData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		String[] include = {"is_del"};
		// delete currency
		productClassify.setIs_del(true);
		
		Boolean status = productClassifyDao.filter(filter).include(include).data(productClassify).update();
		
		if (status == true) {
			setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_DELETE);
		}
		
		return json();
	}
}
