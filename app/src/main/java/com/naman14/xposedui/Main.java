package com.naman14.xposedui;

/**
 * Created by naman on 04/05/15.
 */
import android.content.Context;
import android.content.res.XModuleResources;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.callMethod;

public class Main implements IXposedHookZygoteInit, IXposedHookInitPackageResources, IXposedHookLoadPackage  {

    private static String MODULE_PATH = null;

    private static XModuleResources modRes;
    private static InitPackageResourcesParam mResparam;
    private static ClassLoader classLoader;
    public static String SYSTEM_UI_PACKAGE_NAME = "com.android.systemui";

   private static Context context;
    private static Drawable drawable;
   public static final XSharedPreferences preferences=new XSharedPreferences("com.naman14.xposedui","ALBUM_ART");

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;

    }
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals(SYSTEM_UI_PACKAGE_NAME))
        return;

         classLoader=lpparam.classLoader;

        Object activityThread = callStaticMethod(
                findClass("android.app.ActivityThread", null), "currentActivityThread");
         context = (Context) callMethod(activityThread, "getSystemContext");

        XposedUtils.registerMediaReciever(context);
        XposedUtils.registerXReciever(context);

    }

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {


        if (!resparam.packageName.equals(SYSTEM_UI_PACKAGE_NAME))
        return;

        mResparam=resparam;
        modRes=XModuleResources.createInstance(MODULE_PATH, resparam.res);
        if (context!=null) {
            if (!preferences.getBoolean("BLUR",true))
            HookDrawables.hook(Utils.createDrawable(context, Uri.parse(preferences.getString("URI", ""))));
            else HookDrawables.hook(Utils.createBlurredImage(Utils.createDrawable(context,Uri.parse(preferences.getString("URI",""))),context));
        }
        else XposedBridge.log("Context is null");

    }



    public static XModuleResources getXposedModuleResources() {

        return modRes;

    }

    public static InitPackageResourcesParam getXposedInitPackageResourcesParam() {

        return mResparam;

    }
    public static ClassLoader getClassLoader(){
        return classLoader;
    }

    public static Context getContext(){
        return context;
    }

    public static XSharedPreferences getXSharedPreferences(){
        return preferences;
    }


}
