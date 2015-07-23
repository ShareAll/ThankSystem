package com.thank.database;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.thank.database.MyBatisFactoryTest.TestVo;

public interface TestMapper {
	@Insert("INSERT INTO TEST (name,lastLoginTime) values(#{name},now())") 
	@Options(useGeneratedKeys=true, keyProperty="id",keyColumn="id")
	public int insertTest(TestVo vo); 
	
	@Update("UPDATE TEST SET name= #{name},lastLoginTime=#{lastLoginTime}  WHERE id=#{id}")
	public void updateTest(TestVo vo);
	
	@Delete("DELETE FROM TEST WHERE id=#{id}")
	public void deleteTest(TestVo vo);
	
	@Select("SELECT * FROM TEST WHERE id = #{id}")
	public TestVo selectTest(long id);

	static class PureSqlProvider {
        public String sql(String sql) {
            return sql;
        }
    }
	
}
