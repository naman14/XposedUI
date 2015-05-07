package com.naman14.xposedui;

import android.content.Context;
import android.content.res.XModuleResources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by naman on 07/05/15.
 */
public class Utils {


    public static String PREFS_CHANGED="com.naman14.xposedui.PREFS_CHANGED";
    private static Drawable albumart;
    private static XModuleResources modRes;

   static RenderScript rs;

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

    public static Drawable createBlurredImage(Drawable drawable,Context context) {

        rs = RenderScript.create(context);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        BitmapDrawable bitDw = ((BitmapDrawable) drawable);
        Bitmap bitmap = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
        Bitmap blurTemplate = BitmapFactory.decodeStream(bis, null, options);

        final android.support.v8.renderscript.Allocation input = android.support.v8.renderscript.Allocation.createFromBitmap(rs, blurTemplate); //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(8f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(blurTemplate);
        Drawable dr = new BitmapDrawable(blurTemplate);

        return dr;
    }
}
