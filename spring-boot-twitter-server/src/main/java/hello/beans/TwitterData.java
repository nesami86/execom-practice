package hello.beans;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "twitter_data")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class TwitterData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty
	private long twitterDataId;

	@JsonProperty
	private long twitterId;
	
	@JsonProperty
	private String twitterName;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "twitter_data_id")
	@JsonManagedReference
	@OrderBy("date DESC")
	@JsonProperty
	private List<Tweet> tweets;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "twitter_data_friend", joinColumns = { @JoinColumn(name = "twitter_data_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "friend_id", nullable = false, updatable = false) })
	@JsonManagedReference
	@JsonProperty
	private List<Friend> friends;

	public TwitterData() {

	}

	public long getTwitterDataId() {
		return twitterDataId;
	}

	public void setTwitterDataId(long twitterDataId) {
		this.twitterDataId = twitterDataId;
	}

	public long getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(long twitterId) {
		this.twitterId = twitterId;
	}

	public String getTwitterName() {
		return twitterName;
	}

	public void setTwitterName(String twitterName) {
		this.twitterName = twitterName;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}
}