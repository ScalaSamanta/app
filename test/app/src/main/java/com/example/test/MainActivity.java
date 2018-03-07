package com.example.test;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.text.TextUtils.split;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button[] buttons;
    int[] ids = {R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9};
    boolean isLevelSelection;
    int nQuestions;
    int randomNumber;
    long tStart;
    TextView tv,time;
    int i=0;
    ArrayList<String> list;


    private String filename="timeFile.csv";
   // private String filepath=Environment.getExternalStorageDirectory().getAbsolutePath();
    private String filepath="MyFileStorage";
    File myTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttons = new Button[ids.length];
        for(int i=0;i<ids.length;i++)
            buttons[i] = (Button)findViewById(ids[i]);
        tv = (TextView)findViewById(R.id.tv);
        time=(TextView)findViewById(R.id.time);
        myTime = new File(getExternalFilesDir(filepath), filename);
        list = new ArrayList<String>();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText("How many numbers do you want?");
                tStart=System.currentTimeMillis();
            }
        });

        isLevelSelection=true;
        nQuestions=0;
        randomNumber=0;
    }

    /*public void savegetTime(View view)
    {
        File file=new File(path+ "/saved.csv");
        String[] saveTime=String.valueOf(tv.getText()).split(System.getProperty("line.separator"));

        tv.setText("");

        Toast.makeText(getApplicationContext(), "Saved",Toast.LENGTH_LONG).show();
        save(file,saveTime);
    }*/


    public static void save(File file, ArrayList<String> list)
    {
        FileOutputStream fos = null;
        try
        {
            fos=new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try
        {
            for(int i=0;i<list.size();i++)
            {
                fos.write(list.get(i).getBytes());
                if (i < list.size() - 1)
                    fos.write("\n".getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

 /* public void saveTimeonClick(String s) {

      if (isExternalStorageWritable() || isExternalStorageReadaOnly()) {
          myTime = new File(getExternalFilesDir(filepath), filename);
      try {

          FileOutputStream out=new FileOutputStream(myTime);
            if (!myTime.exists()) {
                myTime.createNewFile();
            }
            //out.write(time.getText().toString().getBytes());
            out.write(s.getBytes());
            out.close();
          Toast.makeText(getApplicationContext(), "Created",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
       Toast.makeText(getApplicationContext(), "Saved",Toast.LENGTH_LONG).show();
      }
  }*/


    @Override
    public void onClick(View view) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<ids.length;i++)
                    buttons[i].setBackgroundColor(Color.TRANSPARENT);
            }
        });
        int p;
        for(p=0;view.getId()!=ids[p];p++);
        final Button b = buttons[p];
        if(isLevelSelection) {

            nQuestions=p+1;
            isLevelSelection=false;
            randomNumber = (int)(Math.random()*8);
            long tEnd = System.currentTimeMillis();
            final long tDelta = tEnd - tStart;
            tStart = System.currentTimeMillis();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText(nQuestions+" questions remaining");
                    buttons[randomNumber].setBackgroundColor(Color.BLUE);
                    time.setText(tDelta+" ms");
                    list.add(String.valueOf(time.getText()));
                    save(myTime,list);
                    i++;
                }
            });
        } else {
            if(randomNumber==p) {
                nQuestions--;
                if(nQuestions>0) {
                    randomNumber = (int) (Math.random() * 8);
                    long tEnd = System.currentTimeMillis();
                    final long tDelta = tEnd - tStart;
                    tStart = System.currentTimeMillis();
                    list.add(String.valueOf(time.getText()));
                    save(myTime,list);
                    i++;

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            tv.setText(nQuestions + " questions remaining");
                            buttons[randomNumber].setBackgroundColor(Color.BLUE);
                            list.add(String.valueOf(time.getText()));
                            save(myTime,list);
                            i++;
                        }
                    });
                } else {
                    isLevelSelection=true;
                    long tEnd = System.currentTimeMillis();
                    final long tDelta = tEnd - tStart;

                    tStart = System.currentTimeMillis();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            tv.setText("You WON: How many numbers do you want?");
                            b.setBackgroundColor(Color.GREEN);
                            list.add(String.valueOf(time.getText()));
                            save(myTime,list);
                            i++;
                        }
                    });
                }
            } else {
                isLevelSelection=true;
                long tEnd = System.currentTimeMillis();
                final long tDelta = tEnd - tStart;

                tStart = System.currentTimeMillis();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        b.setBackgroundColor(Color.RED);
                        tv.setText("YOU FAILED: How many numbers do you want?");
                        time.setText(tDelta+" ms");
                        list.add(String.valueOf(time.getText()));
                        save(myTime,list);
                        i++;
                    }
                });
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadaOnly() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
