package net.oicp.wego.dao.impl;

import org.springframework.stereotype.Service;

import net.oicp.wego.dao.FestivalDao;
import net.oicp.wego.model.Festival;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.08.25
 * @company        WEGO
 * @description    festival data implement 
*/
@Service("festivalDao")
public class FestivalDaoImpl extends MongoDaoImpl<Festival> implements FestivalDao {
	
}
