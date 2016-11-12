package com.example.natarajan.transitproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ViewHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        try {

            DatabaseHelper dbHelper = DatabaseHelper.getInstance(getBaseContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.natarajan.assaignment1/databases/patients_natarajan.db", null);
            Cursor cur = db.rawQuery("select * from search_history", null);
            TableLayout history_tbl = (TableLayout) findViewById(R.id.history_table);
            TableRow row= new TableRow(this);
            TextView t1 = new TextView(this);
            t1.setText("Time stamp");
            TextView t2 = new TextView(this);
            t2.setText("Source");
            TextView t3 = new TextView(this);
            t3.setText("Destination");
            row.addView(t1); row.addView(new TextView(this)); row.addView(t2); row.addView(new TextView(this)); row.addView(t3);
            history_tbl.addView(row,0);

            cur.moveToFirst();
            int i=1;
            while (cur.isAfterLast() != true) {
                String time = (String) cur.getString(0);
                String src = (String) cur.getString(1);
                String desti = (String) cur.getString(2);

                // add new row
                TableRow r= new TableRow(this);
                TextView t4 = new TextView(this);
                t4.setText(time);
                TextView t5 = new TextView(this);
                t5.setText(src);
                TextView t6 = new TextView(this);
                t6.setText(desti);
                r.addView(t4); r.addView(new TextView(this));r.addView(t5);r.addView(new TextView(this)); r.addView(t6);
                history_tbl.addView(r,i);
                i++;
                history_tbl.addView(new TableRow(this), i);

                cur.moveToNext();
                i++;
            }

            cur.close();
            db.close();
            dbHelper.close();
        }catch(Exception ex) {
            Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
