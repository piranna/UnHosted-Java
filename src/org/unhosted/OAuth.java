/**
 * 
 */
package org.unhosted;

import java.net.*;
import java.util.regex.*;

import org.unhosted.html5.Storage;


/**
 * @author piranna
 *
 */
public class OAuth
{
	private Storage localStorage;
	private URL location;

	public OAuth(Storage localStorage)
	{
		this.localStorage = localStorage;

		this.receiveToken();
	}

	public void dance(String oAuthDomain, String userName, String app)
	{
		try
		{
			this.location = new URL(oAuthDomain+"oauth2/auth?response_type=token"
					+"&scope="+this.location.getAuthority()
					+"&user_name="+userName
					+"&client_id="+app+"&redirect_uri="+app);
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
	}

	public void revoke()
	{
		this.localStorage.removeItem("OAuth2-cs::token");
	}

	public void receiveToken()
	{
		Pattern pattern = Pattern.compile("[\\?&]token=([^&#]*)");
		Matcher matcher = pattern.matcher(this.location.toString());
		if(matcher.find())
		{
			this.localStorage.setItem("OAuth2-cs::token", matcher.group(1));

			try
			{
				this.location = new URL(this.location.toString().split("?")[0]);
			}
			catch(MalformedURLException e){};
		}
	}
}
