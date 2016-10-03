package net.oicp.wego.dao.impl;

import org.springframework.stereotype.Service;

import net.oicp.wego.dao.ProductDao;
import net.oicp.wego.model.Product;

/** 
 * @author         lolog
 * @version        V1.0  
 * @Date           2016.08.02
 * @Company        CIMCSSC
 * @Description    product data operate
*/
@Service("productDao")
public class ProductDaoImpl extends MongoDaoImpl<Product> implements ProductDao {
	
}