package com.piranna.MyFavouriteSandwich;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.TextView;

import org.unhosted.DAV;
import org.unhosted.Unhosted;


public class Main extends Activity
{
	private Unhosted unhosted;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.unhosted = new Unhosted(new Storage(this,
											"com.piranna.MyFavouriteSandwich"));

        this.showCurrentUser(this.unhosted.getUserName());
        this.showCurrentSandwich(loadSandwich());
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
    	((TextView)findViewById(R.id.currentUser)).setText("Current user is <strong>"+userName+"</strong>");
    }

    private void showCurrentSandwich(Sandwich sandwich)
    {
    	((EditText)findViewById(R.id.ingredient1)).setText(sandwich.ingredients[0]);
    	((EditText)findViewById(R.id.ingredient2)).setText(sandwich.ingredients[1]);
    }


    // Private
    private void saveSandwich()
    {
    	// Get ingredients
    	String ingredient1 = ((EditText)findViewById(R.id.ingredient1)).getText().toString();
    	String ingredient2 = ((EditText)findViewById(R.id.ingredient2)).getText().toString();

    	// Save sandwich
		this.saveSandwich(new Sandwich(new String[]{ingredient1, ingredient2}));
	}

    private void Lock()
    {
    	this.unhosted.setUserName(null);

    	this.finish();
    }
}