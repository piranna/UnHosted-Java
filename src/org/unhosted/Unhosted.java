/**
 * 
 */
package org.unhosted;

import java.net.URL;

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
	public class RegisterException extends Exception
	{
		public RegisterException(String s)
		{
			super(s);
		}
	}

	public DAV dav = new DAV();

	private Storage localStorage;
	private URL location;

	public Unhosted(Storage localStorage)
	{
		this.localStorage = localStorage;
	}

	public void setUserName(String userName)
	{
		if(userName != "")
		{
			this.localStorage.setItem("unhosted::userName", userName);
			String davDomain = WebFinger.getDavDomain(userName, 0, 1);
			if(davDomain != "")
			{
				this.localStorage.setItem("unhosted::davDomain", davDomain);
				OAuth.dance(davDomain, userName,
							this.location.getHost() + this.location.getPath());
			}
		}
		else
		{
			this.localStorage.removeItem("unhosted::userName");
			this.localStorage.removeItem("unhosted::davDomain");
			OAuth.revoke();
		}
	}

	public String getUserName()
	{
		if(this.localStorage.getItem("OAuth2-cs::token") != null)
			return ((String)this.localStorage.getItem("unhosted::userName")).trim();
		return null;
	}

	public void register(String userName)
	{
		String registerUrl = WebFinger.getAdminUrl(userName);
		if(registerUrl != "")
			this.location = URL(registerUrl);

		// Error while registration
		else
		{
			String[] parts = userName.split("@");
			if(parts.length != 2)
				//inform the user:
				throw new RegisterException("Please use one '@' symbol in the user name");

			//alert the sys admin about the error through a 404 message to her website:
			String url = "http://www."+parts[1]+"/unhosted-account-failure/?user="+userName;
			new DefaultHttpClient().execute(new HttpGet(url));

			//inform the user:
			throw new RegisterException("Unhosted account not found! Please alert" +
										"an IT hero at " +parts[1]+" about this. " +
										"For alternative providers, see " +
										"http://www.unhosted.org/");
		}
	}}