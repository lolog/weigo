package net.oicp.wego.test;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.oicp.wego.dao.ShopDao;
import net.oicp.wego.model.Shop;
import net.oicp.wego.tools.Tool;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.08.26
 * @company        WEGO
 * @description    shop test 
*/
@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/app.xml")
public class ShopTest {
	@Autowired
	private ShopDao shopDao;
	
	@Test
	public void insertTest () {
		Shop shop = new Shop();
		shop.setName("莎莎");
		shop.setAddress("香港");
		shop.setInformation("香港莎莎价格便宜");
		shop.setShop_id("shop_id");
		
		Boolean status = shopDao.insert(shop);
		System.out.println(status);
	}
	
	@Test
	public void updateTest () {
		Shop shop = new Shop();
		shop.setIs_forbidden(true);
		
		String filterArray[][] = {{"name"},{"莎莎"}};
		Map<String, Object> filter = Tool.arrayToMap(filterArray);
		
		Boolean status = shopDao.filter(filter).data(shop).update(shop);
		System.out.println(status);
	}
	
	@Test
	public void queryTest () {
		List<Document> documents = shopDao.queryDocument();
		System.out.println(documents);
	}
}
