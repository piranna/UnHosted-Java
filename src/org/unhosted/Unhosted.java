/**
 * 
 */
package org.unhosted;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


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

	private DAV dav = new DAV();

	public void setUserName(String userName)
	{
		if(userName != "")
		{
			localStorage.setItem("unhosted::userName", userName);
			String davDomain = WebFinger.getDavDomain(userName, 0, 1);
			if(davDomain != "")
			{
				localStorage.setItem("unhosted::davDomain", davDomain);
				OAuth.dance(davDomain, userName, location.host + location.pathname);
			}
		}
		else
		{
			localStorage.removeItem("unhosted::userName");
			localStorage.removeItem("unhosted::davDomain");
			OAuth.revoke();
		}
	}

	public String getUserName()
	{
		if(localStorage.getItem("OAuth2-cs::token"))
			return localStorage.getItem("unhosted::userName").trim();
		return null;
	}

	public void register(String userName)
	{
		String registerUrl = WebFinger.getAdminUrl(userName);
		if(registerUrl != "")
			window.location = registerUrl;

		// Error while registration
		else
		{
			String[] parts = userName.split("@");
			if(parts.length != 2)
				//inform the user:
				throw RegisterException("Please use one '@' symbol in the user name");

			//alert the sys admin about the error through a 404 message to her website:
			String url = "http://www."+parts[1]+"/unhosted-account-failure/?user="+userName;
			new DefaultHttpClient().execute(new HttpGet(url));

			//inform the user:
			throw RegisterException("Unhosted account not found! Please alert" +
									"an IT hero at " +parts[1]+" about this. " +
									"For alternative providers, see " +
									"http://www.unhosted.org/");
		}
	}}