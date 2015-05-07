package com.naman14.xposedui;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by naman on 07/05/15.
 */
public class XposedUtils {

    static SharedPreferences preferences;
    static SharedPreferences.Editor prefs;

    public static void registerMediaReciever(Context context){

        preferences=context.getSharedPreferences("ALBUM_ART",Context.MODE_WORLD_READABLE);
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

        context.registerReceiver(mReceiver, iF);
    }
    private static BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String track = intent.getStringExtra("track");
            long songId = intent.getLongExtra("id", -1);
            //get the albumid using media/song id
            if(songId!=-1) {
                String selection = MediaStore.Audio.Media._ID + " = "+songId+"";

                Cursor cursor = context.getContentResolver().query(
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
                    HookDrawables.hook(Utils.createDrawable(context, Uri.parse(preferences.getString("URI",""))));

                }
                cursor.close();
            }

            Toast.makeText(context, track, Toast.LENGTH_SHORT).show();
        }
    };

}
