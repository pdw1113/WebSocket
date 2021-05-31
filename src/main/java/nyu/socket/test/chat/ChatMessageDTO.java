package nyu.socket.test.chat;

import java.sql.Date;

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
public class ChatMessageDTO {
	
	private String roomId;			// 방 번호
	private String messageContent;	// 메세지 내용
	private Date messageRegDate;	// 메세지 보낸 시간
	private String userUuid;		// 보낸이
	private int unReadCount;		// 메세지 확인 여부
	
}
