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

import dao.mapper.BoardMapper;
import logic.Board;


@Repository
public class BoardDao {
	@Autowired
	private SqlSessionTemplate sqlSession;
	private Map<String,Object> param = new HashMap<>();

	public int count(String type, String content) {
		param.clear();
		param.put("type",type);
		param.put("content", "%"+content+"%");
		return sqlSession.getMapper(BoardMapper.class).count(param);
	}

	public List<Board> list(Integer pageNum, int limit, String type, String content) {
		param.clear();
		param.put("type",type);
		param.put("startrow", (pageNum -1 ) * limit);
		param.put("limit",limit);
		param.put("content","%"+content+"%");
		return sqlSession.getMapper(BoardMapper.class).select(param);
	}
	
	public int maxnum() {
		return sqlSession.getMapper(BoardMapper.class).maxnum();
	}

	public void insert(Board board) {
		sqlSession.getMapper(BoardMapper.class).insert(board);
	}

	public void readcntadd(Integer num) {
		param.clear();
		param.put("num", num);
		sqlSession.getMapper(BoardMapper.class).readcntadd(param);
	}

	public Board selectOne(Integer num) {
		param.clear();
		param.put("num",num);
		return sqlSession.getMapper(BoardMapper.class).select(param).get(0);
	}

	public void grpstepadd(Board board) {
		param.clear();
		param.put("grp",board.getGrp());
		param.put("grpstep",board.getGrpstep());
		sqlSession.getMapper(BoardMapper.class).grpstepadd(param);
	}

	public void update(Board board) {
		sqlSession.getMapper(BoardMapper.class).update(board);
	}

	public void delete(int num) {
		param.clear();
		param.put("num",num);
		sqlSession.getMapper(BoardMapper.class).delete(param);
	}
}
