package com.promessdev.cloud.conciergejava;

import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zach on 9/15/2017.
 */

public class CacheFragment extends Fragment {
    private static final String TAG = "Cache Fragment";
    private TextView tv;
    private TextView oldTv;
    private Button btnTEST;
    private Integer count = 0;
    private Boolean mShouldRerender = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cache, container, false);
        tv = (TextView) view.findViewById(R.id.testText);
        count++;
        if (mShouldRerender) {
            FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
            t.setAllowOptimization(false);
            t.detach(this).attach(this).commitAllowingStateLoss();
            mShouldRerender = false;
        }
        readText();
        return view;

    }

    /** Method to read in a text file placed in the res/raw directory of the application. The
     method reads in all lines of the file sequentially. */

    private void readRaw(){
        tv.append("\nData read from res/raw/textfile.txt:");
        tv.append("\nCount: ");
        tv.append(count.toString());
        InputStream is = this.getResources().openRawResource(R.raw.textfile);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr, 8192);    // 2nd arg is buffer size

        // More efficient (less readable) implementation of above is the composite expression
    /*BufferedReader br = new BufferedReader(new InputStreamReader(
            this.getResources().openRawResource(R.raw.textfile)), 8192);*/

        try {
            String test;
            while (true){
                test = br.readLine();
                // readLine() returns null if no more lines in the file
                if(test == null) break;
                tv.append("\n"+"    "+test);
            }
            isr.close();
            is.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv.append("\n\nThat is all");
    }

    private void readText() {
        //Find the directory for the SD Card using the API
        //*Don't* hardcode "/sdcard"
        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File file = new File(sdcard,"myData.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        tv.append("\nData read form SD card:");
        tv.append("\nCount: ");
        tv.append(count.toString());
        tv.append("\n");
        //Set the text
        tv.append(text);
        tv.append("\n\nThat is all");
    }

    private void rerenderFragment() {
        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.setAllowOptimization(false);
        t.detach(this).attach(this).commitAllowingStateLoss();
    }

}
