package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.ItemMapper;
import logic.Item;
import logic.User;

@Repository //@Component + dao 기능
public class ItemDao {
	@Autowired //내컨테이너들 중에 ?SqlSessionTemplate 객체를 주입해 (spring-db)에있음
	private SqlSessionTemplate sqlSession;
	private Map<String,Object> param = new HashMap<>();
	
	public List<Item> list() {
		return sqlSession.getMapper(ItemMapper.class).select(null);
	}
	public void insert(Item item) {
		param.clear();
		//id : 현재 등록된 id의 최대값
		int id = sqlSession.getMapper(ItemMapper.class).maxid();
		item.setId(++id+"");
		sqlSession.getMapper(ItemMapper.class).insert(item);
	}
	public Item selectOne(String string) { //mapper때문에 Item이 될 수 있음, 상세보기화면
		param.clear();
		param.put("id", string);
		return  sqlSession.getMapper(ItemMapper.class).select(param).get(0);
		//.get(0) : 한개뿐이여서 해줘야함
	}
	public void update(Item item) {
		sqlSession.getMapper(ItemMapper.class).update(item);
	}
	public void delete(String id) {
		param.clear();
		param.put("id",id);
		sqlSession.getMapper(ItemMapper.class).delete(param);
	}
}
