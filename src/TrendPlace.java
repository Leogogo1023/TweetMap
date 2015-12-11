import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.TwitterException;

public class TrendPlace {
	String[] resultArray = new String[10];
	JSONObject json = new JSONObject();
	String location;
	twitter4j.Twitter twitter = null;
	int id = 1;
	HashMap<String, Integer> idMap = new HashMap();
	HashMap<Integer, String> locMap = new HashMap();
	
	//Constructors
	public TrendPlace(twitter4j.Twitter twitter){
		this.twitter = twitter;
		this.setidMap();		
		this.setlocMap();
	}
	
	public TrendPlace(twitter4j.Twitter twitter,int customizedId) throws TwitterException{
		this(twitter);
		this.computeTrends(customizedId);
	}
	
	public TrendPlace(twitter4j.Twitter twitter,String location) throws TwitterException{
		this(twitter);
		this.computeTrends(location);
	}
	
	
	
	//initiate Authorization
	
	public void setId(int customizedId){
		this.id = customizedId;
	}
	
	public String[] getTrendsbyArray(){
		return resultArray;
	}
	
	public JSONObject getTrendsbyJson() throws JSONException{
		this.location = locMap.get(this.id);
		String json = "{\"Location\":\""+this.location+"\"";
		int i = 1;
		for(String str:this.resultArray){
			json = json + ",\""+i+"\":"+"\""+str+"\"";
			i++;
		}
		json = json + "}";
		return new JSONObject(json);
	}
	
	
	
	//get global trend
	public void computeTrends() throws TwitterException{
		

		twitter4j.Trend[] tra = twitter.getPlaceTrends(id).getTrends();
		int i = 0;
		for(twitter4j.Trend td : tra){
			if(i>9) break;
			resultArray[i] = td.getName();
		    System.out.println(resultArray[i]);
			i++;			
		}
	}
	
	//get trend by id
	public void computeTrends(int customizedId) throws TwitterException{
		this.id = customizedId;
		this.computeTrends();
	}
	
	//get trend by country or region
	public void computeTrends(String location) throws TwitterException{
		this.location=location;
		int customizedId = this.getIdByLocation(location);
		this.computeTrends(customizedId);
	}
	
	
	
	private int getIdByLocation(String location) {
		// TODO Auto-generated method stub	
		String inputStr = location.toLowerCase();
		if(!idMap.containsKey(inputStr)){
			System.out.println("The key is not invalid. Please try the key in {NYC, US, UK, Turkey, Japan, Spain, Germany, France}.");
		}
		return idMap.get(inputStr);
	}
	
	//initiate mapping between locations and ids
	private void setidMap(){
		idMap.put("nyc",2459115);
		idMap.put("us",23424977);
		idMap.put("uk",23424975);
		idMap.put("turkey",23424969);
		idMap.put("japan",23424856);
		idMap.put("spain",23424950);
		idMap.put("germany",23424829);
		idMap.put("france",23424819);		
	}
	
	private void setlocMap(){
		locMap.put(2459115,"nyc");
		locMap.put(23424977,"us");
		locMap.put(23424975,"uk");
		locMap.put(23424969,"turkey");
		locMap.put(23424856,"japan");
		locMap.put(23424950,"spain");
		locMap.put(23424829,"germany");
		locMap.put(23424819,"france");		
	}
}
