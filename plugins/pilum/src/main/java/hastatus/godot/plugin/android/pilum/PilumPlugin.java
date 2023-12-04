package hastatus.godot.plugin.android.pilum;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import hastatus.godot.plugin.android.pilum.signals.PilumSignals;

public class PilumPlugin extends GodotPlugin {


  private FirebaseAnalytics firebaseAnalytics;
  private InterstitialAd mInterstitialAd;

  private ConsentInformation consentInformation;
  private View testContainer;
  private boolean admobLoaded;

  private AtomicBoolean admobInitialized;


  public PilumPlugin(Godot godot) {
    super(godot);
  }


  @Override
  @NonNull
  public String getPluginName() {
    return "Pilum";
  }

  @NonNull
  @Override
  public Set<SignalInfo> getPluginSignals() {
    Set<SignalInfo> signals = new HashSet<>();

    signals.add(new SignalInfo(PilumSignals.SIGNAL_ADMOB_INIT_COMPLETE));
    signals.add(new SignalInfo(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_LOADED));
    signals.add(new SignalInfo(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_LOAD, Integer.class, String.class, String.class));
    signals.add(new SignalInfo(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_SHOW));

    signals.add(new SignalInfo(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_CLICKED));
    signals.add(new SignalInfo(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_DISMISSED_FULLSCREEN_CONTENT));
    signals.add(new SignalInfo(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_FAILED_TO_SHOW_FULLSCREEN_CONTENT));
    signals.add(new SignalInfo(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_IMPRESSION));
    signals.add(new SignalInfo(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_SHOWED_FULLSCREEN_CONTENT));

    signals.add(new SignalInfo(PilumSignals.SIGNAL_ADMOB_ERROR_GDPR_CONSENT, Integer.class, String.class));

    return signals;
  }

  @Override
  public View onMainCreate(Activity activity) {
    firebaseAnalytics = FirebaseAnalytics.getInstance(activity);

    View view = activity.getLayoutInflater().inflate(R.layout.hello_world_view, null);
    testContainer = view.findViewById(R.id.hello_world_container);
    return view;
  }


  @UsedByGodot
  public void loadAds(boolean testMode, String testDeviceId) {

    consentInformation = UserMessagingPlatform.getConsentInformation(getActivity());

    consentGDPR(getActivity(), testMode, testDeviceId);

    // Check if you can initialize the Google Mobile Ads SDK in parallel
    // while checking for new consent information. Consent obtained in
    // the previous session can be used to request ads.
    if (consentInformation.canRequestAds()) {
      loadAdmobAds(testMode, testDeviceId);
    }
  }

  private void loadAdmobAds(boolean testMode, String testDeviceId){
    if(admobInitialized.compareAndSet(false, true)) {

      if (testMode) {
        List<String> testDeviceIds = Collections.singletonList(testDeviceId);
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        runOnUiThread(()->Toast.makeText(getActivity(), "Test mode ENABLE!", Toast.LENGTH_LONG).show());

      }

      MobileAds.initialize(getActivity(), initializationStatus -> {
        emitSignal(PilumSignals.SIGNAL_ADMOB_INIT_COMPLETE);
        admobLoaded = true;
      });
    }
  }


  /**
   * Show/hide, print and return "Hello World".
   */
  @UsedByGodot
  public String testPlugin(boolean visible) {
    if (testContainer != null) {
      testContainer.post(() -> {
        if(visible) {
          testContainer.setVisibility(View.VISIBLE);
        }
        else {
          testContainer.setVisibility(View.GONE);
        }

      });
    }

    return getPluginName();
  }













