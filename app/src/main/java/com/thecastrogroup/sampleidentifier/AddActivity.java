package com.thecastrogroup.sampleidentifier;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.thecastrogroup.sampleidentifier.data.RecordlistContract;
import com.thecastrogroup.sampleidentifier.data.RecordlistDbHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddActivity extends AppCompatActivity {


    private static final String TAG = AddActivity.class.getSimpleName();

    Button btnScan;
    TextView identifierTextView;

    String scannedData = null;

    String session = UUID.randomUUID().toString();

    private RecordListAdapter mAdapter;

    private SQLiteDatabase mDb;

    Map<String, String> globals_params = new HashMap<String, String>();
    Map<String, String> globals_data = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        globals_params.clear();
        globals_data.clear();

        doSetup();

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
        String[] selectionArgs = new String[]{session};
        return mDb.query(
                RecordlistContract.RecordlistEntry.TABLE_NAME,
                null,
                RecordlistContract.RecordlistEntry.COLUMN_SESSION + " = ? ",
                selectionArgs,
                null,
                null,
                RecordlistContract.RecordlistEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    public long addRecord(String identifier, String sample) {
        ContentValues cv = new ContentValues();
        cv.put(RecordlistContract.RecordlistEntry.COLUMN_IDENTIFIER, identifier);
        cv.put(RecordlistContract.RecordlistEntry.COLUMN_SAMPLE, sample);
        cv.put(RecordlistContract.RecordlistEntry.COLUMN_SESSION, session);
        return mDb.insert(RecordlistContract.RecordlistEntry.TABLE_NAME, null, cv);
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

    public void doSetup() {

        identifierTextView = findViewById(R.id.identifierTextView);

        btnScan = (Button)findViewById(R.id.btnScan);
        btnScan.setOnClickListener(btnScanOnClickListener);
    }

    private View.OnClickListener btnScanOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestPermission();
            scan();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.record_collection_done) {
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Need to fiture a way to offload this to another class
     */
    protected void requestPermission() {
        if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            Integer requestCode = 0;
            ActivityCompat.requestPermissions(AddActivity.this, new String[] {Manifest.permission.CAMERA}, requestCode);
        }
    }

    protected void scan() {
        Intent intent = new Intent(AddActivity.this, CaptureActivity.class);
        intent.setAction("com.google.zxing.client.android.SCAN");
        intent.putExtra("SAVE_HISTORY", false);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                processScan(contents);
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "RESULT_CANCELED");
            }
        }
    }

    /**
     *
     * @param contents
     */
    protected void processScan(String contents) {
        Log.d(TAG, "contents: " + contents);
        scannedData = contents.trim();
        if(scannedData != null && !scannedData.isEmpty()) {
            if (globals_params.containsKey("identifier")) {
                long recordId = addRecord(globals_params.get("identifier"), scannedData);
                mAdapter.swapCursor(getAllRecords());
                Log.d(TAG, "recordId: " + recordId);
            } else {
                btnScan.setText("Scan Sample");
                globals_params.put("identifier", scannedData);
                identifierTextView.setText("Identifier: " + scannedData);
            }
        }
    }

}
