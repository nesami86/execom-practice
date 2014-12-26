package hello.beans;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tweets")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class Tweet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty
	private long tweetId;

	@ManyToOne
	@JoinColumn(name = "twitter_data_id")
	@JsonBackReference
	@JsonProperty
	private TwitterData twitterData;

	@JsonProperty
	private Date date;
	
	@JsonProperty
	private String text;

	public Tweet() {

	}

	public long getId() {
		return tweetId;
	}

	public void setId(long id) {
		this.tweetId = id;
	}

	public TwitterData getTwitterData() {
		return twitterData;
	}

	public void setTwitterData(TwitterData twitterData) {
		this.twitterData = twitterData;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}