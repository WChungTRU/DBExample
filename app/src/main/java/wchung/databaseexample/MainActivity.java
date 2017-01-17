package wchung.databaseexample;

import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.name;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public String fname_in, lname_in, email_in;
    public Button add, query;
    public DBAdapter myDB;
    public TextView fname_tv, lname_tv, email_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] list = getResources().getStringArray(R.array.items);

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
        ListView listView = (ListView) findViewById(R.id.list);

        add = (Button) findViewById(R.id.add_button);
        query = (Button) findViewById(R.id.query_button);

        fname_tv = (TextView) findViewById(R.id.fname_value);
        lname_tv = (TextView) findViewById(R.id.lname_value);
        email_tv = (TextView) findViewById(R.id.email_value);

        listView.setAdapter(itemsAdapter);

        add.setOnClickListener(this);
        query.setOnClickListener(this);

        openDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDB = new DBAdapter(this);
        myDB.open();
    }
    private void closeDB() {
        myDB.close();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.add_button:
                //toast a message
                Toast.makeText(this, "add pressed",
                        Toast.LENGTH_LONG).show();

                fname_in = fname_tv.getText().toString();
                lname_in = lname_tv.getText().toString();
                email_in = email_tv.getText().toString();

                if(fname_in != "" || lname_in != "" || email_in != "") {

                    onClick_AddRecord(view);
                }
                break;
            case R.id.query_button:
                //toast a message
                Toast.makeText(this, "query pressed",
                        Toast.LENGTH_LONG).show();

                onClick_DisplayRecords(view);
                break;
            default:
                break;
        }
    }

    public void onClick_AddRecord(View v) {

        long newId = myDB.insertRow(fname_in, lname_in, email_in);

        // Query for the record we just added to confirm.
        // Use the ID:
        Cursor cursor = myDB.getRow(newId);
        displayRecordSet(cursor);
        // [TO_DO_B2]
        // Confirm that data is written correctly by reading it back and comparing it with the original data then display a toast message to confirm the transaction.
        // [TO_DO_B3]
        // Update the list view
    }

    public void onClick_ClearAll(View v) {
        myDB.deleteAll();
        // [TO_DO_B4]
        // Confirm that all rows are deleted. Think about what would be the best way to do that?
        // [TO_DO_B5]
        // Update the list view
    }

    public void onClick_DisplayRecords(View v) {
        Cursor cursor = myDB.getAllRows();
        displayRecordSet(cursor);


    }

    // Display an entire recordset to the screen.
    private void displayRecordSet(Cursor cursor) {
        String message = "";
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String fname = cursor.getString(DBAdapter.COL_FNAME);
                String lname = cursor.getString(DBAdapter.COL_LNAME);
                String email = cursor.getString(DBAdapter.COL_EMAIL);

                // Append data to the message:
                message += "id=" + id
                        +", firstname=" + fname
                        +", lastname=" + lname
                        +", email=" + email
                        +"\n";

                // [TO_DO_B6]
                // Create arraylist(s)? and use it(them) in the list view
            } while(cursor.moveToNext());
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();

        // [TO_DO_B7]
        // Update the list view

        // [TO_DO_B8]
        // Display a Toast message
    }
}
