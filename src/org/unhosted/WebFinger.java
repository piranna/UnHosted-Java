/**
 * 
 */
package org.unhosted;

import java.io.InputStream;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * @author piranna
 *
 */
public class WebFinger
{
	static private String getHostMeta(String userName, String linkRel)
	{
		//split the userName at the "@" symbol:
		String[] parts = userName.split("@");
		if(parts.length != 2)
			return null;

		String user = parts[0];
		String domain = parts[1];

		//get the host-meta data for the domain:
		var xhr = new XMLHttpRequest();
		var url = "http://"+domain+"/.well-known/host-meta";
		xhr.open("GET", url, false);	
		//WebFinger spec allows application/xml+xrd as the mime type, but we need it to be text/xml for xhr.responseXML to be non-null:
		xhr.overrideMimeType("text/xml");
		xhr.send();
		if(xhr.status == 200)
		{


			//HACK WHILE I FIND OUT WHY xhr.responseXML is null:
			var parser=new DOMParser();
			var responseXML = parser.parseFromString(xhr.responseText, "text/xml");
			//END HACK -Michiel.


			var hostMetaLinks = responseXML.documentElement.getElementsByTagName("Link");
			var i;
			for(i=0; i<hostMetaLinks.length; i++)
				if(hostMetaLinks[i].attributes.getNamedItem("rel").value == linkRel)
					return hostMetaLinks[i].attributes.getNamedItem("template").value;
		}
	}

	static private boolean matchLinkRel(String linkRel, int majorDavVersion,
										int minMinorDavVersion)
	{
		//TODO: do some real reg exp...
		var davVersion = {major:0, minor:1};

		if(davVersion.major != majorDavVersion)
			return false;

		//from 1.0.0 onwards, check if available version is at least minMinorDavVersion
		if(majorDavVersion > 0)
			return davVersion.minor >= minMinorDavVersion;

		//pre-1.0.0, every minor version is breaking, see http://semver.org/
		return davVersion.minor == minMinorDavVersion;
	}

	public String getDavDomain(String userName, int majorVersion,
								int minMinorVersion)
	{
		//get the WebFinger data for the user and extract the uDAVdomain:
		String template = getHostMeta(userName, "lrdd");
		if(template)
		{
			String url = template.replace("/{uri}/", "acct:"+userName, true);

			// Prepare a request object
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);

			HttpEntity entity;
			try
			{
				// Execute the request
				HttpResponse response = httpclient.execute(httpget);

				// Get hold of the response entity
				entity = response.getEntity();
			}
			catch(ClientProtocolException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

			// If the response does not enclose an entity, there is no need
			// to worry about connection release
			if(entity)
			{
//				// WebFinger spec allows application/xml+xrd as the mime type,
//				// but we need it to be text/xml for xhr.responseXML to be non-null
//				xhr.overrideMimeType("text/xml");

				// Read response
				InputStream instream = entity.getContent();
				String result= convertStreamToString(instream);


				//HACK WHILE I FIND OUT WHY xhr.responseXML is null
				var parser=new DOMParser();
				var responseXML = parser.parseFromString(xhr.responseText, "text/xml");
				//END HACK -Michiel.


				var linkElts = responseXML.documentElement.getElementsByTagName("Link");
				for(int i=0; i < linkElts.length; i++)
				{
					attributes = linkElts[i].attributes;
					if(matchLinkRel(attributes.getNamedItem("rel").value,
									majorVersion, minMinorVersion))
						return attributes.getNamedItem("href").value;
				}
			}
		}

		return null;
	}

	public String getAdminUrl(String userName)
	{
		var template = getHostMeta(userName, "register");
		if(template)
			return template.replace("\{uri\}",userName).replace("\{redirect_url\}",
									window.location);

		return null;
	}
}