package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.User;

public interface UserMapper {

	@Insert("insert into useraccount (userid, password, phoneno, username, postcode, email, birthday) "
				+ " values (#{userid}, #{password}, #{phoneno}, #{username}, #{postcode}, #{email}, #{birthday}) ")
	void insert(User user);

	@Select({"<script>"
			+ "select * from useraccount "
			+ "<if test='userid!=null'> where userid=#{userid} </if>"
			+ "<if test='idchks!=null'> "
			+ " where userid in "
			+ "<foreach collection='idchks' item='id' separator=',' open='(' close=')' index='i'>"
			+ " #{id}"
			+ "</foreach>"
			+ "</if>"
			+ "</script>"})
	List<User> select(Map<Object, Object> param);

	@Update("update useraccount set username=#{username},"
				+"phoneno=#{phoneno},postcode=#{postcode},address=#{address},email=#{email},birthday=#{birthday}"
				+ " where userid=#{userid}")
	void update(User user);

	@Delete("delete from useraccount where userid=#{userid}")
	void delete(String userid);
}
