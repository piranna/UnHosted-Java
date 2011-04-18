package com.piranna;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.unhosted.Unhosted;


public class Main extends Activity
{
	Unhosted unhosted = new Unhosted();

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

    private loadSandwich()
    {
        var sandwich = this.unhosted.dav.get("favSandwich.json");
        if(sandwich)
            return sandwich;
        return {ingredients:["", ""]};
    }

    private void saveSandwich(ingredients)
    {
        this.unhosted.dav.put("favSandwich.json", {"ingredients":ingredients});
    }

    private void show()
    {
        String userName = this.unhosted.getUserName();
        if(userName)
        {
            var sandwich = loadSandwich();
//            showCurrentUser(userName);
//            showCurrentSandwich(sandwich);
            showUnlocked();
        }
        else
            showLocked();
    }
}