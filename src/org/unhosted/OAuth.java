/**
 * 
 */
package org.unhosted;

import java.net.URL;

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
		this.location = oAuthDomain
		+"oauth2/auth"
		+"?client_id="+app
		+"&redirect_uri="+app
		+"&response_type=token"
		+"&scope="+document.domain
		+"&user_name="+userName;
	}

	public void revoke()
	{
		this.localStorage.removeItem("OAuth2-cs::token");
	}

	public void receiveToken()
	{
		var regex = new RegExp("[\\?&]token=([^&#]*)");
		var results = regex.exec(window.location.href);
		if(results)
		{
			this.localStorage.setItem("OAuth2-cs::token", results[1]);
			this.location = URL(this.location.toString().split("?")[0]);
		}
	}
}
