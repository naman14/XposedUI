package com.naman14.xposedui;

import android.content.Context;
import android.content.res.XModuleResources;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

/**
 * Created by naman on 07/05/15.
 */
public class HookDrawables {


    public static void hook(){

        Context context=Main.getContext();

       final XSharedPreferences preferences=new XSharedPreferences("com.naman14.xposedui","ALBUM_ART");

       XC_InitPackageResources.InitPackageResourcesParam resparam=Main.getXposedInitPackageResourcesParam();
       final XModuleResources modRes=Main.getXposedModuleResources();

        resparam.res.hookLayout("com.android.systemui", "layout", "qs_panel", new XC_LayoutInflated() {

            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
               ViewGroup header = (ViewGroup) liparam.view.findViewById(
                        liparam.res.getIdentifier("quick_settings_panel", "id", "com.android.systemui"));
                header.setBackground(modRes.getDrawable(R.drawable.ic_header));

            }
        });
        resparam.res.hookLayout("com.android.systemui", "layout", "status_bar_expanded_header", new XC_LayoutInflated() {

            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
               RelativeLayout quicksettings = (RelativeLayout) liparam.view.findViewById(
                        liparam.res.getIdentifier("header", "id", "com.android.systemui"));

                quicksettings.setBackground(modRes.getDrawable(R.drawable.photo2));

            }
        });
    }

}
