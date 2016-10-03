package net.oicp.wego.test;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.oicp.wego.dao.ProductClassifyDao;
import net.oicp.wego.model.ProductClassify;
import net.oicp.wego.tools.Tool;

/** 
 * @author         lolog
 * @version        v1.0  
 * @date           2016.08.23
 * @company        WEGO
 * @description    product classify test
*/

@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/app.xml")
public class ProductClassifyTest {
	@Autowired
	private ProductClassifyDao productClassifyDao;
	
	@Test
	public void insertTest () {
		ProductClassify productClassify = new ProductClassify();
		productClassify.setName("商家");
		productClassify.setIntro("商家简介");
		
		productClassify.setClassify_id("xxxx");
		Boolean status = productClassifyDao.data(productClassify).insert();
		System.out.println(status);
	}
	
	@Test
	public void updateTest () {
		Object array[][] = {{"name"},{"商家"}};
		Map<String, Object> filter = Tool.arrayToMap(array);
		ProductClassify productClassify = new ProductClassify();
		productClassify.setIntro("商家简介-1");
		productClassify.setClassify_id("xxxx-1");
		
		Boolean status = productClassifyDao.filter(filter).update(productClassify);
		
		System.out.println(status);
	}
	
	@Test
	public void queryTest () {
		Object array[][] = {{"name"},{"商家"}};
		Map<String, Object> filter = Tool.arrayToMap(array);
		
		System.out.println(productClassifyDao.filter(filter).queryDocument());
	}
}
