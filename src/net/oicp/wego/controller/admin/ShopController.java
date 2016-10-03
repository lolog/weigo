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
import net.oicp.wego.model.Shop;
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
 * @Company        WEGO
 * @Description    Currency Setting
*/
@Controller
@RequestMapping("/admin/shop/")
@SuppressWarnings({"unchecked", "unused"})
public class ShopController extends AdminBaseController {
	
	@RequestMapping("manageView")
	public ModelAndView manageView () {
		return view("zh/shop_manageView");
	}
	
	@RequestMapping(value="loadShopData")
	public ModelAndView loadCurrencyData (@RequestParam(value="data", required=false) String data) {
		
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
		String[] orderField = {"name", "address", "information", "add_time","is_forbidden"}; 
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
			
			Object[][]   searchArray = {{"name", "address", "information"},{"$regex","$regex", "$regex"},{value, value, value}};
			List<Object> serachList  = Tool.arrayXMap(searchArray);
			
			filter.put("$or", serachList);
		}
		
		// query fields
		String[] includes = {"shop_id", "name", "address", "information", "add_time", "is_forbidden"};		
		// query currency
		List<Document> shops = shopDao
				.filter(filter)
				.include(includes)
				.skip(search.getStart())
				.sort(sort)
				.limit(search.getLength())
				.queryDocument();
		
		Long totle = shopDao.count();
		
		// set json data
		setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_LOAD_ZH, Errors.ERROR_CODE_NONE);
		
		jsons().put("draw", search.getDraw());
		jsons().put("data", shops);
		jsons().put("recordsTotal", totle);
		jsons().put("recordsFiltered", totle);
		
		return json();
	}
	
	@RequestMapping("addShop")
	public ModelAndView addShop (@RequestParam(value="data", required=false) String data) {
		// get the parameters
		Shop shop = (Shop) JsonConvert.jsonToObject(Shop.class, data);
		if(shop == null 
				|| shop.getName() == null
				|| shop.getAddress() == null
				|| shop.getInformation() == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_NONE);
			return json(jsons());
		}
		
		// currency id
		String shop_id = getIDS("shop_id");
		shop.setShop_id(shop_id);
		// add time
		Date add_time = new Date();
		shop.setAdd_time(add_time);
		
		// filter field
		String[] includeFields = {"shop_id","name","address", "information", "add_time","is_forbidden"};
		
		Boolean flag = false;
		// save object
		try {
			flag = shopDao.include(includeFields).data(shop).insert();
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
	
	@RequestMapping("editShop")
	public ModelAndView editShop (@RequestParam("data") String data) {
		// get the parameters
		Shop shop = (Shop) JsonConvert.jsonToObject(Shop.class, data);
		
		if (shop.getShop_id() == null
			|| shop.getName() == null 
			|| shop.getAddress() == null
			|| shop.getInformation() == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// forbidden setting
		if (shop.getIs_forbidden() == null) {
			shop.setIs_forbidden(false);
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("shop_id", shop.getShop_id());
		
		// query whether exists
		Shop shopData = (Shop) shopDao.filter(filter).queryOne();
		
		// data not exists
		if(shopData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		// update
		String[] include = {"name", "address", "information", "is_forbidden"};
		Boolean status = shopDao.filter(filter).data(shop).include(include).update();
		
		if(status == true) {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_UPDATE);
		}
		
		return json();
	}
	
	@RequestMapping("deleteShop")
	public ModelAndView deleteShop (@RequestParam("data") String data) {
		Shop shop = (Shop) dealJsonData(data, Shop.class, false);
		if (shop.getShop_id() == null
			|| shop.getShop_id().length() < 1) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("shop_id", shop.getShop_id());
		
		// query whether exists
		Shop shopData = (Shop) shopDao.filter(filter).queryOne();
		
		// data not exists
		if(shopData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		String[] include = {"is_del"};
		// delete currency
		shop.setIs_del(true);
		
		Boolean status = shopDao.filter(filter).include(include).data(shop).update();
		
		if (status == true) {
			setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_DELETE);
		}
		
		return json();
	}
}
