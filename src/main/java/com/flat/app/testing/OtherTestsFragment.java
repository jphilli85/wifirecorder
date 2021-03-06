package com.flat.app.testing;


import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.flat.R;
import com.flat.app.PrefsActivity;

public class OtherTestsFragment extends ListFragment {

	/** Interface for communication with container Activity. */
	public interface Callbacks {
		void showDetails(int group);
		boolean isDualPane();
	}

	private Callbacks mCallbacks;
	private ListAdapter mAdapter;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	mCallbacks = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement the Fragment's interface.");
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ArrayAdapter<String>(getActivity(), 
        		R.layout.textview_main, getResources().getStringArray(R.array.mainItems));
        setListAdapter(mAdapter);
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (((Callbacks) getActivity()).isDualPane()) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }   
//        setRetainInstance(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	mCallbacks.showDetails(position);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), PrefsActivity.class));
                break;

        }
        return true;
    }
}
