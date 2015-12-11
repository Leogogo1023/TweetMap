package edu.columbia.cc.elPonePeli.app;

public enum AwsCredentialConstants
{

	SECRET (System.getProperty("AWS_SECRET_KEY")),
	ACCESS (System.getProperty("AWS_ACCESS_KEY_ID"));
	
//// Insert your keys here
//	SECRET (),
//	ACCESS ();
	
	private final String key;
	
	private AwsCredentialConstants(String s)
	{
		this.key = s;
	}
	
	public String getValue()
	{
		return this.key;
	}
}
