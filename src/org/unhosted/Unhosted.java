/**
 * 
 */
package org.unhosted;

import java.io.IOException;
import java.net.*;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.unhosted.html5.Storage;


/**
 * @author piranna
 *
 */
public class Unhosted
{
	// Storage namespace
	final private String namespace = "unhosted";

	// Attributes
	private Storage localStorage;
	private URL location;

	private OAuth oAuth;
	private WebFinger webFinger = new WebFinger();

	// UnHosted modules - only DAV at this moment
	public DAV dav;


	// Errors exception
	public class RegisterException extends Exception
	{
		public RegisterException(String s)
		{
			super(s);
		}
	}


	// Constructor
	public Unhosted(Storage localStorage)
	{
		this.localStorage = localStorage;

		this.oAuth = new OAuth(localStorage);

		this.dav = new DAV(this);
	}

	// User name
	public String getUserName()
	{
		if(this.oAuth.getToken() != null)
			return ((String)this.localStorage.getItem(this.namespace+"::userName")).trim();
		return null;
	}

	public void setUserName(String userName)
	{
		if(userName == null)
		{
			this.localStorage.removeItem(this.namespace+"::userName");
			this.localStorage.removeItem(this.namespace+"::davDomain");

			this.oAuth.revokeToken();
		}
		else
		{
			this.localStorage.setItem(this.namespace+"::userName", userName);

			String davDomain = this.webFinger.getDavDomain(userName, 0, 1);
			if(davDomain != null)
			{
				this.localStorage.setItem(this.namespace+"::davDomain", davDomain);

				try
				{
					this.location = this.oAuth.dance(davDomain, userName,
													this.location.getHost() +
													this.location.getPath());
				}
				catch(MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void register(String userName) throws RegisterException
	{
		String registerUrl = this.webFinger.getAdminUrl(userName);
		if(registerUrl != "")
			try
			{
				this.location = new URL(registerUrl);
			}
			catch(MalformedURLException e)
			{
				e.printStackTrace();
			}

		// Error while registration
		else
		{
			String[] parts = userName.split("@");

			//alert the sys admin about the error through a 404 message to her website:
			if(parts.length == 2)
			{
				String url = "http://www."+parts[1]+"/unhosted-account-failure/?user="+userName;

				// Prepare a request object
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(url);

				// Execute the request and get hold of the response entity
				try
				{
					httpclient.execute(httpget).getEntity();
				}
				catch(ClientProtocolException e)
				{
					e.printStackTrace();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}

				//inform the user:
				throw new RegisterException("Unhosted account not found! Please alert" +
											"an IT hero at " +parts[1]+" about this. " +
											"For alternative providers, see " +
											"http://www.unhosted.org/");
			}

			//inform the user:
			throw new RegisterException("Please use one '@' symbol in the user name");
		}
	}


	// Auxiliars
	public String getDavDomain()
	{
		return (String)this.localStorage.getItem(this.namespace+"::davDomain");
	}

	public String getLocationAuthority()
	{
		return this.location.getAuthority();
	}

	public String getOAuthToken()
	{
		return this.oAuth.getToken();
	}
}