package hello.beans;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "friends")
public class Friend {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long friendId;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "friends")
	@JsonBackReference
	private List<TwitterData> twitterData;

	private String name;

	public Friend() {

	}

	public long getFriendId() {
		return friendId;
	}

	public void setId(long friendId) {
		this.friendId = friendId;
	}

	public List<TwitterData> getTwitterData() {
		return twitterData;
	}

	public void seTwitterData(List<TwitterData> twitterData) {
		this.twitterData = twitterData;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}