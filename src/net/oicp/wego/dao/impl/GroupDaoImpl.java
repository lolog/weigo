package net.oicp.wego.dao.impl;

import org.springframework.stereotype.Service;

import net.oicp.wego.dao.GroupDao;
import net.oicp.wego.model.Group;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.09.22
 * @company        WEGO
 * @description    group base data implement 
 */
@Service
public class GroupDaoImpl extends MongoDaoImpl<Group> implements GroupDao {

}
