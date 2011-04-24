/**
 * 
 */
package org.unhosted;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.impl.client.DefaultHttpClient;

import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;


/**
 * @author piranna
 *
 */
public class WebFinger
{
	private URL location;

	static private String getHostMeta(String userName, String linkRel)
	{
		//split the userName at the "@" symbol:
		String[] parts = userName.split("@");
		if(parts.length != 2)
			return null;

		String domain = parts[1];

		//get the host-meta data for the domain:
		HttpGet http = new HttpGet("http://"+domain+"/.well-known/host-meta");

//		// WebFinger spec allows application/xml+xrd as the mime type,
//		// but we need it to be text/xml for xhr.responseXML to be non-null:
//		xhr.overrideMimeType("text/xml");

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
				HttpEntity entity = response.getEntity();
				InputStream is = null;
				try
				{
					is = entity.getContent();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

				DocumentBuilder documentBuilder = null;
				try
				{
					documentBuilder = dbf.newDocumentBuilder();
				}
				catch(ParserConfigurationException e)
				{
					e.printStackTrace();
				}

				if(documentBuilder != null)
				{
					Document doc = null;
					try
					{
						doc = documentBuilder.parse(is);
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
					catch(SAXException e)
					{
						e.printStackTrace();
					}

					if(doc != null)
					{
						NodeList links = doc.getElementsByTagName("Link");
						for(int i=0; i<links.getLength(); i++)
						{
							NamedNodeMap attributes = links.item(i).getAttributes();
							if(attributes.getNamedItem("rel").getNodeValue() == linkRel)
								return attributes.getNamedItem("template").getNodeValue();
						}
					}
				}
			}
		}

		return null;
	}

	static private boolean matchLinkRel(String linkRel, int majorDavVersion,
										int minMinorDavVersion)
	{
		//TODO: do some real reg exp...
		final class davVersion
		{
			static final int major = 0;
			static final int minor = 1;
		};

		if(davVersion.major != majorDavVersion)
			return false;

		// from 1.0.0 onwards,
		// check if available version is at least minMinorDavVersion
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
		if(template != null)
		{
			String url = template.replace("{uri}", "acct:"+userName);

			// Prepare a request object
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);

			HttpEntity entity = null;
			try
			{
				// Execute the request
				HttpResponse response = httpclient.execute(httpget);

				// Get hold of the response entity
				entity = response.getEntity();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

			// If the response does not enclose an entity, there is no need
			// to worry about connection release
			if(entity != null)
			{
//				// WebFinger spec allows application/xml+xrd as the mime type,
//				// but we need it to be text/xml for xhr.responseXML to be non-null
//				xhr.overrideMimeType("text/xml");

				// Read response
				InputStream is = null;
				try
				{
					is = entity.getContent();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}

				if(is != null)
				{
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

					DocumentBuilder documentBuilder = null;
					try
					{
						documentBuilder = dbf.newDocumentBuilder();
					}
					catch(ParserConfigurationException e)
					{
						e.printStackTrace();
					}

					if(documentBuilder != null)
					{
						Document doc = null;
						try
						{
							doc = documentBuilder.parse(is);
						}
						catch(IOException e)
						{
							e.printStackTrace();
						}
						catch(SAXException e)
						{
							e.printStackTrace();
						}

						if(doc != null)
						{
							NodeList links = doc.getElementsByTagName("Link");
							for(int i=0; i<links.getLength(); i++)
							{
								NamedNodeMap attributes = links.item(i).getAttributes();
								if(matchLinkRel(attributes.getNamedItem("rel").getNodeValue(),
												majorVersion, minMinorVersion))
									return attributes.getNamedItem("href").getNodeValue();
							}
						}
					}
				}
			}
		}

		return null;
	}

	public String getAdminUrl(String userName)
	{
		String template = getHostMeta(userName, "register");
		if(template != null)
			return template.replace("{uri}",userName)
							.replace("{redirect_url}",this.location.toString());

		return null;
	}
}