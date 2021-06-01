package nyu.socket.test.user;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	@Autowired
	SqlSessionTemplate sqlSession;
	
	private final String NS = "userMapper.";
	
	public UserDTO selectUser(String user) {
		return sqlSession.selectOne(NS + "selectUser", user);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<UserDTO> selectUserList() {
		return (ArrayList<UserDTO>)(Object)sqlSession.selectList(NS + "selectUserList");
	}

}
