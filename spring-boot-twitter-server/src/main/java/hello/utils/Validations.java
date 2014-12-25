package hello.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import hello.beans.Friend;
import hello.beans.Tweet;
import hello.beans.TwitterData;
import hello.beans.User;

@Component
public class Validations {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TwitterDataRepository twitterDataRepository;
	
	@Autowired
	private FriendsRepository friendsRepository;
		
	public String checkLogin(User user) {
				
		if (userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword()) != null) {
			
			return "OK_" + user.getUsername();
		
		} else {
			
			return "Wrong username and/or password!";
		}
	}
	
	public String checkRegistration(User user) {
				
		if (userRepository.findByUsername(user.getUsername()) != null) {
			
			return "Username already taken!";
		
		} else {
		
			userRepository.save(user);
			return "OK";
		}
	}
	
	/**
	 * Adds twitter ID and Screen Name to the existing user in the database.
	 * 
	 * @param username
	 * @param twitter
	 */
	public void synchronizeTwitter(String username, Twitter twitter) {
		
		User user = userRepository.findByUsername(username);
		user.setTwitterId(twitter.userOperations().getProfileId());
		user.setTwitterName(twitter.userOperations().getScreenName());
		
		userRepository.save(user);
	}
	
	/**
	 * Saves twitter data to database.
	 * Checks if the data of the specific user already exist in the database, if so it then compares the dates of
	 * the last tweets and number of friends and updates them accordingly.
	 * 
	 * @param twitter
	 */
	public void saveTwitterData(Twitter twitter) {
			
		TwitterData twitterData = twitterDataRepository.findByTwitterId(twitter.userOperations().getProfileId());
		List<Tweet> tweets;
		List<Friend> friends;
		Date lastTweet = twitter.timelineOperations().getUserTimeline().get(0).getCreatedAt();
		Date lastSavedTweet = null;
		
		if (twitterData != null) {
			
			lastSavedTweet = twitterData.getTweets().get(0).getDate();
			tweets = twitterData.getTweets();
			friends = twitterData.getFriends();
	
		} else {
			
			twitterData = new TwitterData();
			tweets = new ArrayList<Tweet>();
			friends = new ArrayList<Friend>();			
		}
				
		if ((lastSavedTweet != null && lastSavedTweet.before(lastTweet)) || lastSavedTweet == null) {
			
			twitterData.setTwitterId(twitter.userOperations().getUserProfile().getId());
			twitterData.setTwitterName(twitter.userOperations().getUserProfile().getScreenName());
				
			int numberOfNewTweets = twitter.timelineOperations().getUserTimeline().size() - tweets.size();
			
			for (int i = 0; i < numberOfNewTweets; i++) {
				
				Tweet tweet = new Tweet();
				tweet.setTwitterData(twitterData);
				tweet.setDate(twitter.timelineOperations().getUserTimeline().get(i).getCreatedAt());
				tweet.setText(twitter.timelineOperations().getUserTimeline().get(i).getText());
				tweets.add(i, tweet);
			}
			
			int numberOfNewFriends = twitter.friendOperations().getFriends().size() - friends.size();
			
			for (int i = 0; i < numberOfNewFriends; i++) {
				
				Friend friend = friendsRepository.findByName(twitter.friendOperations().getFriends().get(i).getName());
				
				if (friend == null) {
					
					friend = new Friend();
					List<TwitterData> twitterDataList = new ArrayList<TwitterData>();
					friend.seTwitterData(twitterDataList);
					friend.getTwitterData().add(twitterData);
					friend.setName(twitter.friendOperations().getFriends().get(i).getName());
				
				} else {
					
					friend.getTwitterData().add(twitterData);
				}
				
				friends.add(i, friend);
			}
			
			twitterData.setTweets(tweets);
			twitterData.setFriends(friends);
			
			twitterDataRepository.save(twitterData);
		}
	}
}