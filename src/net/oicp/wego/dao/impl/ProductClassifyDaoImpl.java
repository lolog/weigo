package net.oicp.wego.dao.impl;

import org.springframework.stereotype.Service;

import net.oicp.wego.dao.ProductClassifyDao;
import net.oicp.wego.model.ProductClassify;

/** 
 * @author         lolog
 * @version        v1.0  
 * @date           2016.08.23
 * @company        WEGO
 * @description    product classify Dao implement
*/
@Service("productClassifyDao")
public class ProductClassifyDaoImpl extends MongoDaoImpl<ProductClassify> implements ProductClassifyDao {

}
