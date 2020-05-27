package main.authentication;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import main.util.ConfigurationProperties;


public class HMACSignature{
	String data;
	String signedData;


	HMACSignature(String data) throws Exception {
		this.data = data;
	}
	
	private String toHexString(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = AuthenticationConstants.HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = AuthenticationConstants.HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	// throws illegal state Exception when Data is not initialized
	public String sign() throws Exception
	{
		if(data == null) {
			throw new IllegalStateException();
		}
		String key = ConfigurationProperties.getConfiguration(AuthenticationConstants.CONFIG_KEY_HMAC_SHA1);
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), AuthenticationConstants.HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(AuthenticationConstants.HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		signedData = toHexString(mac.doFinal(data.getBytes()));
		return signedData;
	}
	
	public String getSignedData() {
		return signedData;
	}

}
