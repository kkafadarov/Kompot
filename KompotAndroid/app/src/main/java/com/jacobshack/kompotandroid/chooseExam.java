package com.jacobshack.kompotandroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class chooseExam extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_exam);


        String[] tempArray = new String[10];

        tempArray[0] = "Exam balls 1";
        tempArray[1] = "Exam balls 2";
        tempArray[2] = "Exam balls 3";
        tempArray[3] = "Exam balls 4";
        tempArray[4] = "Exam balls 5";
        tempArray[5] = "Exam balls 6";
        tempArray[6] = "Exam balls 7";
        tempArray[7] = "Exam balls 8";
        tempArray[8] = "Exam balls 9";
        tempArray[9] = "Exam balls 10";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, tempArray);

        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(chooseExam.this, chooseAction.class);
                intent.putExtra("Exam", (listView.getItemAtPosition(position).toString())  );
                startActivity(intent);
            }
        }

        );
   }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choose_exam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
