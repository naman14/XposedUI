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
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookZygoteInit, IXposedHookInitPackageResources, IXposedHookLoadPackage  {

    private static String MODULE_PATH = null;
    private static ViewGroup header;
    private static RelativeLayout quicksettings;

   private static XModuleResources modRes;
    private static InitPackageResourcesParam mResparam;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;

    }
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

    }

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {

        mResparam=resparam;

        modRes=XModuleResources.createInstance(MODULE_PATH, resparam.res);

        HookDrawables.hook();



    }



    public static XModuleResources getXposedModuleResources() {

        return modRes;

    }

    public static InitPackageResourcesParam getXposedInitPackageResourcesParam() {

        return mResparam;

    }
}
