/**
 * Interface that mimics the HTML5 Storage functionality
 * http://www.w3.org/TR/webstorage/#storage-0
 */
package org.unhosted;


/**
 * @author piranna
 *
 */
public interface IStorage
{
//	public long length = 0;

	public String key(long index);

	public Object getItem(String key);
	public void setItem(String key, Object value);

	public void removeItem(String key);

	public void clear();
}