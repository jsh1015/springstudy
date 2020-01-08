package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.Board;

public interface BoardMapper {
	String boardsql = "select num,name,pass,subject,content,file1 fileurl,regdate,readcnt,grp,grplevel,grpstep"
			+ " from board";
	
	@Select({"<script>"
			+ "select count(*) from board "
			+ "<if test='type != null'>"
			+ " where ${type} like #{content} </if>"
			+ "</script>"})
	int count(Map<String, Object> param);
	
	
	@Select({"<script>",
			 boardsql,
			 "<if test='type!=null'>where ${type} like #{content} </if>",
			 "<if test='num !=null'>where num = #{num} </if>",
			 "<if test='num ==null'> order by grp desc, grpstep limit #{startrow}, #{limit}</if> ",
			 "</script>"})
	List<Board> select(Map<String, Object> param);


	@Select("select ifnull(max(num),0) from board")
	int maxnum();

	@Insert("insert into board (num,name,pass,subject,content,file1,regdate," 
				+"readcnt, grp, grplevel, grpstep) "
				+ "values(#{num},#{name},#{pass},#{subject},#{content},#{fileurl},now()," 
				+ "0, #{grp}, #{grplevel}, #{grpstep})")
	void insert(Board board);

	@Update("update board set readcnt=readcnt+1 where num=#{num}")
	void readcntadd(Map<String, Object> param);

	@Update("update board set grpstep=grpstep+1 where grp=#{grp} and grpstep>#{grpstep}")
	void grpstepadd(Map<String, Object> param);

	@Update("update board set name=#{name},subject=#{subject},content=#{content},file1=#{fileurl} where num=#{num}")
	void update(Board board);

	@Delete("delete from board where num=#{num}")
	void delete(Map<String, Object> param);
}
