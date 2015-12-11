import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class test {

	public static void main(String[] args) {
		TweetSQS tw = new TweetSQS();
		tw.init();
		for (;;){
		Message mes = tw.getFromQueue();
		System.out.println(mes.getBody());
	//	mes.getReceiptHandle();
	//	tw.deleteMes(mes.getReceiptHandle());
		tw.countMessage();
		}
		
	}
}
