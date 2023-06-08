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

import dao.mapper.SaleMapper;
import logic.Sale;


@Repository
public class saleDao {

	@Autowired
	private SqlSessionTemplate template;
	Class<SaleMapper> cls = SaleMapper.class;
		
	public int getMaxSaleId( ) {
		return template.getMapper(cls).maxid();
	}
	
	public void insert(Sale sale) {
		template.getMapper(SaleMapper.class).insert(sale);
	}
	
	public List<Sale> list(String userid) {
		return template.getMapper(SaleMapper.class).list(userid);
	}
	
	



}
