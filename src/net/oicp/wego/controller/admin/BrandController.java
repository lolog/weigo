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
import net.oicp.wego.model.Brand;
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
@RequestMapping("/admin/brand/")
@SuppressWarnings({"unchecked", "unused"})
public class BrandController extends AdminBaseController {
	
	@RequestMapping("manageView")
	public ModelAndView manageView () {
		return view("zh/brand_manageView");
	}
	
	@RequestMapping(value="loadBrandData")
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
			|| search.getLength() < 1
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// sort
		Map<String, Object> sort       = new HashMap<String, Object>();
		String[]            orderField = {"name", "information", "add_time","is_forbidden"}; 
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
		String[] includes = {"brand_id","name","information","add_time","is_forbidden"};		
		// query currency
		List<Document> currencys = brandDao
				.filter(filter)
				.include(includes)
				.skip(search.getStart())
				.sort(sort)
				.limit(search.getLength())
				.queryDocument();
		
		Long totle = brandDao.count();
		
		// set json data
		setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_LOAD_ZH, Errors.ERROR_CODE_NONE);
		
		jsons().put("draw", search.getDraw());
		jsons().put("data", currencys);
		jsons().put("recordsTotal", totle);
		jsons().put("recordsFiltered", totle);
		
		return json();
	}
	
	@RequestMapping("addBrand")
	public ModelAndView addBrand (@RequestParam(value="data", required=false) String data) {
		// get the parameters
		Brand brand = (Brand) JsonConvert.jsonToObject(Brand.class, data);
		if(brand == null 
			|| brand.getName() == null
			|| brand.getInformation() == null
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_NONE);
			return json(jsons());
		}
		
		// currency id
		String brand_id = getIDS("brand_id");
		brand.setBrand_id(brand_id);
		// add time
		Date add_time = new Date();
		brand.setAdd_time(add_time);
		
		// filter field
		String[] includeFields = {"brand_id","name","information","add_time","is_forbidden"};
		
		Boolean flag = false;
		// save object
		try {
			flag = brandDao.include(includeFields).data(brand).insert();
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
	
	@RequestMapping("editBrand")
	public ModelAndView editBrand (@RequestParam("data") String data) {
		// get the parameters
		Brand brand = (Brand) JsonConvert.jsonToObject(Brand.class, data);
		
		if (brand == null
			|| brand.getName() == null
			|| brand.getInformation() == null 
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// forbidden setting
		if (brand.getIs_forbidden() == null) {
			brand.setIs_forbidden(false);
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("brand_id", brand.getBrand_id());
		
		// query whether exists
		Brand brandData = (Brand) brandDao.filter(filter).queryOne();
		
		// data not exists
		if(brandData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		// update
		String[] include = {"name", "information", "is_forbidden"};
		Boolean status = brandDao.filter(filter).data(brand).include(include).update();
		
		if(status == true) {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_UPDATE);
		}
		
		return json();
	}
	
	@RequestMapping("deleteBrand")
	public ModelAndView deleteBrand (@RequestParam("data") String data) {
		Brand brand = (Brand) dealJsonData(data, Brand.class, false);
		if (brand.getBrand_id() == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("brand_id", brand.getBrand_id());
		
		// query whether exists
		Brand brandData = (Brand) brandDao.filter(filter).queryOne();
		
		// data not exists
		if(brandData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		String[] include = {"is_del"};
		// delete currency
		brand.setIs_del(true);
		
		Boolean status = brandDao.filter(filter).include(include).data(brand).update();
		
		if (status == true) {
			setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_DELETE);
		}
		
		return json();
	}
}
