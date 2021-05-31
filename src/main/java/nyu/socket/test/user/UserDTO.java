package nyu.socket.test.user;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserDTO {
	
	private String userUuid;		// 고유번호
	private String userName;		// 이름
	private String userPassword;	// 비밀번호
	private String userPic;			// 사진
	
}
