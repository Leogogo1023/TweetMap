import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNSClient;
public class SNSClient {

	
	public AmazonSNSClient ini()
	{
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider("default").getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. " +
							"Please make sure that your credentials file is at the correct " +
							"location (/Users/Liang/.aws/credentials), and is in valid format.",
							e);
		}
		AmazonSNSClient amazonSNSClient = new AmazonSNSClient(credentials);
		return amazonSNSClient;
	}
	public void publish(AmazonSNSClient amazonSNSClient, String message)
	{	
		System.out.println("*************");
		amazonSNSClient.publish("arn:aws:sns:us-east-1:583916764003:tweet", message);
	}
	

}
