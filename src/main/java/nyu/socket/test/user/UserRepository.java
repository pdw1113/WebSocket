package nyu.socket.test.user;

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

}
