package nyu.socket.test.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public UserDTO selectUser(String user) {
		return userRepository.selectUser(user);
	}
	
	

}
