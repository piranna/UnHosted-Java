package com.piranna.MyFavouriteSandwich;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.unhosted.DAV;
import org.unhosted.Unhosted;

import com.piranna.MyFavouriteSandwich.R;
import com.piranna.MyFavouriteSandwich.Storage;


public class Main extends Activity
{
	Unhosted unhosted = new Unhosted(new Storage(this,
												"com.piranna.MyFavouriteSandwich.Storage"));

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    // Events
    public void unlock_onClick(View view)
    {
    	final EditText user = (EditText)findViewById(R.id.user);
    	this.unhosted.setUserName(user.getText().toString());
    	this.show();
    }

    public void create_onClick(View view)
    {
    	final EditText user = (EditText)findViewById(R.id.user);
    	final TextView errorText = (TextView)findViewById(R.id.errorText);

    	try
    	{
    		this.unhosted.register(user.getText().toString());
    	}
    	catch(Unhosted.RegisterException e)
    	{
        	errorText.setText(e.getMessage());
    	}
    }

    private Sandwich loadSandwich()
    {
    	Sandwich sandwich = null;
    	try
    	{
        	sandwich = (Sandwich)this.unhosted.dav.get("favSandwich.json");
    	}
    	catch(DAV.DAVException e)
		{
			e.printStackTrace();
		}

    	if(sandwich != null)
            return sandwich;
        return new Sandwich(new String[]{"", ""});
    }

    private void saveSandwich(Sandwich sandwich)
    {
    	try
    	{
            this.unhosted.dav.put("favSandwich.json", sandwich);
    	}
    	catch(DAV.DAVException e)
		{
			e.printStackTrace();
		}
    }

    private void show()
    {
        String userName = this.unhosted.getUserName();
        if(userName != null)
        {
            Sandwich sandwich = loadSandwich();
//            showCurrentUser(userName);
//            showCurrentSandwich(sandwich);
            showUnlocked();
        }
        else
            showLocked();
    }
}