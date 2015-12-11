import java.io.IOException;
import java.util.Vector;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.*;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

@ServerEndpoint("/websocket")
public class SocketServer {
	static int lon,lat;
	static int lastIndex = 0;
	static String temp = "";
	static TrendPlace tp;
	@OnMessage
	public void onMessage(String message, Session session) throws IOException,InterruptedException, TwitterException, org.json.JSONException {
		if (!temp.equals(message)){
			lastIndex =0;
			temp = message;
		}
		Vector<Vector<String>> tweet = null;

		switch (message){
		case "all": tweet = Rds.getResult(); break;
		case "sport":
		case "job":
		case "love":
		case "food": tweet =Rds.getResult(message); break;
		case "hour":
		case "day":
		case "more" :  tweet =Rds.getDateData(message); break;	    	
		}

		for (Session peer : session.getOpenSessions()) {
			if (tweet == null)
			{
				ConfigurationBuilder cb = TweetConfig.config();
				System.out.println(message);
				TwitterFactory tf = new TwitterFactory(cb.build());
				twitter4j.Twitter twitter = tf.getInstance();
				tp = new TrendPlace(twitter);
				tp.computeTrends(message);

				String trend = tp.getTrendsbyJson().toString();
				peer.getBasicRemote().sendText(trend);
			}
			else
			{
				for (int i =lastIndex; i<tweet.size();i++)
				{   
					String str = "{ \"GeoLocation\":[" + tweet.get(i).get(4) +","+ tweet.get(i).get(5) +","+ tweet.size() + "],";
					str = str + " \"Sentiment\": \"" + tweet.get(i).get(6) +"\" }";
					peer.getBasicRemote().sendText(str);	    			
				}
				lastIndex = tweet.size();
				System.out.println("lastIndex: "+ lastIndex );
			}
		}
	}


	@OnOpen
	public void onOpen(Session session) throws IOException, InterruptedException {
		System.out.println("Client connected");
		lastIndex =0;
	}

	@OnClose
	public void onClose(Session session) throws IOException, InterruptedException{
		System.out.println("Connection closed");
	}


	
} // end class
