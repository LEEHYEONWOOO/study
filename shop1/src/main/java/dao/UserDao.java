package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.User;

@Repository
public class UserDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<User> mapper = new BeanPropertyRowMapper<User>(User.class);
	private Map<String, Object> param = new HashMap<>();
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}

	public void insert(User user) { 
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		// param : user 객체의 프로퍼티를 이용하여 db에 값 등록
		String sql = "insert into useraccount (userid, username, password,"
				+ " birthday, phoneno, postcode, address, email) values "
				+ " (:userid, :username, :password,"
				+ "	:birthday, :phoneno, :postcode, :address, :email)";
		template.update(sql, param);
	}

	public User selectOne(String userid) {
		param.clear();
		param.put("userid", userid);
		return template.queryForObject ("select * from useraccount where userid=:userid", param, mapper);
	}

	public void update(@Valid User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		// param : user 객체의 프로퍼티를 이용하여 db에 값 등록
		String sql = "update useraccount set username=:username,"
				+ " birthday=:birthday, phoneno=:phoneno, postcode=:postcode, address=:address, email=:email where userid=:userid";
		template.update(sql, param);
		
	}

	public void delete(String userid) {
		param.clear();
		param.put("userid", userid);
		String sql = "delete from useraccount where userid=:userid";
		template.update(sql, param);
	}

	public void chgpass(String userid,String chgpass) {
		param.clear();
		param.put("userid", userid);
		String sql = "update useraccount set password="+chgpass+" where userid=:userid";
		template.update(sql, param);
		
	}

	public List<User> userList() {
		param.clear();
		String sql = "select * from useraccount";
		//String sql = "select * from useraccount"+sort;
		return template.query(sql, param, mapper);
	}
	
		
	public List<User> list(String[] idchks) {
		//이것은 내가 마음대로 한것이니 프로젝트에 넣지마시오!!
		String list = "";
		int len = idchks.length;
		for(int i=0;i<len;i++) {
			if(i!=0) list +=",";
			list += "'"+idchks[i]+"'";
		}
		String sql = "select * from useraccount where userid in ("+list+")";
		System.out.println("sql은 : "+sql);
		return template.query(sql, mapper);
	}

	public String search(User user) {
		String col = "userid";
		param.clear();
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		if(user.getUserid()!=null) 
			col = "password";
		String sql = "select " + col + " from useraccount where email=:email and phoneno=:phoneno"; 
		if(user.getUserid()!=null) {
			sql+= " and userid=:userid";
		}
		return template.queryForObject (sql, param, String.class);
	}


 
} // class
