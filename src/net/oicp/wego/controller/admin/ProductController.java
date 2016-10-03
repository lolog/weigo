package net.oicp.wego.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import net.oicp.wego.controller.AdminBaseController;
import net.oicp.wego.model.Brand;
import net.oicp.wego.model.Currency;
import net.oicp.wego.model.Label;
import net.oicp.wego.model.Product;
import net.oicp.wego.model.ProductClassify;
import net.oicp.wego.model.Shop;
import net.oicp.wego.model.append.Festival;
import net.oicp.wego.model.parameter.DataTable;
import net.oicp.wego.model.parameter.Order;
import net.oicp.wego.model.parameter.Search;
import net.oicp.wego.tools.Errors;
import net.oicp.wego.tools.JsonConvert;
import net.oicp.wego.tools.Tool;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.08.27
 * @company        WEGO
 * @description    product controller
*/
@Controller
@RequestMapping("/admin/product/")
@SuppressWarnings({"unchecked", "unused"})
public class ProductController extends AdminBaseController {
	@RequestMapping("/manageView")
	public ModelAndView manageView () {
		return view("zh/product_manageView");
	}
	@RequestMapping("/addView")
	public ModelAndView addView () {
		// {"is_del":{"$ne":true}}
		Map<String, Object> filter = new HashMap<String, Object>();
		Object[][]          isDelFilter        = {{"$ne"}, {true}};
		filter.put("is_del", Tool.arrayToMap(isDelFilter));
		filter.put("is_forbidden", Tool.arrayToMap(isDelFilter));
		
		// includes
		String[] include = {"classify_id","name"};
		// product classify
		List<ProductClassify> productClassifies = productClassifyDao.filter(filter).include(include).query();
		// currency
		include[0] = "currency_id";
		List<Currency> currencies = currencyDao.filter(filter).include(include).query();
		// brands
		include[0] = "brand_id";
		List<Brand> brands = brandDao.filter(filter).include(include).query();
		// festival
		include[0] = "festival_id";
		List<Festival> festivals = festivalDao.filter(filter).query();
		// label
		include[0] = "label_id";
		List<Label> labels = labelDao.filter(filter).include(include).query();
		// shop
		include[0] = "shop_id";
		List<Shop> shops = shopDao.filter(filter).include(include).query();
		
		models().put("productClassifies", productClassifies);
		models().put("currencies", currencies);
		models().put("brands", brands);
		models().put("festivals", festivals);
		models().put("labels", labels);
		models().put("shops", shops);
		
		// value
		Map<String, Object> oMap = new HashMap<String, Object>();
		oMap.put("productClassifies", productClassifies);
		oMap.put("currencies", currencies);
		oMap.put("brands", brands);
		oMap.put("festivals", festivals);
		oMap.put("labels", labels);
		oMap.put("shops", shops);
		
		JSONObject jsonObject = new JSONObject(oMap);
		models().put("OBJ", jsonObject);
		
		return view("zh/product_addView");
	}
	
	@RequestMapping(value="editView")
	public ModelAndView editView (@RequestParam(value="product_id", required=false) String product_id) {
		if (product_id == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			try {
				response().sendRedirect("manageView.html");
			} catch (Exception e) {
				logger.error(e);
			}
			return null;
		}
		
		// {"is_del":{"$ne":true}}
		Map<String, Object> filter = new HashMap<String, Object>();
		Object[][]          isNotDelFilter        = {{"$ne"}, {true}};
		filter.put("is_del", Tool.arrayToMap(isNotDelFilter));
		filter.put("is_forbidden", Tool.arrayToMap(isNotDelFilter));
		filter.put("product_id", product_id);
		
		// query
		Product product = (Product) productDao.filter(filter).queryOne();
		if (product == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			try {
				response().sendRedirect("manageView.html");
			} catch (Exception e) {
				logger.error(e);
			}
			return null;
		}
		
		filter.remove("product_id");
		// includes
		String[] include = {"classify_id","name"};
		// product classify
		List<ProductClassify> productClassifies = productClassifyDao.filter(filter).include(include).query();
		// currency
		include[0] = "currency_id";
		List<Currency> currencies = currencyDao.filter(filter).include(include).query();
		// brands
		include[0] = "brand_id";
		List<Brand> brands = brandDao.filter(filter).include(include).query();
		// festival
		include[0] = "festival_id";
		List<Festival> festivals = festivalDao.filter(filter).query();
		// label
		include[0] = "label_id";
		List<Label> labels = labelDao.filter(filter).include(include).query();
		// shop
		include[0] = "shop_id";
		List<Shop> shops = shopDao.filter(filter).include(include).query();

		models().put("productClassifies", productClassifies);
		models().put("currencies", currencies);
		models().put("products", product);
		models().put("brands", brands);
		models().put("festivals", festivals);
		models().put("labels", labels);
		models().put("shops", shops);
		
		models().put("product_id", product_id);
		
		// value
		Map<String, Object> oMap = new HashMap<String, Object>();
		oMap.put("productClassifies", productClassifies);
		oMap.put("currencies", currencies);
		oMap.put("brands", brands);
		oMap.put("festivals", festivals);
		oMap.put("labels", labels);
		oMap.put("shops", shops);
		
		JSONObject jsonObject = new JSONObject(oMap);
		models().put("OBJ", jsonObject);
		
		return view("zh/product_editView");
	}
	
