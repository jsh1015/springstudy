package dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

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
	RowMapper<User> mapper = new BeanPropertyRowMapper<User>(User.class);
	Map<String, String> param = new HashMap<String,String>();
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.template = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public void insert(User user) {
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(user);
		String sql = "insert into useraccount (userid, password, username, postcode, email, birthday) "
				+ " values (:userid, :password, :username, :postcode, :email, :birthday)";
		template.update(sql, proparam);
	}

	public User selectOne(String userid) {
		String sql = "select * from useraccount where userid=:userid";
		param.clear();
		param.put("userid",userid);
		return template.queryForObject(sql,param,mapper);
	}

	public void update(User user) {
		String sql = "update useraccount set username=:username,"
				+"phoneno=:phoneno,postcode=:postcode,address=:address,email=:email,birthday=:birthday"
				+ " where userid=:userid";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(user);
		template.update(sql, proparam);
	}

	public void delete(String userid) {
		param.clear();
		param.put("userid",userid);
		template.update("delete from useraccount where userid=:userid", param);
	}
}
