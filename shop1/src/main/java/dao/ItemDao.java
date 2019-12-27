package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
// Connection객체를 의미

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import logic.Item;

public class ItemDao {
	private NamedParameterJdbcTemplate template;//db에 연결할 수 있는 객체를 만듬. spring jdbc 프레임워크
	RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class);
	//Connection 객체 주입 => xml 설정
	//dataSource : spring-db.xml에 있는 DriverManagerDataSource 객체
	public void setDataSource(DataSource dataSource) {
		this.template = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public List<Item> list() { //db에 있는 Item테이블에있는 모든 레코드를 조회해서 list로 전달 ->shopservice에있는 getItemList로 전달
		/*
			Item item = new Item();
			item.setId(rs.getString("id"));
			item.setName(rs.getString("name"));
		 */
		return template.query("select * from item",mapper);
	}

	public Item selectOne(Integer id) {
		Map<String, Integer> param = new HashMap<>();
		param.put("id",id);
		return template.queryForObject("select * from item where id=:id", param, mapper);
	}
}