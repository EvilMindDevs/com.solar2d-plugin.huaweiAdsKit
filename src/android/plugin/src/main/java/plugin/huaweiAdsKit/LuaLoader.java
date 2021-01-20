//
//  LuaLoader.java
//  TemplateApp
//
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

// This corresponds to the name of the Lua library,
// e.g. [Lua] require "plugin.library"
package plugin.huaweiAdsKit;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.ansca.corona.CoronaActivity;
import com.ansca.corona.CoronaEnvironment;
import com.ansca.corona.CoronaLua;
import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeListener;
import com.ansca.corona.CoronaRuntimeTask;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.RequestOptions;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.ads.reward.Reward;
import com.huawei.hms.ads.reward.RewardAd;
import com.huawei.hms.ads.reward.RewardAdLoadListener;
import com.huawei.hms.ads.reward.RewardAdStatusListener;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaType;
import com.naef.jnlua.NamedJavaFunction;


@SuppressWarnings("WeakerAccess")
public class LuaLoader implements JavaFunction, CoronaRuntimeListener {

    private int fListener;

    public static final String TAG = "Huawei Ads Kit";

    private static final String EVENT_NAME = "Huawei Ads Kit";
    private static final String VERSION = "1.0.0";

    private static final String Interstitial = "Interstitial";
    private static final String Rewarded = "Rewarded";
    private static final String Banner = "Banner";

    private static final String create = "create";
    private static final String load = "load";
    private static final String show = "show";
    private static final String hide = "hide";

    public AdParam adParam;
    private static RewardAd rewardAd;
    private static InterstitialAd interstitialAd;
    private BannerView bannerView;
    private FrameLayout layout;
    public String AdId = "";
    public BannerAdSize bannerAdSize = BannerAdSize.BANNER_SIZE_320_50;
    public int bannerAdRefresh;

    @SuppressWarnings("unused")
    public LuaLoader() {
        fListener = CoronaLua.REFNIL;
        CoronaEnvironment.addRuntimeListener(this);
    }

