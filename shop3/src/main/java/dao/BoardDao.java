package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Board;


@Repository
public class BoardDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<Board> mapper = new BeanPropertyRowMapper<Board>(Board.class);
	private Map<String,Object> param = new HashMap<>();
	private String boardcolum = "select num,name,pass,subject,"
			+ "content,file1 fileurl,regdate,readcnt,grp,grplevel,grpstep from board";
	
	@Autowired //객체 주입 
	public void setDataSource(DataSource datasource) {
		template = new NamedParameterJdbcTemplate(datasource);
	}

	public int count(String type, String content) {
		String sql = "select count(*) from board";
		if(type != null && !type.equals("")) {
			sql +=" where " + type +" like :content";
		}
		param.clear();
		param.put("content", "%"+content+"%");
		return template.queryForObject(sql, param, Integer.class);
	}

	public List<Board> list(Integer pageNum, int limit, String type, String content) {
		String sql = boardcolum;
		if(type != null  && !type.equals("")) {
			sql += " where " + type + " like :content";
		}
		sql += " order by grp desc, grpstep limit :startrow, :limit";
		param.clear();
		param.put("startrow", (pageNum -1 ) * limit);
		param.put("limit",limit);
		param.put("content", "%"+content+"%");
		return template.query(sql, param, mapper);
	}
	
	public int maxnum() {
		return template.queryForObject("select ifnull(max(num),0) from board", param, Integer.class);
	}

	public void insert(Board board) {
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(board);
		String sql = "insert into board (num,name,pass,subject,content,file1,regdate," 
				+"readcnt, grp, grplevel, grpstep) "
				+ "values(:num,:name,:pass,:subject,:content,:fileurl,now()," 
				+ "0, :grp, :grplevel, :grpstep)";
		template.update(sql, proparam);
	}

	public void readcntadd(Integer num) {
		param.clear();
		param.put("num", num);
		String sql = "update board set readcnt=readcnt+1 where num=:num";
		template.update(sql, param);
	}

	public Board selectOne(Integer num) {
		param.clear();
		param.put("num",num);
		return template.queryForObject(boardcolum+" where num=:num",param,mapper);
	}

	public void grpstepadd(Board board) {
		String sql = "update board set grpstep=grpstep+1 where grp=:grp and grpstep>:grpstep";
		param.clear();
		param.put("grp",board.getGrp());
		param.put("grpstep",board.getGrpstep());
		template.update(sql, param);
	}

	public void update(Board board) {
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(board);
		String sql = "update board set name=:name,subject=:subject,content=:content,file1=:fileurl where num=:num";
		template.update(sql, proparam);
	}

	public void delete(int num) {
		String sql = "delete from board where num=:num";
		param.clear();
		param.put("num",num);
		template.update(sql, param);
	}


}
