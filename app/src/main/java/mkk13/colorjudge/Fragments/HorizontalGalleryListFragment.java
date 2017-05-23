package mkk13.colorjudge.Fragments;

/**
 * Created by mkk-1 on 17/05/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.content.Loader;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.List;

import mkk13.colorjudge.Activities.GuessColorActivity;
import mkk13.colorjudge.Activities.JudgePreActivity;
import mkk13.colorjudge.PhotoGalleryAsyncLoader;
import mkk13.colorjudge.Adapters.PhotoAdapter;
import mkk13.colorjudge.Adapters.PhotoItem;
import mkk13.colorjudge.R;

/**
 * This is an example which will load all the images on your phone into a grid using a background
 * image AsyncLoader.
 *
 * Reference: http://developer.android.com/reference/android/content/AsyncTaskLoader.html
 *
 * Created by Rex St. John (on behalf of AirPair.com) on 3/4/14.
 */


public class HorizontalGalleryListFragment extends Fragment implements AbsListView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<List<PhotoItem>>  {

    // Ivars.
    // Left and right scrolling two way view
    protected PhotoAdapter mAdapter;
    protected ArrayList<PhotoItem> mPhotoListItem;
    protected TextView mEmptyTextView;

    /**
     * Required empty constructor
     */
    public HorizontalGalleryListFragment() {
        super();
    }

    /**
     * Static factory method
     * @param sectionNumber
     * @return
     */
    public static HorizontalGalleryListFragment newInstance(int sectionNumber) {
        HorizontalGalleryListFragment fragment = new HorizontalGalleryListFragment();
        Bundle args = new Bundle();
        args.putInt("ARG_SECTION_NUMBER", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an empty loader and pre-initialize the photo list items as an empty list.
        Context context = getActivity().getBaseContext();

        // Set up empty mAdapter
        mPhotoListItem = new ArrayList<PhotoItem>() ;
        mAdapter = new PhotoAdapter(context,
                R.layout.photo_item,
                mPhotoListItem, false);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.horizontal_gallery_list_fragment, container, false);

        // Set the mAdapter
        mEmptyTextView = (TextView)view.findViewById(R.id.empty);

        TwoWayView mHorizontalListView = (TwoWayView)  view.findViewById(R.id.horizontalList);
        mHorizontalListView.setAdapter(mAdapter);
        mHorizontalListView.setOnItemClickListener(this);
        mHorizontalListView.setItemMargin(10);

        resolveEmptyText();

        return view;
    }

    /**
     * Used to show a generic empty text warning. Override in inheriting classes.
     */
    protected void resolveEmptyText(){
        if(mAdapter.isEmpty()){
            mEmptyTextView.setVisibility(View.VISIBLE);
            setEmptyText();
        } else {
            mEmptyTextView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * This is only triggered when the user selects a single photo.
     * @param parent
     * @param view
     * @param position
     * @param id
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Tell the share builder to add the photo to the share operation.
        PhotoItem photoListItem = this.mAdapter.getItem(position);
        Activity parentActivity = getActivity();

        if (photoListItem != null) {
            if (parentActivity instanceof GuessColorActivity)
                ((GuessColorActivity)parentActivity).cropImage(photoListItem.getFullImageUri());
            else if (parentActivity instanceof JudgePreActivity)
                ((JudgePreActivity)parentActivity).cropImage(photoListItem.getFullImageUri());
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText() {
        mEmptyTextView.setText("No Photos!");
    }

    /**
     * Loader Handlers for loading the photos in the background.
     */
    @Override
    public Loader<List<PhotoItem>> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader with no arguments, so it is simple.
        return new PhotoGalleryAsyncLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<PhotoItem>> loader, List<PhotoItem> data) {
        // Set the new data in the mAdapter.
        mPhotoListItem.clear();

        for(int i = 0; i < data.size();i++){
            PhotoItem item = data.get(i);
            mPhotoListItem.add(item);
        }

        mAdapter.notifyDataSetChanged();
        resolveEmptyText();
    }

    @Override
    public void onLoaderReset(Loader<List<PhotoItem>> loader) {
        // Clear the data in the mAdapter.
        mPhotoListItem.clear();
        mAdapter.notifyDataSetChanged();
        resolveEmptyText();
    }
}