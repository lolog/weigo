package net.oicp.wego.dao.impl;

import org.springframework.stereotype.Service;

import net.oicp.wego.dao.BrandDao;
import net.oicp.wego.model.Brand;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.08.28
 * @company        WEGO
 * @description    product dao implement
*/
@Service("brandDao")
public class BrandDaoImpl extends MongoDaoImpl<Brand> implements BrandDao {

}