  @UsedByGodot
  private void loadAdIntersticial(String adUnitId) {
    AdRequest adRequest = new AdRequest.Builder().build();

    getActivity().runOnUiThread(()->
            InterstitialAd.load(getActivity(), adUnitId, adRequest,
            new InterstitialAdLoadCallback() {
              @Override
              public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                emitSignal(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_LOADED);
              }

              @Override
              public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
                emitSignal(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_LOAD, loadAdError.getCode(), loadAdError.getCause(), loadAdError.getMessage());

              }
            }));

    emitSignal(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_LOAD, -42, "Admob disabled", "Pilum is not using Admob yet");


  }


  @UsedByGodot
  private boolean isAdmobLoaded() {
    return this.admobLoaded;

  }
  @UsedByGodot
  private void showAdIntersticialLoaded() {

    if (mInterstitialAd != null) {
      mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
        @Override
        public void onAdClicked() {
          emitSignal(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_CLICKED);
          // Called when a click is recorded for an ad.

        }

        @Override
        public void onAdDismissedFullScreenContent() {
          // Called when ad is dismissed.
          // Set the ad reference to null so you don't show the ad a second time.
          emitSignal(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_DISMISSED_FULLSCREEN_CONTENT);
          mInterstitialAd = null;
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
          // Called when ad fails to show.
          emitSignal(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_FAILED_TO_SHOW_FULLSCREEN_CONTENT, adError.getCode(), adError.getCause(), adError.getMessage());
          mInterstitialAd = null;
        }

        @Override
        public void onAdImpression() {
          // Called when an impression is recorded for an ad.
          emitSignal(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_IMPRESSION);
        }

        @Override
        public void onAdShowedFullScreenContent() {
          // Called when ad is shown.
          emitSignal(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_SHOWED_FULLSCREEN_CONTENT);
        }
      });

      final Activity activity = getActivity();
      if(activity!=null && !activity.isFinishing()) {
        getActivity().runOnUiThread(() -> mInterstitialAd.show(getActivity()));
      }


    } else {
      emitSignal(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_SHOW);
    }
    emitSignal(PilumSignals.SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_SHOW);
  }




  @UsedByGodot
  private void registerEvent(String eventName, org.godotengine.godot.Dictionary params) {
    Bundle bundle = new Bundle();

    if(params!=null) {
      Set<Map.Entry<String, Object>> entrySet = params.entrySet();
      for (Map.Entry<String, Object> entry : entrySet) {
        if(entry.getValue() instanceof String) {
          bundle.putString(entry.getKey(), (String)entry.getValue());
        }
        else if(entry.getValue() instanceof Integer) {
          bundle.putInt(entry.getKey(), (Integer)entry.getValue());
        }
        else if(entry.getValue() instanceof Boolean) {
          bundle.putBoolean(entry.getKey(), (Boolean) entry.getValue());
        }
        else if(entry.getValue() instanceof Float) {
          bundle.putFloat(entry.getKey(), (Float) entry.getValue());
        }
        else if(entry.getValue() instanceof Double) {
          bundle.putDouble(entry.getKey(), (Double) entry.getValue());
        }
        else if(entry.getValue() instanceof Long) {
          bundle.putLong(entry.getKey(), (Long) entry.getValue());
        }
      }

    }

    Log.d("PILUM_EVENTS", "bundle size:"+bundle.size());

    firebaseAnalytics.logEvent(eventName, bundle);
  }




  private void consentGDPR(final Activity activity, boolean testMode, final String testDeviceId){

    ConsentRequestParameters params;
    if (testMode) {

      consentInformation.reset(); //reset to always ask

      //force geography area to GDPR
      ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(activity)
              .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
              .addTestDeviceHashedId(testDeviceId)
              .build();

      params = new ConsentRequestParameters
              .Builder()

              .setConsentDebugSettings(debugSettings)
              .build();
    }
    else {
      // Set tag for under age of consent. false means users are not under age
      // of consent.
      params = new ConsentRequestParameters
              .Builder()
              .setTagForUnderAgeOfConsent(false)
              .build();
    }


    consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            () -> UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity,
                    loadAndShowError -> {
                      if (loadAndShowError != null) {
                        // Consent gathering failed.
                        errorGDPRUserConsent(loadAndShowError);
                      }

                      // Consent has been gathered.
                      if (consentInformation.canRequestAds()) {
                        loadAdmobAds(testMode, testDeviceId);
                      }
                    }
            ),
            requestConsentError -> {
              // Consent gathering failed.
              errorGDPRUserConsent( requestConsentError);
            });


  }


  private void errorGDPRUserConsent(FormError formError) {
    Log.w("CONSENT_GDRP", String.format("%s: %s",
            formError.getErrorCode(),
            formError.getMessage()));

    emitSignal(PilumSignals.SIGNAL_ADMOB_ERROR_GDPR_CONSENT, formError.getErrorCode(), formError.getMessage());

  }
}
