package com.example.android.travellogger;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.android.travellogger.provider.JournalAdapter;
import com.example.android.travellogger.provider.TravelContentProvider;
import com.example.android.travellogger.provider.TravelContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class JournalsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private JournalAdapter mJournalsAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    private static final String[] DB_ROWS = {
            TravelContract.JournalEntry.COLUMN_ID,
            TravelContract.JournalEntry.COLUMN_NAME,
            TravelContract.JournalEntry.COLUMN_LOCK
    };

    public static final int COL_JOURNAL_ID = 0;
    public static final int COL_JOURNAL_NAME = 1;

    public JournalsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(MainActivity.ismTwoPane()) {
            getActivity().setTitle("Journals");
        }
        mJournalsAdapter = new JournalAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.journals_fragment_main, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_journal);
        mListView.setAdapter(mJournalsAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    final long id) {
                final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                mPosition = position;
                if (cursor != null) {
                    final String s;
                    if ((s = cursor.getString(2)) == null) {
                        GoToNextThing(id);
                    } else {
                        final EditText input = new EditText(getActivity());
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Enter Password:")
                                .setView(input)
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("Unlock", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String text = input.getText().toString().trim();
                                        if (text.equals(s.trim())) {
                                            GoToNextThing(id);
                                        }
                                    }
                                }).show();
                    }
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            final String[] options = {
                    "Delete",
                    "Change Name",
                    "Change Lock"
            };

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                final long _id = id;
                new AlertDialog.Builder(getActivity()).setTitle("Extra Options")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final EditText input = new EditText(getActivity());
                                switch (which)
                                {
                                    case 0:
                                        TravelContentProvider.SafeDeleteJournal(getActivity().getContentResolver(), ((Cursor)parent.getItemAtPosition(position)).getString(1));
                                        break;
                                    case 1:
                                        new AlertDialog.Builder(getActivity()).setTitle("Rename to:")
                                                .setView(input)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ContentValues values = new ContentValues();
                                                        values.put(TravelContract.JournalEntry.COLUMN_NAME, input.getText().toString());
                                                        getActivity().getContentResolver().update(TravelContract.JournalEntry.CONTENT_URI, values, TravelContract.JournalEntry.COLUMN_ID + " = " + _id, null);
                                                    }
                                                }).setNegativeButton("Cancel", null)
                                                .show();
                                        break;
                                    case 2:
                                        Cursor cursor = (Cursor)parent.getItemAtPosition(position);
                                        final String s;
                                        if ((s = cursor.getString(2)) == null) {
                                            ChangePassword(_id);
                                        } else {
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("Enter Password:")
                                                    .setView(input)
                                                    .setNegativeButton("Cancel", null)
                                                    .setPositiveButton("Unlock", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String text = input.getText().toString().trim();
                                                            if (text.equals(s.trim())) {
                                                                ChangePassword(_id);
                                                            }
                                                        }
                                                    }).show();
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
                return true;
            }
        });

        if(savedInstanceState != null && savedInstanceState.containsKey("mPosition"))
        {
            mPosition = savedInstanceState.getInt("mPosition");
        }

        return rootView;

    }

    public void ChangePassword(long id)
    {
        MainActivity activity = ((MainActivity)getActivity());
        activity.LockJournal(id);
    }

    public void GoToNextThing(long id)
    {

            String string = TravelContract.EntryEntry.CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build().toString();
            //on click, we need to replace the fragment from main activity with
            //the list of posts.
            if(getActivity().findViewById(R.id.detail_container) == null) {
                Intent intent = new Intent(getActivity(), DisplayPostsActivity.class);
                intent.putExtra("uri", string);
                startActivity(intent);
            } else {

                PostsFragment fragment = new PostsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("uri", string);
                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment, fragment)
                        .commit();
            }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle b)
    {
        String sortOrder = TravelContract.JournalEntry.COLUMN_ID + " DESC";
        return new CursorLoader(getActivity(),
                TravelContract.JournalEntry.CONTENT_URI,
                DB_ROWS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mJournalsAdapter.swapCursor(data);
        if(mPosition != ListView.INVALID_POSITION)
        {
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    public void onSaveInstanceState(Bundle outBundle)
    {
        if(mPosition != ListView.INVALID_POSITION) {
            outBundle.putInt("mPosition", mPosition);
        }
        super.onSaveInstanceState(outBundle);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mJournalsAdapter.swapCursor(null);
    }


}
