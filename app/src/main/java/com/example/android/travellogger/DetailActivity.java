package com.example.android.travellogger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.plus.PlusShare;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_share) {
//            Intent intent = new Intent(DetailActivity.this, PlusActivity.class);
//            startActivity(intent);

            Intent shareIntent = new PlusShare.Builder(this)
                    .setType("text/plain")
                    .setText("Welcome to the Google+ platform.")
                    .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                    .getIntent();

            startActivityForResult(shareIntent, 0);
        }

        return super.onOptionsItemSelected(item);
    }

}
