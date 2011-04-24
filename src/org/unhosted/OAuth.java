/**
 * 
 */
package org.unhosted;

import java.net.*;
import java.util.regex.*;


/**
 * @author piranna
 *
 */
public class OAuth
{
	// Storage namespace
	final private String namespace = "OAuth2-cs";

	// Attributes
	private Unhosted unhosted;

	// Constructor
	public OAuth(Unhosted unhosted)
	{
		this.unhosted = unhosted;

		this.receiveToken();
	}

	public URL dance(String oAuthDomain, String userName, String app)
	throws MalformedURLException
	{
		return new URL(oAuthDomain+"oauth2/auth?response_type=token"
				+"&scope="+this.unhosted.getLocationAuthority()
				+"&user_name="+userName
				+"&client_id="+app+"&redirect_uri="+app);
	}

	// Token
	private void receiveToken()
	{
		String location = this.unhosted.getLocation();

		Pattern pattern = Pattern.compile("[\\?&]token=([^&#]*)");
		Matcher matcher = pattern.matcher(location);
		if(matcher.find())
		{
			this.unhosted.getLocalStorage().setItem(this.namespace+"::token", matcher.group(1));

			try
			{
				this.unhosted.setLocation(new URL(location.split("?")[0]));
			}
			catch(MalformedURLException e)
			{
				e.printStackTrace();
			};
		}
	}

	public String getToken()
	{
		return (String)this.unhosted.getLocalStorage().getItem(this.namespace+"::token");
	}

	public void revokeToken()
	{
		this.unhosted.getLocalStorage().removeItem(this.namespace+"::token");
	}
}