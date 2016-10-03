package net.oicp.wego.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.oicp.wego.dao.UserDao;
import net.oicp.wego.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/app.xml")
public class UserDaoTest {
	
	public static void main(String[] args) {
		String admin = "/admin/";
		System.out.println(admin.substring(1, admin.length()));
	}
	
	@Resource
	private UserDao userDao;

	@Test
	public void insetOneTest () {
		User user = new User();
		user.setId(1);
		user.setIndex("2");
		user.setName("2");
		
		List<net.oicp.wego.model.Test> tests = new ArrayList<net.oicp.wego.model.Test>();
		net.oicp.wego.model.Test test = new net.oicp.wego.model.Test();
		test.setIndex("2");
		net.oicp.wego.model.Test test1 = new net.oicp.wego.model.Test();
		test1.setIndex("2");
		tests.add(test);
		tests.add(test1);
		
		user.setList(tests);
		
		Boolean stauts = userDao.data(user).insert();
		System.out.println(stauts);
	}
	
	@Test
	public void selectTest () {
		List<User> users = userDao.query();
		System.out.println(users);
	}
	
	@Test
	public void countTest () {
		long count = userDao.collection("user").count();
		System.out.println(count);
	}
	
	public void testTest (User user) {
		System.out.println(user.getClass() == User.class);
	}
	
	@Test
	public void createIndexTest () {
		userDao.createIndex();
	}
	
	@Test
	public void queryIndexTest () {
		Map<String, Object> map = userDao.queryIndex();
		System.out.println(map);
		
	}
	
	@Test
	public void getCollectionNameTest () {
		System.out.println(userDao.getCollectionName());
	}
	
	@Test
	public void getCollectionTest () {
	}
	
}
