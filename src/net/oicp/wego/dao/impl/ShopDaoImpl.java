package net.oicp.wego.dao.impl;

import org.springframework.stereotype.Service;

import net.oicp.wego.dao.ShopDao;
import net.oicp.wego.model.Shop;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.08.26
 * @company        WEGO
 * @description    shop data operate implement
*/
@Service("shopDao")
public class ShopDaoImpl extends MongoDaoImpl<Shop> implements ShopDao {

}
