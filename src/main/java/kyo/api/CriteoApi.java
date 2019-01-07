package kyo.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

public class CriteoApi {


//	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String clientId = "mapi-cf0739ae-e50a-4513-86ff-87abdf7ed3fa";
		String password = "P@s1$.:&vglHeN3f";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 1step : Get token for access API
			HttpPost httpPost = new HttpPost("https://api.criteo.com/marketing/oauth2/token");

			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("client_id", clientId));
			postParameters.add(new BasicNameValuePair("client_secret", password));
			postParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
			CloseableHttpResponse res = httpclient.execute(httpPost);
			String theJsonString = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(theJsonString);
			HashMap<String, Object> result = mapper.readValue(theJsonString, HashMap.class);
			String token = (String) result.get("access_token");
			httpPost = new HttpPost("https://api.criteo.com/marketing/v1/statistics");
			httpPost.setHeader("Authorization", "Bearer " + token);
			
			// Get campaign list
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
