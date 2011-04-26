package org.unhosted.android;

import android.app.Dialog;
import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.unhosted.Unhosted;

import com.piranna.MyFavouriteSandwich.R;


public class Login extends Dialog
{
	private Unhosted unhosted;

	public Login(Context context, Unhosted unhosted)
	{
		super(context);
		this.setTitle("UnHosted login");

		this.unhosted = unhosted;
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.login);
    }


    // Events
    public void unlock_onClick(View view)
    {
    	final EditText user = (EditText)findViewById(R.id.user);
    	this.unhosted.setUserName(user.getText().toString());

//    	// Check if credentials were valid
//        String userName = this.unhosted.getUserName();
//        if(userName != null)
//        	startActivity(new Intent(Login.this, Main.class));
//        else
//        {
//        	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//        	alertDialog.setMessage("User name is not set");
//        	alertDialog.setButton("OK",new DialogInterface.OnClickListener()
//        	{
//        		public void onClick(DialogInterface dialog, int which){};
//        	});
//        	alertDialog.show();
//        }
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