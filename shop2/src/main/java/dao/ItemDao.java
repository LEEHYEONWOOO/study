package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.ItemMapper;
import logic.Item;

@Repository // @Component + dao(database 연동) 기능
public class ItemDao {
	
	@Autowired
	private SqlSessionTemplate template;
	private Map<String, Object> param = new HashMap<>(); // 맵 객체
	private final Class<ItemMapper> cls = ItemMapper.class;
	
	public List<Item> list() {
		param.clear();
		return template.getMapper(cls).select(param);
		// select한 데이터를 mapper에 저장
	}

	public Item getItem(Integer id) {
		param.clear();
		param.put("id", id);
		return template.getMapper(cls).select(param).get(0);
		//return template.selectOne("dao.mapper.ItemMapper.select",param);
	}

	public int maxId() {
		return template.getMapper(cls).maxid();
	}

	public void insert(Item item) {
		template.getMapper(cls).insert(item);
	}

	public void update(Item item) {
		template.getMapper(cls).update(item);
	}

	public void delete(Integer id) {
		param.clear();
		param.put("id", id);
		template.getMapper(cls).delete(param);
	}

}
