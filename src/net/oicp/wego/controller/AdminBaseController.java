package net.oicp.wego.controller;

import org.springframework.beans.factory.annotation.Autowired;

import net.oicp.wego.dao.BrandDao;
import net.oicp.wego.dao.CurrencyDao;
import net.oicp.wego.dao.FestivalDao;
import net.oicp.wego.dao.GroupDao;
import net.oicp.wego.dao.LabelDao;
import net.oicp.wego.dao.ModuleDao;
import net.oicp.wego.dao.ProductClassifyDao;
import net.oicp.wego.dao.ProductDao;
import net.oicp.wego.dao.ShopDao;

/** 
 * @author         lolog
 * @version        V1.0  
 * @Date           2016.07.08
 * @Company        WEIGO
 * @Description    manager base controller
*/

public class AdminBaseController extends BaseController {
	@Autowired
	protected CurrencyDao currencyDao;
	@Autowired
	protected ProductClassifyDao productClassifyDao;
	@Autowired
	protected FestivalDao festivalDao;
	@Autowired
	protected ShopDao shopDao;
	@Autowired
	protected ProductDao productDao;
	@Autowired
	protected LabelDao labelDao;
	@Autowired
	protected BrandDao brandDao;
	@Autowired
	protected GroupDao groupDao;
	@Autowired
	protected ModuleDao moduleDao;
	
	public AdminBaseController() {
	}
}
