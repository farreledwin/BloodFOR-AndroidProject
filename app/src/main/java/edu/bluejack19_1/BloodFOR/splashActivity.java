package edu.bluejack19_1.BloodFOR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class splashActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView persentase;
    private int Value = 0;
    public static String email,uid;
    public static Boolean cekGoogle, cekFb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Bundle extras = getIntent().getExtras();
        progressBar = findViewById(R.id.progress);
        persentase = findViewById(R.id.persentase);
        progressBar.setProgress(0); //Set Progress Dimulai Dari O
        email = extras.getString("email");
        uid = extras.getString("uid");
        cekGoogle = extras.getBoolean("cekGoogle");
        cekFb = extras.getBoolean("cekFb");
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // Menampung semua data yang ingin diproses oleh thread
                persentase.setText(String.valueOf(Value)+"%");
                if(Value == progressBar.getMax()){
                    Toast.makeText(getApplicationContext(), "Progress Completed", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(splashActivity.this, MainActivity.class);
                    myIntent.putExtra("uid", uid);
                    myIntent.putExtra("cekGoogle", true);
                    myIntent.putExtra("cekFb", false);
                    myIntent.putExtra("email",email);
                    startActivity(myIntent);
                    finish();
                }
                Value++;
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int w=0; w<=progressBar.getMax(); w++){
                        progressBar.setProgress(w); // Memasukan Value pada ProgressBar
                        // Mengirim pesan dari handler, untuk diproses didalam thread
                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(100); // Waktu Pending 100ms/0.1 detik
                    }
                }catch(InterruptedException ex){
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
