package com.naman14.xposedui;

/**
 * Created by naman on 04/05/15.
 */
import android.content.res.XModuleResources;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookZygoteInit, IXposedHookInitPackageResources, IXposedHookLoadPackage  {

    private static String MODULE_PATH = null;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;

    }
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

    }

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals("com.android.systemui"))
            return;
        final XModuleResources modRes=XModuleResources.createInstance(MODULE_PATH, resparam.res);
        resparam.res.hookLayout("com.android.systemui", "layout", "qs_panel", new XC_LayoutInflated() {

            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                ViewGroup clock = (ViewGroup) liparam.view.findViewById(
                        liparam.res.getIdentifier("quick_settings_panel", "id", "com.android.systemui"));

               clock.setBackground(modRes.getDrawable(R.drawable.photo2));

            }
        });
        resparam.res.hookLayout("com.android.systemui", "layout", "status_bar_expanded_header", new XC_LayoutInflated() {

            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                RelativeLayout clock = (RelativeLayout) liparam.view.findViewById(
                        liparam.res.getIdentifier("header", "id", "com.android.systemui"));

                clock.setBackground(modRes.getDrawable(R.drawable.ic_header));

            }
        });
    }
}
