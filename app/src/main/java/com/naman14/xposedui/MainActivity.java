package com.naman14.xposedui;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {


    ImageView album;
    SharedPreferences preferences;
    SharedPreferences.Editor prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        album=(ImageView) findViewById(R.id.album);
        preferences=getSharedPreferences("ALBUM_ART",MODE_WORLD_READABLE);
        prefs=preferences.edit();



        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.htc.music.metachanged");
        iF.addAction("fm.last.android.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.nullsoft.winamp.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");
        iF.addAction("com.miui.player.metachanged");
        iF.addAction("com.real.IMP.metachanged");
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.rdio.android.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.andrew.apollo.metachanged");

        registerReceiver(mReceiver, iF);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String track = intent.getStringExtra("track");
            long songId = intent.getLongExtra("id", -1);
            //get the albumid using media/song id
            if(songId!=-1) {
                String selection = MediaStore.Audio.Media._ID + " = "+songId+"";

                Cursor cursor = getContentResolver().query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[] {
                                MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ALBUM_ID},
                        selection, null, null);

                if (cursor.moveToFirst()) {
                    long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    Log.d("Album ID : ", "" + albumId);

                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                    Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                    prefs.putString("URI",albumArtUri.toString());
                    prefs.commit();



                }
                cursor.close();
            }

            Toast.makeText(MainActivity.this, track, Toast.LENGTH_SHORT).show();
        }
    };




}
