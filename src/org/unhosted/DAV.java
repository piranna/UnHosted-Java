/**
 * 
 */
package org.unhosted;

//import java.io.BufferedReader;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
//import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
//import java.io.Writer;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.StatusLine;

import android.util.Base64;	// API Level 8

import com.google.gson.Gson;


/**
 * @author piranna
 *
 */
public class DAV
{
	// Attributes
	private Unhosted unhosted;

	// Errors exception
	public class DAVException extends Exception
	{
		public DAVException(String s)
		{
			super(s);
		}
	}

	// Constructor
	public DAV(Unhosted unhosted)
	{
		this.unhosted = unhosted;
	}

	static private String makeBasicAuth(String user, String password)
	{
		String hash = user + ':' + password;
		return "Basic " + Base64.encodeToString(hash.getBytes(), Base64.DEFAULT);
	}

	private String key2Url(String key)
	{
		String[] userNameParts = this.unhosted.getUserName().split("@");

		return this.unhosted.getDavDomain()
							+"webdav/"+userNameParts[1]
							+"/"+userNameParts[0]
							+"/"+this.unhosted.getLocationAuthority()
							+"/"+key;
	}

	public Object get(String key) throws DAVException
	{
		// Set method
		HttpGet http = new HttpGet(key2Url(key));

		http.addHeader("Authorization",
						makeBasicAuth(this.unhosted.getUserName(),
									this.unhosted.getOAuthToken()));
//		http.withCredentials = "true";

		// Create client and execute
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		try
		{
			response = client.execute(http);
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
			{
				Reader reader = null;
				try
				{
					reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}

				if(reader != null)
					return new Gson().fromJson(reader,Object.class);
			}
			if(code == 404)
				return null;

			throw new DAVException("error: got status "+code+
								" ("+statusLine.getReasonPhrase()+")"+
								" when doing basic auth GET on url "+key2Url(key));
		}

		return null;
	}

	public void put(String key, Object value) throws DAVException
	{
		// Set method
		HttpPut http = new HttpPut(key2Url(key));

		http.addHeader("Authorization",
						makeBasicAuth(this.unhosted.getUserName(),
									this.unhosted.getOAuthToken()));
//		http.withCredentials("true");

		// Set entity
		StringEntity stringEntity = null;
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
			HttpResponse response = null;
			try
			{
				response = client.execute(http);
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
					throw new DAVException("error: got status "+code
										+" ("+statusLine.getReasonPhrase()+")"
										+" when doing basic auth PUT on url "
										+key2Url(key));
			}
		}
	}

//	static private String Stream2String(InputStream is)
//	throws IOException
//	{
//		/*
//		 * To convert the InputStream to String we use the
//		 * Reader.read(char[] buffer) method. We iterate until the
//		 * Reader return -1 which means there's no more data to
//		 * read. We use the StringWriter class to produce the string.
//		 */
//		if(is != null)
//		{
//			Writer writer = new StringWriter();
//		
//			char[] buffer = new char[1024];
//			try
//			{
//				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//				int n;
//				while((n = reader.read(buffer)) != -1)
//					writer.write(buffer, 0, n);
//			}
//			finally
//			{
//				is.close();
//			}
//	
//			return writer.toString();
//		}
//	
//		return "";
//	}
}