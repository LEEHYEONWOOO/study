package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Board;

@Repository
public class boardDao {

		private NamedParameterJdbcTemplate template;
		private Map<String, Object> param = new HashMap<>(); // 맵 객체
		private RowMapper<Board> mapper = new BeanPropertyRowMapper<>(Board.class);
		// -> DB에서 조회된 컬럼명과 Item 클래스의 프로퍼티와 비교해서 일치 할 경우 같은 값을 Item 객체로 생성
		// Item item = new Item();
		// item.setId(rs.getString("id"))
		private String select="select num,writer,pass,title,content,file1 fileurl,regdate,readcnt,grp,grplevel,grpstep,boardid from board";
		
		@Autowired // spring-db.xml에서 설정된 dataSource 객체 주입
		public void setDataSource(DataSource dataSource) {
			// dataSource : db에 연결된 객체
			// NamedParameterJdbcTemplate : spring 프레임워크의 jdbc 템플릿
			template = new NamedParameterJdbcTemplate(dataSource); 
		}

		public int maxNum() {
			return template.queryForObject("select ifnull(max(num),0) from board", param, Integer.class);
		}

		public void insert(@Valid Board board) {
			SqlParameterSource param = new BeanPropertySqlParameterSource(board);
			String sql = "insert into board (num, writer, pass, title, content, file1, boardid, regdate, readcnt, grp, grplevel, grpstep)"
					     + " values(:num, :writer, :pass, :title, :content, :fileurl, :boardid, now(), 0, :grp, :grplevel, :grpstep)";
			template.update(sql, param);
		}

		public int count(String boardid, String searchtype,String searchcontent) {
			String sql = "select count(*) from board where boardid=:boardid";
			param.clear();
			param.put("boardid",boardid);
			if(searchtype != null && searchcontent != null) {
				sql += " and " + searchtype + " like :searchcontent";
				param.put("searchcontent", "%"+searchcontent+"%");
			}
			return template.queryForObject(sql, param, Integer.class);
		}

		public List<Board> list(Integer pageNum, int limit, String boardid, String searchtype,String searchcontent) {
			param.clear();
			String sql = select;
			sql += " where boardid=:boardid";
			if(searchtype != null && searchcontent != null) {
				sql += " and " + searchtype + " like :searchcontent";
				param.put("searchcontent", "%"+searchcontent+"%");
			}
			sql += " and boardid=:boardid order by grp desc, grpstep asc limit :startrow, :limit";
			param.put("startrow",(pageNum-1)*limit);
			param.put("limit",limit);
			param.put("boardid",boardid);
			
			return template.query(sql,param,mapper);
		}

		public Board selectOne(Integer num) {
			param.clear();
			param.put("num",num);
			String sql = select + " where num=:num";
			return template.queryForObject(sql, param, mapper);
		}

		public void addReadcnt(Integer num) {
			param.clear();
			param.put("num",num);
			String sql = "update board set readcnt=readcnt+1 where num=:num";
			template.update(sql, param);
		}

		public void updateGrpStep(Board board) {
			String sql = "update board set grpstep=grpstep + 1"
					+ " where grp = :grp and grpstep > :grpstep";
			param.clear();
			param.put("grp",board.getGrp());
			param.put("grpstep",board.getGrpstep());
			template.update(sql, param);
				
		}

		public void boardUpdate(Board board, HttpServletRequest request) {
			SqlParameterSource param = new BeanPropertySqlParameterSource(board);
			String sql = "update board set writer=:writer, title=:title, content=:content, file1=:fileurl"
					+ " where num=:num";
					     
			template.update(sql, param);
			
		}

		public void boardDelete(int num) {
			template.update("delete from board where num="+num,param);
			
		}

}