	@RequestMapping(value="scanView")
	public ModelAndView scanView (@RequestParam(value="product_id", required=false) String product_id) {
		if (product_id == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			try {
				response().sendRedirect("manageView.html");
			} catch (Exception e) {
				logger.error(e);
			}
			return null;
		}
		
		// {"is_del":{"$ne":true}}
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("product_id", product_id);
		
		// query
		Product product = (Product) productDao.filter(filter).queryOne();
		if (product == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			try {
				response().sendRedirect("manageView.html");
			} catch (Exception e) {
				logger.error(e);
			}
			return null;
		}
		
		// currency
		if (product.getCurrency_id() != null) {
			filter.clear();
			filter.put("currency_id", product.getCurrency_id());
			// currency query
			Currency currency = (Currency) currencyDao.filter(filter).queryOne();
			if (currency != null) {
				product.setCurrency_name(currency.getName());
			}
		}
		
		// festival
		if (product.getFestivals() != null && product.getFestivals().isEmpty() == false) {
			filter.clear();
			int index = 0;
			Iterator<Festival> iterator = product.getFestivals().iterator();
			while (iterator.hasNext()) {
				Festival festival = iterator.next();
				if (festival != null 
					&& festival.getFestival_id() !=  null
					&& festival.getFestival_id().length() > 0) {
					filter.put("festival_id", festival.getFestival_id());
					net.oicp.wego.model.Festival f = (net.oicp.wego.model.Festival) festivalDao.filter(filter).queryOne();
					if (f != null ) {
						product.getFestivals().get(index++).setName(f.getName());;
					}
				}
			}
		}
		
		// shops
		if (product.getShops() != null && product.getShops().isEmpty() == false) {
			filter.clear();
			int index = 0;
			Iterator<Shop> iterator = product.getShops().iterator();
			while (iterator.hasNext()) {
				Shop festival = iterator.next();
				if (festival != null && festival.getShop_id() !=  null) {
					filter.put("shop_id", festival.getShop_id());
					Shop shop = (Shop) shopDao.filter(filter).queryOne();
					product.getShops().get(index++).setName(shop.getName());;
				}
			}
		}
		
		models().put("products", product);
		
		return view("zh/product_scanView");
	}
	
	@RequestMapping(value="loadProductData")
	public ModelAndView loadProductData (@RequestParam(value="data", required=false) String data) {
		
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
		Map<String, Object> sort = new HashMap<String, Object>();
		String[] orderField = {"name", "classify_name", "price", "stock", "add_time", "is_recommand", "is_forbidden"}; 
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
			
			String  value        = search.getSearch().get("value").toString();
			Boolean regex        = (Boolean) search.getSearch().get("regex");
			Double  priceOrStock = Tool.stringToDouble(value);
			// name
			Map<String, Object> regexMap  = null;
			
			if (priceOrStock == null) {
				Object[][]   searchArray = {
												{"name", "classify_name", "price", "stock"},
												{"$regex","$regex","$regex","$regex"},
												{value,value, value, value}
											};
				List<Object> serachList  = Tool.arrayXMap(searchArray);
				
				filter.put("$or", serachList);
			}
			else {
				Object[][]          searchArray = {{"$regex"},{value}};
				Map<String, Object> serachMap   = Tool.arrayToMap(searchArray);
				
				filter.put("name", serachMap);
			}
		}
		
		// query fields
		String[] includes = {"product_id", "name", "classify_name", "price", "stock", "add_time", "is_forbidden"};		
		// query currency
		List<Document> shops = productDao
				.filter(filter)
				.include(includes)
				.skip(search.getStart())
				.sort(sort)
				.limit(search.getLength())
				.queryDocument();
		
		Long totle = productDao.count();
		
