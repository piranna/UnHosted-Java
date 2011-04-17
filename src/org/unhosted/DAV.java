/**
 * 
 */
package org.unhosted;

import org.json.*;

import android.util.Base64;	// API Level 8


/**
 * @author piranna
 *
 */
public class DAV
{
	static private String makeBasicAuth(String user, String password)
	{
		String hash = user + ':' + password;
		return "Basic " + Base64.encodeToString(hash.getBytes(), Base64.DEFAULT);
	}

	static public String key2Url(String key)
	{
		String[] userNameParts = localStorage.getItem("unhosted::userName").split("@");
		String resource = document.domain;

		String url = localStorage.getItem("unhosted::davDomain")
				+"webdav/"+userNameParts[1]
				+"/"+userNameParts[0]
				+"/"+resource
				+"/"+key;
		return url;
	}

	public get(String key)
	{
		JSONObject text = new JSONObject(value);
	}

	public put(String key, Object value)
	{
		JSONObject text = new JSONObject(value);
	}
}