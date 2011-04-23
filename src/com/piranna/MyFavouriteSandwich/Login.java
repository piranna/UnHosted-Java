package com.piranna.MyFavouriteSandwich;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.unhosted.Unhosted;


public class Login extends Activity
{
	Unhosted unhosted = new Unhosted(new Storage(this,
									"com.piranna.MyFavouriteSandwich.Storage"));

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }


    // Events
    public void unlock_onClick(View view)
    {
    	final EditText user = (EditText)findViewById(R.id.user);
    	this.unhosted.setUserName(user.getText().toString());

//    	document.getElementById("lockedView").style.display="none";
//    	document.getElementById("unlockedView").style.display="block";
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
}