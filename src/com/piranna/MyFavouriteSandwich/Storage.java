/**
 * 
 */
package com.piranna.MyFavouriteSandwich;

import android.content.SharedPreferences;
import android.context.Context.*;


/**
 * @author piranna
 *
 */
public class Storage implements org.unhosted.html5.Storage
{
	private SharedPreferences settings;

	public Storage(String file)
	{
		this.settings = getSharedPreferences(file, MODE_PRIVATE);
	}

	/* (non-Javadoc)
	 * @see org.unhosted.html5.Storage#clear()
	 */
	@Override
	public void clear()
	{
		SharedPreferences.Editor editor = this.settings.edit();
		editor.clear();
		editor.apply();
	}

	/* (non-Javadoc)
	 * @see org.unhosted.html5.Storage#getItem(java.lang.String)
	 */
	@Override
	public Object getItem(String key)
	{
		return this.settings.getString(key, null);
	}

	/* (non-Javadoc)
	 * @see org.unhosted.html5.Storage#key(long)
	 */
	@Override
	public String key(long index)
	{
		return this.settings.getAll()[index];
	}

	/* (non-Javadoc)
	 * @see org.unhosted.html5.Storage#removeItem(java.lang.String)
	 */
	@Override
	public void removeItem(String key)
	{
		SharedPreferences.Editor editor = this.settings.edit();
		editor.remove(key);
		editor.apply();
	}

	/* (non-Javadoc)
	 * @see org.unhosted.html5.Storage#setItem(java.lang.String, java.lang.String)
	 */
	@Override
	public void setItem(String key, Object value)
	{
		SharedPreferences.Editor editor = this.settings.edit();
		editor.putObject(key, value);
		editor.apply();
	}
}