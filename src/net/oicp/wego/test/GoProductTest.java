package net.oicp.wego.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.oicp.wego.dao.ProductDao;
import net.oicp.wego.model.Product;
import net.oicp.wego.model.append.Festival;
import net.oicp.wego.util.impl.DocumentUtilImpl;

/** 
 * @author         lolog
 * @version        V1.0  
 * @Date           2016年8月2日
 * @Company        CIMCSSC
 * @Description    TODO(文件描述) 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/app.xml")
public class GoProductTest extends DocumentUtilImpl<Product>{
	@Resource
	private ProductDao productDao;
	
	@Test
	public void insertOneTest () {
		Product goProduct = new Product();
		goProduct.setProduct_id("product_id_1");
		goProduct.setAdd_time(new Date());
		
		List<Festival> festivals = new ArrayList<Festival>();
		Festival festival = new Festival();
		festival.setEnd_time(new Date());
		festival.setName("中秋节");
		festival.setDiscount_money(1);
		
		festivals.add(festival);
		
		goProduct.setFestivals(festivals);
		goProduct.setIs_del(false);
		
		Boolean status = productDao.insert(goProduct);
		System.out.println(status);
	}
	
	@Test
	public void selectTest () {
		List<Product> products = productDao.query();
		System.out.println(products);
	}
	
	public static void main(String[] args) {
		Product goProduct = new Product();
		goProduct.setProduct_id("product_id_1");
		goProduct.setAdd_time(new Date());
		
		List<Festival> festivals = new ArrayList<Festival>();
		Festival festival = new Festival();
		festival.setEnd_time(new Date());
		festival.setName("中秋节");
		festival.setDiscount_money(1);
		
		festivals.add(festival);
		
		goProduct.setFestivals(festivals);
		goProduct.setIs_del(false);
		
		Document document = new GoProductTest().ekeyObjectToDocument(goProduct,true,null);
		System.out.println(document);
	}
}
