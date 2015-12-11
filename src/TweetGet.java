import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;

import com.amazonaws.services.sns.AmazonSNSClient;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.JSONObject;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TweetGet{
	static Connection conn;
	static TweetSQS tweetSQS;
	static int tweetID;
	public static void main(String[] args) throws TwitterException,UnknownHostException, SQLException, JSONException, twitter4j.JSONException {
		tweetSQS = new TweetSQS();
		tweetSQS.init();
		ConfigurationBuilder cb = TweetConfig.config();
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		FilterQuery fq = new FilterQuery();    
		String[] keyword = {"sport","love","food","job"};
		fq.track(keyword); 

		String countRow = "SELECT max(TID) FROM Tweets;";
		ResultSet rs = Rds.stmt.executeQuery(countRow); 
		rs.next();
		if(rs.getString(1) == null)
			tweetID =1;
		else{
			tweetID = Integer.parseInt(rs.getString(1));
		}
		System.out.println("tweetID is :" + tweetID);


		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				GeoLocation loc = status.getGeoLocation();
				if (loc != null )
				{
					try {
						String latitude = loc.getLatitude()+"";
						String longitude = loc.getLongitude()+"";
						String usr = status.getUser().getScreenName();
						String content = status.getText().replaceAll("\u005c\u0022", "");
						content = content.replaceAll("(\\r|\\n|\\r\\n)+", "");
						String time  = dateConvert( status.getCreatedAt().toString() );;

						tweetSQS.putInQueue(++tweetID, usr, content, time,latitude, longitude);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}	            
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				//System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				// System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				//System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				//System.out.println("Got stall warning:" + warning);
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};
		twitterStream.addListener(listener);
		twitterStream.filter(fq);

		SNSClient snsclient = new SNSClient();
		AmazonSNSClient amazonSNSClient =snsclient.ini();
		System.out.println(amazonSNSClient.toString());
		SNSServlet snsservlet = new SNSServlet();
		Worker wk = new Worker();
		for(;;){			
			String temp =wk.getResult();
			if (temp != null){
				JSONObject jsonObj = new JSONObject(temp);
				String senti =jsonObj.get("Sentiment").toString();
				if ( senti.equals("positive") ||senti.equals("negative")||senti.equals("neutral") ){	
					System.out.println(temp);
					snsclient.publish(amazonSNSClient, temp);
				}
			}
		} 		

	}  // end main

	public static String dateConvert(String date) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
		Date parsedDate = dateFormat.parse(date);
		Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		return timestamp.toString();
	}

}

