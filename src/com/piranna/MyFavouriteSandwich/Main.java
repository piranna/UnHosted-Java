package com.piranna.MyFavouriteSandwich;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.TextView;

import org.unhosted.Unhosted;
import org.unhosted.android.*;
import org.unhosted.DAV.DAVException;


public class Main extends Activity
{
	static final int UNHOSTED_LOGIN_ID = 0;

	private Unhosted unhosted;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);

        this.unhosted = new Unhosted("http://www.myfavouritesandwich.org/",
        							new Storage(this,
											"com.piranna.MyFavouriteSandwich"));

        String userName = null;
//        do
//        {
        	// Get user name
        	userName = this.unhosted.getUserName();

        	// User is not logged, show login dialog
        	if(userName == null)
        		showDialog(UNHOSTED_LOGIN_ID);

//        }while(userName == null);

//        this.showCurrentUser(userName);
//        this.showCurrentSandwich(loadSandwich());
    }

    protected Dialog onCreateDialog(int id, Bundle args)
    {
        Dialog dialog;
        switch(id)
        {
        	case UNHOSTED_LOGIN_ID:
        		dialog = new Login(this, this.unhosted);
        		break;

	        default:
            	Log.e("onCreateDialog", "default");
	            dialog = null;
        }

        return dialog;
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
    	catch(DAVException e)
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
    	catch(DAVException e)
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