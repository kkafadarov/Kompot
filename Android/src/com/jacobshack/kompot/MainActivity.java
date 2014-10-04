package com.jacobshack.kompot;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
	
	Button button_logIn;  
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        button_logIn = (Button) findViewById(R.id.button_logIn);
        
        
        
    }



}
