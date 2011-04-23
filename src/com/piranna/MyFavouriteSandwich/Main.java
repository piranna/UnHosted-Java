package com.piranna.MyFavouriteSandwich;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.unhosted.DAV;
import org.unhosted.Unhosted;


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

        String userName = this.unhosted.getUserName();
        if(userName != null)
        {
            this.showCurrentUser(userName);
            this.showCurrentSandwich(loadSandwich());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch(item.getItemId())
        {
	        case R.id.saveSandwich:
	            this.saveSandwich();
	            return true;

	        case R.id.lock:
	            this.Lock();
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
        }
    }


    // Data access
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


    // Presentation
    private void showCurrentUser(String userName)
    {
    	document.getElementById("currentUser").innerHTML=
    		"Current user is <strong>"+userName+"</strong> " +
    		"[<a onclick='localStorage.removeItem(\'OAuth2-cs::token\');show();'>Lock</a>]";
    }

    private void showCurrentSandwich(Sandwich sandwich)
    {
    	document.getElementById("firstIngredient").value = sandwich.ingredients[0];
    	document.getElementById("secondIngredient").value = sandwich.ingredients[1];
    }


    // Private
    private void Lock()
    {
    	document.getElementById("unlockedView").style.display="none";
    	document.getElementById("lockedView").style.display="block";
    }
}