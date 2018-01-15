package com.thecastrogroup.sampleidentifier;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.thecastrogroup.sampleidentifier.R;
import com.thecastrogroup.sampleidentifier.data.RecordlistContract;
import com.thecastrogroup.sampleidentifier.data.RecordlistDbHelper;

public class ViewActivity extends AppCompatActivity {

    private static final String TAG = ViewActivity.class.getSimpleName();

    private RecordListAdapter mAdapter;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        RecyclerView recordlistRecyclerView;
        recordlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_records_list_view);
        recordlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecordlistDbHelper dbHelper = new RecordlistDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getAllRecords();
        Log.d(TAG, "The total cursor count is: " + cursor.getCount());
        mAdapter = new RecordListAdapter(this, cursor);
        recordlistRecyclerView.setAdapter(mAdapter);
        // Create an item touch helper to handle swiping items off the list
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //get the id of the item being swiped
                long id = (long) viewHolder.itemView.getTag();
                //remove from DB
                removeRecord(id);
                //update the list
                mAdapter.swapCursor(getAllRecords());
            }

        }).attachToRecyclerView(recordlistRecyclerView);
    }

    /**
     * Query the mDb and get all records from the recordlist table
     *
     * @return Cursor containing the list of data
     */
    private Cursor getAllRecords() {
        return mDb.query(
                RecordlistContract.RecordlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                RecordlistContract.RecordlistEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    /**
     * Removes the record with the specified id
     *
     * @param id the DB id to be removed
     * @return True: if removed successfully, False: if failed
     */
    private boolean removeRecord(long id) {
        return mDb.delete(RecordlistContract.RecordlistEntry.TABLE_NAME, RecordlistContract.RecordlistEntry._ID + "=" + id, null) > 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_begin) {
            Intent intent = new Intent(ViewActivity.this, AddActivity.class);
            startActivity(intent);
        } else if (itemThatWasClickedId == R.id.action_view) {
            Intent intent = new Intent(ViewActivity.this, ViewActivity.class);
            startActivity(intent);
//        } else if (itemThatWasClickedId == R.id.action_send) {
//            Intent intent = new Intent(ViewActivity.this, ViewActivity.class);
//            startActivity(intent);
//            Context context = ViewActivity.this;
//            String textToShow = "SendActivity clicked";
//            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
