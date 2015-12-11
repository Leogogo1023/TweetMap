import twitter4j.conf.ConfigurationBuilder;

public class TweetConfig {
	
	public static ConfigurationBuilder config(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("*")
		.setOAuthConsumerSecret("*")
		.setOAuthAccessToken("*")
		.setOAuthAccessTokenSecret("*")
		.setJSONStoreEnabled(true);
		return cb;		
	}

}
