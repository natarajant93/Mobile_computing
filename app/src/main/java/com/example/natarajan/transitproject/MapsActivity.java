package com.example.natarajan.transitproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // create a dummy database and fill values for history data
        DatabaseHelper dbHelper;
        SQLiteDatabase db;
        dbHelper = DatabaseHelper.getInstance(this);
        db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("create table IF NOT EXISTS search_history (time_stamp text, source text, destination text)");
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            db.execSQL("insert into search_history(time_stamp, source, destination) values('"+timeStamp+"','src1','dest1')");
            db.execSQL("insert into search_history(time_stamp, source, destination) values('"+timeStamp+"','src2','dest2')");
            db.execSQL("insert into search_history(time_stamp, source, destination) values('"+timeStamp+"','src3','dest3')");
        }catch(Exception ex) {
            Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        db.close();
        dbHelper.close();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button view_history_button = (Button) findViewById(R.id.view_history);
        view_history_button.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                // Perform action on click

                Intent activityChangeIntent = new Intent(MapsActivity.this, ViewHistory.class);

                // currentContext.startActivity(activityChangeIntent);

                MapsActivity.this.startActivity(activityChangeIntent);
            }
        }

        );

        Button download_button = (Button) findViewById(R.id.download_map);
        download_button.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                File fileBrochure = new File(Environment.getExternalStorageDirectory() + "/" + "abc.pdf");
                if (!fileBrochure.exists())
                {
                    CopyAssetsbrochure();
                }

                /** PDF reader code */
                File file = new File(Environment.getExternalStorageDirectory() + "/" + "abc.pdf");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),"application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try
                {
                    getApplicationContext().startActivity(intent);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(MapsActivity.this, "NO Pdf Viewer", Toast.LENGTH_SHORT).show();
                }
            }

            //method to write the PDFs file to sd card
            private void CopyAssetsbrochure() {
                AssetManager assetManager = getAssets();
                String[] files = null;
                try
                {
                    files = assetManager.list("");
                }
                catch (IOException e)
                {
                    Log.e("tag", e.getMessage());
                }
                for(int i=0; i<files.length; i++)
                {
                    String fStr = files[i];
                    if(fStr.equalsIgnoreCase("abc.pdf"))
                    {
                        InputStream in = null;
                        OutputStream out = null;
                        try
                        {
                            in = assetManager.open(files[i]);
                            out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + files[i]);
                            copyFile(in, out);
                            in.close();
                            in = null;
                            out.flush();
                            out.close();
                            out = null;
                            break;
                        }
                        catch(Exception e)
                        {
                            Log.e("tag", e.getMessage());
                        }
                    }
                }
            }

            private void copyFile(InputStream in, OutputStream out) throws IOException {
                byte[] buffer = new byte[1024];
                int read;
                while((read = in.read(buffer)) != -1){
                    out.write(buffer, 0, read);
                }
            }
        }

        );
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}
