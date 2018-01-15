package com.thecastrogroup.sampleidentifier;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thecastrogroup.sampleidentifier.R;
import com.thecastrogroup.sampleidentifier.data.RecordlistContract;


/**
 * Created by jodycastro on 1/12/18.
 */

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordViewHolder> {

    private static final String TAG = RecordListAdapter.class.getSimpleName();

    private Cursor mCursor;
    private Context mContext;

    /**
     * Constructor using the context and the db cursor
     *
     * @param context the calling context/activity
     */
    public RecordListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.record_list_item, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null

        // Update the view holder with the information needed to display
        String identifier = mCursor.getString(mCursor.getColumnIndex(RecordlistContract.RecordlistEntry.COLUMN_IDENTIFIER));
        String sample = mCursor.getString(mCursor.getColumnIndex(RecordlistContract.RecordlistEntry.COLUMN_SAMPLE));
        String timestamp = mCursor.getString(mCursor.getColumnIndex(RecordlistContract.RecordlistEntry.COLUMN_TIMESTAMP));
        long id = mCursor.getLong(mCursor.getColumnIndex(RecordlistContract.RecordlistEntry._ID));

        holder.identifier.setText(identifier);
        holder.sample.setText(sample);
        holder.timestamp.setText(timestamp);
        holder.itemView.setTag(id);
    }

    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class RecordViewHolder extends RecyclerView.ViewHolder {

        // Will display the identifier
        TextView identifier;
        // Will display the recordId
        TextView sample;
        // Will display the timestamp
        TextView timestamp;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link RecordListAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public RecordViewHolder(View itemView) {
            super(itemView);
            identifier = (TextView) itemView.findViewById(R.id.identifier_text_view);
            sample = (TextView) itemView.findViewById(R.id.data_id_text_view);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp_text_view);
        }

    }
}


