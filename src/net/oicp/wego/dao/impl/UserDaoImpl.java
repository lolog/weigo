package net.oicp.wego.dao.impl;

import org.springframework.stereotype.Service;

import net.oicp.wego.dao.UserDao;
import net.oicp.wego.model.User;

@Service("userDao")
public class UserDaoImpl extends MongoDaoImpl<User> implements UserDao {
	
}
