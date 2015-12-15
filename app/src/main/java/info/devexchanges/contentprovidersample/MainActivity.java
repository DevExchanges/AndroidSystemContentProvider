package info.devexchanges.contentprovidersample;

import android.os.Bundle;
import android.provider.Browser;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.content.CursorLoader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private View btnGetCallLogs;
    private View btnGetBookmarks;
    private View btnGetMedia;
    private ArrayList<String> arrayList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetBookmarks = findViewById(R.id.btn_get_bookmark);
        btnGetCallLogs = findViewById(R.id.btn_get_call_log);
        btnGetMedia = findViewById(R.id.btn_get_media);
        listView = (ListView) findViewById(R.id.list_view);

        btnGetMedia.setOnClickListener(this);
        btnGetCallLogs.setOnClickListener(this);
        btnGetBookmarks.setOnClickListener(this);

        arrayList = new ArrayList<>();
    }

    @SuppressWarnings("PointlessBooleanExpression")
    public void accessTheCallLog() {
        Toast.makeText(this, "Get all Call logs", Toast.LENGTH_SHORT).show();
        String[] projection = new String[]{Calls.DATE, Calls.NUMBER, Calls.DURATION};

        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, null);

        assert c != null;
        arrayList.clear();
        c.moveToFirst();

        while (!c.isAfterLast()) {
            String phoneNumber = Calls.NUMBER;
            int phoneIndex = c.getColumnIndex(phoneNumber);
            arrayList.add(c.getString(phoneIndex));
            c.moveToNext();
        }
        c.close();
        setListViewAdapter();
    }


    public void accessMediaStore() {
        Toast.makeText(this, "Show all Media Objects!", Toast.LENGTH_SHORT).show();
        String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.MIME_TYPE
        };
        CursorLoader loader = new CursorLoader(this, Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        Cursor c = loader.loadInBackground();
        c.moveToFirst();
        arrayList.clear();

        while (!c.isAfterLast()) {
            String mediaName = MediaStore.MediaColumns.DISPLAY_NAME;
            int nameIndex = c.getColumnIndex(mediaName);
            arrayList.add(c.getString(nameIndex));
            c.moveToNext();
        }

        c.close();
        setListViewAdapter();
    }

    private void setListViewAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, arrayList);
        listView.setAdapter(adapter);
    }

    public void accessBookmarks() {
        Toast.makeText(this, "Show all Bookmarks", Toast.LENGTH_SHORT).show();
        String[] projection = {
                Browser.BookmarkColumns.TITLE,
                Browser.BookmarkColumns.URL,
        };
        Cursor c = getContentResolver().query(Browser.BOOKMARKS_URI, projection, null, null, null);
        assert c != null;
        c.moveToFirst();
        arrayList.clear();

        while (!c.isAfterLast()) {
            int titleIndex = c.getColumnIndex(Browser.BookmarkColumns.TITLE);
            if (!c.getString(titleIndex).equals("")) {
                arrayList.add(c.getString(titleIndex));
            }
            c.moveToNext();
        }
        c.close();
        setListViewAdapter();
    }

    @Override
    public void onClick(View v) {
        if (v == btnGetBookmarks) {
            accessBookmarks();
        } else if (v == btnGetCallLogs) {
            accessTheCallLog();
        } else if (v == btnGetMedia) {
            accessMediaStore();
        } else {
            //????
        }
    }
}