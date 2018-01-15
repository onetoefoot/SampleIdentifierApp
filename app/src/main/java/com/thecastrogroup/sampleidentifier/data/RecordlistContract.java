package com.thecastrogroup.sampleidentifier.data;

import android.provider.BaseColumns;

/**
 * Created by jodycastro on 1/12/18.
 */

public class RecordlistContract {

    public static final class RecordlistEntry implements BaseColumns {

        public static final String TABLE_NAME = "recordlist";
        public static final String COLUMN_IDENTIFIER = "identifier";
        public static final String COLUMN_SAMPLE = "sample";
        public static final String COLUMN_SESSION = "session";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}
