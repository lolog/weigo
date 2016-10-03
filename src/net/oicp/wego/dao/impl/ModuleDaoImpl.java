package net.oicp.wego.dao.impl;

import org.springframework.stereotype.Service;

import net.oicp.wego.dao.ModuleDao;
import net.oicp.wego.model.Module;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.09.24
 * @company        WEGO
 * @description    module base data operate implement
*/
@Service("moduleDao")
public class ModuleDaoImpl extends MongoDaoImpl<Module> implements ModuleDao {

}
