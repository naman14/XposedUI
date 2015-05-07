package com.naman14.xposedui;

import android.content.Context;
import android.content.res.XModuleResources;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
            albumart = modRes.getDrawable(R.drawable.ic_header);
        } catch (NullPointerException e){
            albumart=modRes.getDrawable(R.drawable.photo2);
        }
        return albumart;
    }
}
