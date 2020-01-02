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

import logic.Item;
import logic.User;

@Repository //@Component + dao 기능
public class ItemDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class);
	private Map<String,Object> param = new HashMap<>();
	@Autowired
	public void setDataSource(DataSource datasource) {
		template = new NamedParameterJdbcTemplate(datasource);
	}
	public List<Item> list() {
		return template.query("select * from item order by id", mapper);
	}
	public void insert(Item item) {
		param.clear();
		//id : 현재 등록된 id의 최대값
		int id = template.queryForObject("select ifnull(max(id),0) from item", param, Integer.class);
		item.setId(++id+"");
		String sql = "insert into item (id,name,price,description,pictureUrl)"
				+ " values (:id,:name,:price,:description,:pictureUrl)";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(item);
		template.update(sql, proparam);
	}
	public Item selectOne(String string) { //mapper때문에 Item이 될 수 있음, 상세보기화면
		param.clear();
		param.put("id", string);
		return  template.queryForObject("select * from item where id=:id",param, mapper);
	}
	public void update(Item item) {
		String sql = "update item set name=:name, price=:price, description=:description,"
				+ "pictureUrl=:pictureUrl where id=:id";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(item); 
		//item에있는 property를 파라미터를 배치하라는것
		template.update(sql, proparam);
	}
	public void delete(int id) {
		param.clear();
		param.put("id", id);
		template.update("delete from item where id=:id",param);
	}
}
