package nyu.socket.test.user;

import org.springframework.stereotype.Component;

@Component
public class User {
	
	private String uuid;	// 고유번호
	private String name;	// 이름
	
	public User() {
		super();
	}

	public User(String uuid, String name) {
		super();
		this.uuid = uuid;
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [uuid=" + uuid + ", name=" + name + "]";
	}
}
