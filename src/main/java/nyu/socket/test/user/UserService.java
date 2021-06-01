package nyu.socket.test.user;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public UserDTO selectUser(String user) {
		return userRepository.selectUser(user);
	}

	public ArrayList<UserDTO> selectUserList() {
		return userRepository.selectUserList();
	}
	
	

}
