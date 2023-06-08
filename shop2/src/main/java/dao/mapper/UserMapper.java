package dao.mapper;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import logic.User;

public interface UserMapper {
	
	@Insert("insert into useraccount (userid, username, password,"
			+ " birthday, phoneno, postcode, address, email) "
			+ " values (#{userid}, #{username}, #{password},"
			+ "	#{birthday}, #{phoneno}, #{postcode}, #{address}, #{email})")
	public void insert(User user);
	
	@Select({"<script>",
			"select * from useraccount",
			"<if test='userid != null'> where userid=#{userid}</if>",
			"<if test='userids != null'> where userid in "
			+ "<foreach collection='userids' items='id' separator=',' open='(' close=')'>#{id}"
			+ "</foreach></if>",
			"</script>"})
	List<User> select(Map<String,Object> param);

	@Update("update useraccount set username=#{username},"
			+ " birthday=#{birthday}, phoneno=#{phoneno},"
			+ " postcode=#{postcode}, address=#{address}, email=#{email}"
			+ " where userid=#{userid}")
	public void update(User user);

	@Delete("delete from useraccount where userid=#{userid}")
	public void delete(String userid);

	@Update("update useraccount set password=#{password}"
				+ " where userid=#{userid}")
	public void chgpass(Map<String, Object> param);

	@Select({"<script>",
			"select ${col} from useraccount where email=#{email} and phoneno=#{phoneno} "
			+ "<if test='userid != null'> and userid =#{userid}</if>",
			"</script>"})
	public String search(Map<String, Object> param);
}
	

