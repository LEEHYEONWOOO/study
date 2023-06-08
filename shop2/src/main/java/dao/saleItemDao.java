package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import dao.mapper.ItemMapper;
import dao.mapper.SaleItemMapper;
import dao.mapper.SaleMapper;
import logic.SaleItem;

@Repository // 객체 주입
public class saleItemDao {
	
	@Autowired
	private SqlSessionTemplate template;
	private Map<String, Object> param = new HashMap<>();
	private final Class<SaleItemMapper> cls = SaleItemMapper.class;

	public void insert(SaleItem saleItem) {
		template.getMapper(SaleItemMapper.class).insert(saleItem);
	}

	public List<SaleItem> list(int saleid) {
		param.clear();
		return template.getMapper(SaleItemMapper.class).list(saleid);
	}

}
