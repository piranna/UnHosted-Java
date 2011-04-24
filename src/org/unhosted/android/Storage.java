/**
 * 
 */
package org.unhosted.android;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * @author piranna
 *
 */
public class Storage implements org.unhosted.IStorage
{
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;

	public Storage(Context context, String file)
	{
		this.settings = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		this.editor = this.settings.edit();
	}

	public long length()
	{
		return this.settings.getAll().size();
	}

	/* (non-Javadoc)
	 * @see org.unhosted.html5.Storage#clear()
	 */
	@Override
	public void clear()
	{
		this.editor.clear();
		this.editor.commit();
//		this.editor.apply();
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
		return (String)this.settings.getAll().keySet().toArray()[(int)index];
	}

	/* (non-Javadoc)
	 * @see org.unhosted.html5.Storage#removeItem(java.lang.String)
	 */
	@Override
	public void removeItem(String key)
	{
		this.editor.remove(key);
		this.editor.commit();
//		this.editor.apply();
	}

	/* (non-Javadoc)
	 * @see org.unhosted.html5.Storage#setItem(java.lang.String, java.lang.String)
	 */
	@Override
	public void setItem(String key, Object value)
	{
		this.editor.putString(key, (String)value);
		this.editor.commit();
//		this.editor.apply();
	}
}