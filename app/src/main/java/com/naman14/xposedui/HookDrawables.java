package com.naman14.xposedui;

import android.content.res.XModuleResources;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

/**
 * Created by naman on 07/05/15.
 */
public class HookDrawables {


    public static void hook(final Drawable drawable){


       XC_InitPackageResources.InitPackageResourcesParam resparam=Main.getXposedInitPackageResourcesParam();
       final XModuleResources modRes=Main.getXposedModuleResources();


        resparam.res.hookLayout("com.android.systemui", "layout", "status_bar_expanded_header", new XC_LayoutInflated() {

            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
               RelativeLayout header = (RelativeLayout) liparam.view.findViewById(
                        liparam.res.getIdentifier("header", "id", "com.android.systemui"));

                header.setBackground(drawable);

            }
        });
        resparam.res.hookLayout("com.android.systemui", "layout", "qs_panel", new XC_LayoutInflated() {

            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                ViewGroup quicksettings = (ViewGroup) liparam.view.findViewById(
                        liparam.res.getIdentifier("quick_settings_panel", "id", "com.android.systemui"));
                quicksettings.setBackground(drawable);

            }
        });
    }

}
