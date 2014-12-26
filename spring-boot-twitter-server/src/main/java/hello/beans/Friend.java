package hello.beans;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "friends")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class Friend {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty
	private long friendId;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "friends")
	@JsonBackReference
	@JsonProperty
	private List<TwitterData> twitterData;

	@JsonProperty
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