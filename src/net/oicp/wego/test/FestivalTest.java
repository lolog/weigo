package net.oicp.wego.test;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.oicp.wego.dao.FestivalDao;
import net.oicp.wego.model.Festival;
import net.oicp.wego.tools.Tool;

/** 
 * @author         lolog
 * @version        V1.0  
 * @date           2016.08.25
 * @company        WEGO
 * @description    festival test 
*/
@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/app.xml")
public class FestivalTest {
	@Autowired
	private FestivalDao festivalDao;
	
	@Test
	public void insertTest () {
		Festival festival = new Festival();
		festival.setName("中秋节");
		festival.setFestival_id("zh-q");
		festival.setStart_time(Tool.dateStringToDate("2016-8-17 10:10:10", "yyyy-MM-dd HH:mm:ss"));
		festival.setEnd_time(Tool.dateStringToDate("2016-8-18 10:10:10", "yyyy-MM-dd HH:ii:ss"));
		
		Boolean status = festivalDao.data(festival).insert();
		System.out.println(status);
	}
	
	@Test
	public void updateTest () {
		Object object[][] = {{"name"},{"中秋节"}};
		Map<String, Object> filter = Tool.arrayToMap(object);
		Festival festival = new Festival();
		festival.setFestival_id("zh-q-0");
		festival.setIs_del(false);
		
		Boolean status = festivalDao.filter(filter).data(festival).update();
		System.out.println(status);
	}
	
	@Test
	public void queryTest () {
		String[] includes = {"festival_id","name","start_time_string","end_time_string","add_time"};
		List<Document> documents = festivalDao.include(includes).queryDocument();
		System.out.println(documents);
	}
}
