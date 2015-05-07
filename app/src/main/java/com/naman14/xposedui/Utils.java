package com.naman14.xposedui;

import android.content.Context;
import android.content.res.XModuleResources;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by naman on 07/05/15.
 */
public class Utils {

    private static Drawable albumart;
    private static XModuleResources modRes;

    public static Drawable createDrawable(Context context,Uri uri){
        modRes=Main.getXposedModuleResources();
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            albumart = Drawable.createFromStream(inputStream, uri.toString());
        } catch (FileNotFoundException e) {
            albumart = modRes.getDrawable(R.drawable.photo2);
            XposedBridge.log("lol2");
        }
        return albumart;
    }
}
