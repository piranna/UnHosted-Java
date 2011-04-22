/**
 * 
 */
package org.unhosted;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.StatusLine;
import org.json.*;
import org.unhosted.html5.Storage;

import com.google.gson.Gson;

import android.util.Base64;	// API Level 8


/**
 * @author piranna
 *
 */
public class DAV
{
	public class DAVException extends Exception
	{
		public DAVException(String s)
		{
			super(s);
		}
	}

	private Storage localStorage;
	private URL location;

	static private String makeBasicAuth(String user, String password)
	{
		String hash = user + ':' + password;
		return "Basic " + Base64.encodeToString(hash.getBytes(), Base64.DEFAULT);
	}

	private String key2Url(String key)
	{
		String[] userNameParts = ((String)this.localStorage.getItem("unhosted::userName")).split("@");

		return this.localStorage.getItem("unhosted::davDomain")
				+"webdav/"+userNameParts[1]
				+"/"+userNameParts[0]
				+"/"+this.location.getAuthority()
				+"/"+key;
	}

	public Object get(String key) throws DAVException
	{
		// Set method
		HttpGet http = new HttpGet(key2Url(key));

		http.addHeader("Authorization",
						makeBasicAuth((String)localStorage.getItem("unhosted::userName"),
									(String)localStorage.getItem("OAuth2-cs::token")));
//		http.withCredentials = "true";

		// Create client and execute
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response;
		try
		{
			response = client.execute(http);
		}
		catch(ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		// Check response
		if(response != null)
		{
			StatusLine statusLine = response.getStatusLine();
			int code = statusLine.getStatusCode();
			if(code == 200)
				return new Gson().fromJson(response.getEntity().getContent());
			if(code == 404)
				return null;

			throw new DAVException("error: got status "+code+
								" ("+statusLine.getReasonPhrase()+")"+
								" when doing basic auth GET on url "+key2Url(key));
		}
	}

	public void put(String key, Object value) throws DAVException
	{
		// Set method
		HttpPut http = new HttpPut(key2Url(key));

		http.addHeader("Authorization",
						makeBasicAuth((String)localStorage.getItem("unhosted::userName"),
									(String)localStorage.getItem("OAuth2-cs::token")));
//		http.withCredentials("true");

		// Set entity
		StringEntity stringEntity;
		try
		{
			stringEntity = new StringEntity(new Gson().toJson(value));
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		if(stringEntity != null)
		{
			http.setEntity(stringEntity);

			// Create client and execute
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse response;
			try
			{
				response = client.execute(http);
			}
			catch(ClientProtocolException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

			// Check response
			if(response != null)
			{
				StatusLine statusLine = response.getStatusLine();
				int code = statusLine.getStatusCode();
				if(code != 200 && code != 201 && code != 204)
					throw new DAVException("error: got status "+code+
										" ("+statusLine.getReasonPhrase()+")"+
										" when doing basic auth PUT on url "+key2Url(key));
			}
		}
	}
}