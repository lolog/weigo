package net.oicp.wego.dao.impl;

import org.springframework.stereotype.Service;

import net.oicp.wego.dao.LabelDao;
import net.oicp.wego.model.Label;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.08.28
 * @company        WEGO
 * @description    label dao implement
*/
@Service("labelDao")
public class LabelDaoImpl extends MongoDaoImpl<Label> implements LabelDao {

}