    private AdListener bannerAdListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            dispatchEvent(false, "Ad onAdLoaded", Banner, TAG);
        }

        @Override
        public void onAdFailed(int errorCode) {
            dispatchEvent(true, "Ad onAdFailed Error Code => " + errorCode, Banner, TAG);
        }

        @Override
        public void onAdOpened() {
            dispatchEvent(false, "Ad onAdOpened", Banner, TAG);
        }

        @Override
        public void onAdClicked() {
            dispatchEvent(false, "Ad onAdClicked", Banner, TAG);
        }

        @Override
        public void onAdLeave() {
            dispatchEvent(false, "Ad onAdLeave", Banner, TAG);
        }

        @Override
        public void onAdClosed() {
            dispatchEvent(false, "Ad onAdClosed", Banner, TAG);
        }
    };

    private AdListener interstitialAdListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            dispatchEvent(false, "Ad Loaded", Interstitial, TAG);
        }

        @Override
        public void onAdFailed(int errorCode) {
            dispatchEvent(true, "Ad onAdFailed Error Code => " + errorCode, Interstitial, TAG);
        }

        @Override
        public void onAdClosed() {
            dispatchEvent(false, "Ad onAdClosed", Interstitial, TAG);
        }

        @Override
        public void onAdClicked() {
            dispatchEvent(false, "Ad onAdClicked", Interstitial, TAG);
        }

        @Override
        public void onAdLeave() {
            dispatchEvent(false, "Ad onAdLeave", Interstitial, TAG);
        }

        @Override
        public void onAdOpened() {
            dispatchEvent(false, "Ad onAdOpened", Interstitial, TAG);
        }

        @Override
        public void onAdImpression() {
            dispatchEvent(false, "Ad onAdImpression", Interstitial, TAG);
        }
    };

    private RewardAdLoadListener rewarededAdListener = new RewardAdLoadListener() {
        @Override
        public void onRewardedLoaded() {
            dispatchEvent(false, "Ad onRewardedLoaded", Rewarded, TAG);
        }

        @Override
        public void onRewardAdFailedToLoad(int errorCode) {
            dispatchEvent(true, "Ad onRewardAdFailedToLoad Error Code => " + errorCode, Rewarded, TAG);
        }
    };

    @Override
    public int invoke(LuaState L) {
        // Register this plugin into Lua with the following functions.
        NamedJavaFunction[] luaFunctions = new NamedJavaFunction[]{
                new init(),
                new create(),
                new load(),
                new show(),
                new hide(),
        };
        String libName = L.toString(1);
        L.register(libName, luaFunctions);

        // Returning 1 indicates that the Lua require() function will return the above Lua library.
        return 1;
    }

    @SuppressWarnings("unused")
    public void dispatchEvent(final Boolean isError, final String message, final String type, final String provider) {
        CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
            @Override
            public void executeUsing(CoronaRuntime runtime) {
                LuaState L = runtime.getLuaState();

                CoronaLua.newEvent(L, EVENT_NAME);

                L.pushString(message);
                L.setField(-2, "message");

                L.pushBoolean(isError);
                L.setField(-2, "isError");

                L.pushString(type);
                L.setField(-2, "type");

                L.pushString(provider);
                L.setField(-2, "provider");

                try {
                    CoronaLua.dispatchEvent(L, fListener, 0);
                } catch (Exception ignored) {
                }
            }
        });
    }

    @SuppressWarnings("unused")
    private class init implements NamedJavaFunction {
        @Override
        public String getName() {
            return "init";
        }

        @Override
        public int invoke(LuaState luaState) {
            int listenerIndex = 1;

            if (CoronaLua.isListener(luaState, listenerIndex, EVENT_NAME)) {
                fListener = CoronaLua.newRef(luaState, listenerIndex);
            }

            HwAds.init(CoronaEnvironment.getApplicationContext());
            adParam = new AdParam.Builder().build();

            if (luaState.isTable(2)) {
                RequestOptions.Builder requestOptions = HwAds.getRequestOptions().toBuilder();

                for (luaState.pushNil(); luaState.next(2); luaState.pop(1)) {
                    String key = luaState.toString(-2);
                    switch (key) {
                        case "setTagForChildProtection":
                            requestOptions.setTagForChildProtection(luaState.toInteger(-1));
                            break;
                        case "setTagForUnderAgeOfPromise":
                            requestOptions.setTagForUnderAgeOfPromise(luaState.toInteger(-1));
                            break;
                        case "setAdContentClassification":
                            requestOptions.setAdContentClassification(luaState.toString(-1));
                            break;
                        case "setNonPersonalizedAd":
                            requestOptions.setNonPersonalizedAd(luaState.toInteger(-1));
                            break;
                    }
                }
                HwAds.setRequestOptions(requestOptions.build());
            }
            return 0;
        }
    }

    @SuppressWarnings("unused")
    private class create implements NamedJavaFunction {
        @Override
        public String getName() {
            return create;
        }

        @Override
        public int invoke(LuaState luaState) {
            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            String type = "";
            if (luaState.type(1) == LuaType.STRING) {
                type = luaState.toString(1);
            } else {
                dispatchEvent(true, "create (String, Table) expected, got " + luaState.typeName(1)
                        + "," + luaState.typeName(2), create, TAG);
                return 0;
            }

            if (luaState.isTable(2)) {
                for (luaState.pushNil(); luaState.next(2); luaState.pop(1)) {
                    String key = luaState.toString(-2);
                    switch (key) {
                        case "AdId":
                            AdId = luaState.toString(-1);
                            break;
                        case "BannerSize":
                            switch (luaState.toString(-1)) {
                                case "360x57":
                                    bannerAdSize = BannerAdSize.BANNER_SIZE_360_57;
                                    break;
                                case "360x144":
                                    bannerAdSize = BannerAdSize.BANNER_SIZE_360_144;
                                    break;
                                case "160x600":
                                    bannerAdSize = BannerAdSize.BANNER_SIZE_160_600;
                                    break;
                                case "300x250":
                                    bannerAdSize = BannerAdSize.BANNER_SIZE_300_250;
                                    break;
                                case "320x100":
                                    bannerAdSize = BannerAdSize.BANNER_SIZE_320_100;
                                    break;
                                case "320x50":
                                    bannerAdSize = BannerAdSize.BANNER_SIZE_320_50;
                                    break;
                                case "468x60":
                                    bannerAdSize = BannerAdSize.BANNER_SIZE_468_60;
                                    break;
                                case "728x90":
                                    bannerAdSize = BannerAdSize.BANNER_SIZE_728_90;
                                    break;
                                case "fluid":
                                    bannerAdSize = BannerAdSize.BANNER_SIZE_DYNAMIC;
                                    break;
                                case "invalid":
                                    bannerAdSize = BannerAdSize.BANNER_SIZE_INVALID;
                                    break;
                                case "smart_banner":
                                    bannerAdSize = BannerAdSize.BANNER_SIZE_SMART;
                                    break;
                            }
                            break;
                        case "BannerRefresh":
                            if (luaState.toInteger(-1) > 120 || luaState.toInteger(-1) < 30) {
                                dispatchEvent(true, "Refresh interval, in seconds. The value ranges from 30 to 120.", Banner, TAG);
                            } else {
                                bannerAdRefresh = luaState.toInteger(-1);
                            }
                            break;
                    }
                }
            } else {
                dispatchEvent(true, "create (String, Table) expected, got " + luaState.typeName(1)
                        + "," + luaState.typeName(2), create, TAG);
                return 0;
            }

            switch (type) {
                case "Banner":
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
                            if (activity == null) {
                                return;
                            }

                            if (layout == null) {
                                layout = new FrameLayout(activity);
                                FrameLayout.LayoutParams layoutParams =
                                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                                activity.getOverlayView().addView(layout, layoutParams);
                            }

                            if (bannerView == null) {
                                bannerView = new BannerView(CoronaEnvironment.getCoronaActivity());

                                bannerView.setAdId(AdId);
                                bannerView.setBannerAdSize(bannerAdSize);
                                bannerView.setAdListener(bannerAdListener);
                                if (bannerAdRefresh < 121 || bannerAdRefresh > 29){
                                    bannerView.setBannerRefresh(bannerAdRefresh);
                                }
                                layout.addView(bannerView);
                                dispatchEvent(false, "Bannar ad was created.", create, TAG);

                            }
                        }
                    });
                    break;
                case "Interstitial":
                    interstitialAd = new InterstitialAd(CoronaEnvironment.getApplicationContext());
                    interstitialAd.setAdId(AdId);
                    interstitialAd.setAdListener(interstitialAdListener);
                    dispatchEvent(false, "Interstitial Ad was created.", create, TAG);

                    break;
                case "Rewarded":
                    rewardAd = new RewardAd(CoronaEnvironment.getCoronaActivity(), AdId);
                    dispatchEvent(false, "Rewarded Ad was created.", create, TAG);

                    break;
            }
            return 0;
        }
    }

    @SuppressWarnings("unused")
    public class load implements NamedJavaFunction {

        @Override
        public String getName() {
            return load;
        }

        @Override
        public int invoke(LuaState luaState) {
            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            String type = "";
            if (luaState.type(1) == LuaType.STRING) {
                type = luaState.toString(1);
            } else {
                dispatchEvent(true, "load (String) expected, got " + luaState.typeName(1),
                        load, TAG);
                return 0;
            }

            switch (type) {
                case "Banner":
                    if (bannerView == null) {
                        dispatchEvent(true, "First you need to create Banner ad ", Banner, TAG);
                        return 0;
                    }
                    bannerView.loadAd(adParam);
                    break;
                case "Interstitial":
                    if (interstitialAd == null) {
                        dispatchEvent(true, "First you need to create Interstitial ad ", Interstitial, TAG);
                        return 0;
                    }
                    interstitialAd.loadAd(adParam);
                    break;
                case "Rewarded":
                    if (rewardAd == null) {
                        dispatchEvent(true, "First you need to create RewardAd ad ", Rewarded, TAG);
                        return 0;
                    }
                    rewardAd.loadAd(adParam, rewarededAdListener);
                    break;
            }

            return 0;
        }
    }

    @SuppressWarnings("unused")
    public class show implements NamedJavaFunction {

        @Override
        public String getName() {
            return show;
        }

        @Override
        public int invoke(LuaState luaState) {
            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            String adType = "";
            if (luaState.type(1) == LuaType.STRING) {
                adType = luaState.toString(1);
            } else {
                dispatchEvent(true, "show (String) expected, got " + luaState.typeName(1),
                        show, TAG);
                return 0;
            }

            switch (adType) {
                case "Banner":
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
                            if (activity == null) {
                                return;
                            }
                            if (bannerView == null) {
                                dispatchEvent(true, "First you need to create Banner ad ", Banner, TAG);
                            } else {
                                bannerView.resume();
                                bannerView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    break;

                case "Interstitial":
                    if (interstitialAd != null && interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    } else {
                        dispatchEvent(true, "interstitialAd is not loaded", Interstitial, TAG);
                    }
                    break;

                case "Rewarded":
                    if (rewardAd != null && rewardAd.isLoaded()) {
                        rewardAd.show(activity, new RewardAdStatusListener() {
                            @Override
                            public void onRewardAdClosed() {
                                dispatchEvent(false, "onRewardAdClosed", Rewarded, TAG);
                            }

                            @Override
                            public void onRewardAdFailedToShow(int errorCode) {
                                dispatchEvent(true, "onRewardAdFailedToShow Error Code is " + errorCode, Rewarded, TAG);
                            }

                            @Override
                            public void onRewardAdOpened() {
                                dispatchEvent(false, "onRewardAdOpened ", Rewarded, TAG);
                            }

                            @Override
                            public void onRewarded(Reward reward) {
                                dispatchEvent(false, "onRewarded ", Rewarded, TAG);
                            }
                        });
                    } else {
                        dispatchEvent(true, "rewardAd is not loaded", Rewarded, TAG);
                    }
                    break;
            }

            return 0;
        }
    }

    @SuppressWarnings("unused")
    public class hide implements NamedJavaFunction {

        @Override
        public String getName() {
            return hide;
        }

        @Override
        public int invoke(LuaState luaState) {
            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            String adType = "";
            if (luaState.type(1) == LuaType.STRING) {
                adType = luaState.toString(1);
            } else {
                dispatchEvent(true, "hide (String) expected, got " + luaState.typeName(1),
                        hide, TAG);
                return 0;
            }

            if (Banner.equals(adType)) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
                        if (activity == null) {
                            return;
                        }
                        if (bannerView == null) {
                            dispatchEvent(true, "First you need to create Banner ad ", Banner, TAG);
                        } else {
                            bannerView.pause();
                            bannerView.setVisibility(View.GONE);
                        }
                    }
                });
            }
            return 0;
        }
    }

    @Override
    public void onLoaded(CoronaRuntime runtime) {
    }

    @Override
    public void onStarted(CoronaRuntime runtime) {
        Log.i(EVENT_NAME, "Started v" + VERSION);
    }


    @Override
    public void onSuspended(CoronaRuntime runtime) {
    }


    @Override
    public void onResumed(CoronaRuntime runtime) {
    }

    @Override
    public void onExiting(CoronaRuntime runtime) {
        // Remove the Lua listener reference.
        CoronaLua.deleteRef(runtime.getLuaState(), fListener);
        fListener = CoronaLua.REFNIL;
    }

}