		// set json data
		setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_LOAD_ZH, Errors.ERROR_CODE_NONE);
		
		jsons().put("draw", search.getDraw());
		jsons().put("data", shops);
		jsons().put("recordsTotal", totle);
		jsons().put("recordsFiltered", totle);
		
		return json();
	}
	
	@RequestMapping("addProduct")
	public ModelAndView addProduct (@RequestParam(value="data", required=false) String data) {
		// get the parameters
		Product product = (Product) JsonConvert.jsonToObject(Product.class, data);
		
		if(product == null 
			|| product.getName() == null
			|| product.getTitle() == null
			|| product.getCurrency_id() == null
			|| product.getCurrency_price() == null
			|| product.getStock() == null
			|| product.getDescription() == null
			|| product.getDiscount() == null
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_NONE);
			return json(jsons());
		}
		
		// count 
		product.setCount(product.getStock());
		// cost count
		product.setCost_count(0);
		// price
		Double price = product.getCurrency_price() * product.getCurrency_rate();
		price = (product.getDiscount() == null) ? price : (price - product.getDiscount());
		product.setPrice(price);
		
		Map<String, Object> filter = new HashMap<String, Object>();
		// filter
		Object[][] filterArray = {{"$ne"}, {true}};
		filter.put("is_del", Tool.arrayToMap(filterArray));
		filter.put("is_forbidden", Tool.arrayToMap(filterArray));
		
		// classify
		filter.put("classify_id", product.getClassify_id());
		
		ProductClassify productClassify = (ProductClassify) productClassifyDao.filter(filter).queryOne();
		if (productClassify == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		product.setClassify_name(productClassify.getName());
		
		// brand
		filter.remove("classify_id");
		filter.put("brand_id", product.getBrand_id());
		Brand brand = (Brand) brandDao.filter(filter).queryOne();
		if (brand == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		product.setBrand_name(brand.getName());
		
		// currency id
		String product_id = getIDS("product_id");
		product.setProduct_id(product_id);
		// add time
		Date add_time = new Date();
		product.setAdd_time(add_time);
		
		// filter field
		String[] excludeFields = {"count","cost_count","editors","is_del"};
		
		Boolean flag = false;
		// save object
		try {
			flag = productDao.exclude(excludeFields).data(product).insert();
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
	
	@RequestMapping("editProduct")
	public ModelAndView editProduct (@RequestParam("data") String data) {
		// get the parameters
		Product product = (Product) JsonConvert.jsonToObject(Product.class, data);
		
		if (product == null
			|| product.getProduct_id() == null
			|| product.getName() == null
			|| product.getTitle() == null
			|| product.getCurrency_id() == null
			|| product.getCurrency_price() == null
			|| product.getStock() == null
			|| product.getDescription() == null
			|| product.getDiscount() == null
		) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// price
		Double price = product.getCurrency_price() * product.getCurrency_rate();
		price = (product.getDiscount() == null) ? price : (price - product.getDiscount());
		product.setPrice(price);
		
		Map<String, Object> filter = new HashMap<String, Object>();
		// filter
		Object[][] filterArray = {{"$ne"}, {true}};
		filter.put("is_del", Tool.arrayToMap(filterArray));
		filter.put("is_forbidden", Tool.arrayToMap(filterArray));
		
		// classify
		filter.put("classify_id", product.getClassify_id());
		
		ProductClassify productClassify = (ProductClassify) productClassifyDao.filter(filter).queryOne();
		if (productClassify == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		product.setClassify_name(productClassify.getName());
		
		// brand
		filter.remove("classify_id");
		filter.put("brand_id", product.getBrand_id());
		Brand brand = (Brand) brandDao.filter(filter).queryOne();
		if (brand == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		product.setBrand_name(brand.getName());
		
		// add time
		Date add_time = new Date();
		product.setAdd_time(add_time);
		
		filter.clear();
		filter.put("product_id", product.getProduct_id().trim());
		// query whether exists
		Product productData = (Product) productDao.filter(filter).queryOne();
		
		// data not exists
		if(productData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		// count
		Integer count = productData.getCount() == null ? productData.getStock() : productData.getCount();
		// cost_count
		Integer cost_count = count - product.getStock();
		
		count      = count + productData.getStock() - product.getStock();
		count      = count > 0 ? count : 0;
		cost_count = cost_count > 0 ? cost_count : 0;
		
		product.setCount(count);
		product.setCost_count(cost_count);
		
		String[] excludeFields = {"is_del"};
		
		Boolean status = false;
		
		try {
			status = productDao.filter(filter).data(product).exclude(excludeFields).update();
		} catch (Exception e) {
			
		}
		
		if(status == true) {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_UPDATE_ZH, Errors.ERROR_CODE_UPDATE);
		}
		
		return json();
	}
	
	@RequestMapping("deleteProduct")
	public ModelAndView deleteProduct (@RequestParam("data") String data) {
		Product product = (Product) dealJsonData(data, Product.class, false);
		if (product.getProduct_id() == null
			|| product.getProduct_id().length() < 1) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_PARAMETERS_ZH, Errors.ERROR_CODE_PARAMETER);
			return json();
		}
		
		// condition
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("product_id", product.getProduct_id());
		
		// query whether exists
		Product productData = (Product) productDao.filter(filter).queryOne();
		
		// data not exists
		if(productData == null) {
			setJson(Errors.ERROR, Errors.ERROR_MESSAGE_DATA_NOT_EXISTS_ZH, Errors.ERROR_CODE_DATA_NOT_EXISTS);
			return json();
		}
		
		String[] include = {"is_del"};
		// delete currency
		product.setIs_del(true);
		
		Boolean status = productDao.filter(filter).include(include).data(product).update();
		
		if (status == true) {
			setJson(Errors.SUCCESS, Errors.SUCCESS_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_NONE);
		}
		else {
			setJson(Errors.SUCCESS, Errors.ERROR_MESSAGE_DELETE_ZH, Errors.ERROR_CODE_DELETE);
		}
		
		return json();
	}
}
