package com.example.android.travellogger;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class DisplayPostsActivity extends ActionBarActivity {

    private String m_Text;
    private boolean mTwoPane;


    @Override
    public void onStart() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_posts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_posts, menu);
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
//        if(id == R.id.action_add_new_post) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("New Post Title:");
//            final EditText input = new EditText(this);
//            builder.setView(input);
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    m_Text = input.getText().toString();
//
//                    ContentValues values = new ContentValues();
//                    values.put(TravelContract.EntryEntry.COLUMN_TITLE, m_Text);
//                    Uri uri = Uri.parse(getIntent().getStringExtra("uri"));
//
//                    Uri newUri = getContentResolver().insert(uri, values);
//                    getContentResolver().notifyChange(uri, null);
//
//                    Intent intent = new Intent(DisplayPostsActivity.this, CreatePostActivity.class);
//                    intent.putExtra("post name", m_Text);
//                    intent.putExtra("uri", newUri.toString());
//                    startActivity(intent);
//                }
//            });
//            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//
//            builder.show();
//
//        }
        return super.onOptionsItemSelected(item);
    }
}
