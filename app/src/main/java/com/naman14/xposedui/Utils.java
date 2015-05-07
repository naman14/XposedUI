package com.naman14.xposedui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by naman on 07/05/15.
 */
public class Utils {

    private static Drawable albumart;

    public static Drawable createDrawable(Context context,Uri uri){
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            albumart = Drawable.createFromStream(inputStream, uri.toString());
        } catch (FileNotFoundException e) {
            albumart = context.getResources().getDrawable(R.drawable.ic_header);
        }
        return albumart;
    }
}
