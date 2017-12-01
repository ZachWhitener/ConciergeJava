package com.promessdev.cloud.conciergejava;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.promessdev.cloud.conciergejava.Utils.HttpGetRequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zach on 9/15/2017.
 */

public class SyncFragment extends Fragment {
    private static final String TAG = "Sync Fragment";
    private TextView tv;
    private Button syncButton = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync, container, false);
        syncButton = (Button) view.findViewById(R.id.syncButton);
        SetupSyncButtonListener(view);
        return view;

    }
    private void SetupSyncButtonListener(View view) {
       // final Button syncButton = (Button) view.findViewById(R.id.syncButton);
        tv = (TextView) view.findViewById(R.id.testText);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                syncButton.setText("Checking...");
                GetTextFile();


                // writes to a file
                // checkExternalMedia();
                // writeToSDFile();
                // readRaw();

            }
        });
    }

    /** Method to check whether external media available and writable. This is adapted from
     http://developer.android.com/guide/topics/data/data-storage.html#filesExternal */

    private void checkExternalMedia(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        //tv.append("\n\nExternal Media: readable="
        //        +mExternalStorageAvailable+" writable="+mExternalStorageWriteable);
    }

    /** Method to write ascii text characters to file on SD card. Note that you must add a
     WRITE_EXTERNAL_STORAGE permission to the manifest file or this method will throw
     a FileNotFound Exception because you won't have write permission. */

    private void writeToSDFile(String content){

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

        File root = android.os.Environment.getExternalStorageDirectory();
        tv.append("\nExternal file system root: "+root);

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File (root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, "myData.txt");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(content);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv.append("\n\nFile written to "+file);
    }



    private void TestAsyncHttp() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://172.25.63.1/myconnect/", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                tv.append("Successful connecting via testasynchttp");
                syncButton.setText("Sync");
                // Check external media,
                checkExternalMedia();
                writeToSDFile(new String(responseBody));
                rerenderFragment();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                tv.append("Failed connecting via testasynchttp");
                syncButton.setText("Sync");
                WifiDialog dialog = new WifiDialog();

                dialog.show(getActivity().getFragmentManager(), "WifiDialog");
            }
        });
    }

    private void GetTextFile() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://172.25.63.1/myconnect/info.txt", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                tv.append(new String(responseBody));
                syncButton.setText("Connection successful");
                try {
                    Thread.sleep(3000);
                    syncButton.setText("Sync");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                tv.append("Failed connecting via testasynchttp");
                syncButton.setText("Sync");
                WifiDialog dialog = new WifiDialog();
                dialog.show(getActivity().getFragmentManager(), "WifiDialog");
            }
        });
    }
    private String getTextFile() throws ExecutionException, InterruptedException {
        // URL endpoint pointing to USB stick
        String myUrl = "http://172.25.63.1/myconnect/info.txt";

        // String to place result in
        String result;

        // Instantiate new instance of httpget class
        HttpGetRequest getRequest = new HttpGetRequest();

        // Perform doInBackground method, passing in url
        result = getRequest.execute(myUrl).get();

        return result;
    }

    private void rerenderFragment() {
        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.setAllowOptimization(false);
        t.detach(this).attach(this).commitAllowingStateLoss();
    }


}
