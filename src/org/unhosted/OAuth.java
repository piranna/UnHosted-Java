/**
 * 
 */
package org.unhosted;

/**
 * @author piranna
 *
 */
public class OAuth
{
	public OAuth()
	{
		this.receiveToken();
	}

	public void dance(String oAuthDomain, String userName, String app)
	{
		window.location = oAuthDomain
		+"oauth2/auth"
		+"?client_id="+app
		+"&redirect_uri="+app
		+"&response_type=token"
		+"&scope="+document.domain
		+"&user_name="+userName;
	}

	public void revoke()
	{
		localStorage.removeItem("OAuth2-cs::token");
	}

	public void receiveToken()
	{
		var regex = new RegExp("[\\?&]token=([^&#]*)");
		var results = regex.exec(window.location.href);
		if(results)
		{
			localStorage.setItem("OAuth2-cs::token", results[1]);
			window.location = location.href.split("?")[0];
		}
	}
}
