package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.UserMapper;
import exception.LoginException;
import logic.User;

@Repository
public class UserDao {
	@Autowired
	private SqlSessionTemplate sqlSession;
	Map<Object, Object> param = new HashMap<Object,Object>();
	
	public void insert(User user) {
		sqlSession.getMapper(UserMapper.class).insert(user);
	}
	
	public User selectOne(String userid) {
		param.clear();
		param.put("userid",userid);
		List<User> list = sqlSession.getMapper(UserMapper.class).select(param);
		if(list==null || list.isEmpty()){
			throw new LoginException("해당 아이디 없음","");
		}else 
		return list.get(0);
	}

	public void update(User user) {
		sqlSession.getMapper(UserMapper.class).update(user);
	}

	public void delete(String userid) {
		param.clear();
		param.put("userid",userid);
		sqlSession.getMapper(UserMapper.class).delete(userid);
	}

	public List<User> userlist() {
		return sqlSession.getMapper(UserMapper.class).select(null);
	}

	public List<User> list(String[] idchks) {
		param.clear();
		param.put("idchks",idchks);
		return sqlSession.getMapper(UserMapper.class).select(param);
	}
}
