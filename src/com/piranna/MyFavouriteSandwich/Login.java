package com.piranna.MyFavouriteSandwich;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.unhosted.Unhosted;


public class Login extends Activity
{
	private Unhosted unhosted;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        this.unhosted = new Unhosted(new Storage(this,
        									"com.piranna.MyFavouriteSandwich"));
    }


    // Events
    public void unlock_onClick(View view)
    {
    	final EditText user = (EditText)findViewById(R.id.user);
    	this.unhosted.setUserName(user.getText().toString());

    	// Check if credentials were valid
        String userName = this.unhosted.getUserName();
        if(userName != null)
        	startActivity(new Intent(Login.this, Main.class));
    }

    public void create_onClick(View view)
    {
    	String user = ((EditText)findViewById(R.id.user)).getText().toString();

    	try
    	{
    		this.unhosted.register(user);
    	}
    	catch(Unhosted.RegisterException e)
    	{
        	((TextView)findViewById(R.id.errorText)).setText(e.getMessage());
    	}
    }
}