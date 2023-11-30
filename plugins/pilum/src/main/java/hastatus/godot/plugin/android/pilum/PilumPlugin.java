package hastatus.godot.plugin.android.pilum;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

import java.util.HashSet;
import java.util.Set;

public class PilumPlugin extends GodotPlugin {


  private FirebaseAnalytics firebaseAnalytics;
  private InterstitialAd mInterstitialAd;
  private static final String SIGNAL_ADMOB_INIT_COMPLETE = "AdmobInicializationComplete";
  private static final String SIGNAL_ADMOB_INTERSTITIAL_LOADED = "AdmobInterstitialLoaded";
  private static final String SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_LOAD = "AdmobInterstitialFailToLoad";
  private static final String SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_SHOW = "AdmobInterstitialFailToShow";
  private static final String SIGNAL_ADMOB_INTERSTITIAL_CLICKED = "AdmobInterstitialClicked";
  private static final String SIGNAL_ADMOB_INTERSTITIAL_DISMISSED_FULLSCREEN_CONTENT = "AdmobInterstitialDismissedFullScreenContent";
  private static final String SIGNAL_ADMOB_INTERSTITIAL_FAILED_TO_SHOW_FULLSCREEN_CONTENT = "AdmobInterstitialFailedToShowFullScreenContent";
  private static final String SIGNAL_ADMOB_INTERSTITIAL_IMPRESSION = "AdmobInterstitialImpression";
  private static final String SIGNAL_ADMOB_INTERSTITIAL_SHOWED_FULLSCREEN_CONTENT = "AdmobInterstitialShowedFullScreenContent";




  private View helloWorldContainer;

  private boolean admobLoaded;
  public PilumPlugin(Godot godot) {
    super(godot);
  }

  @Override
  public String getPluginName() {
    return "Pilum";
  }

  @NonNull
  @Override
  public Set<SignalInfo> getPluginSignals() {
    Set<SignalInfo> signals = new HashSet<>();

    signals.add(new SignalInfo(SIGNAL_ADMOB_INIT_COMPLETE));
    signals.add(new SignalInfo(SIGNAL_ADMOB_INTERSTITIAL_LOADED));
    signals.add(new SignalInfo(SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_LOAD, Integer.class, String.class, String.class));
    signals.add(new SignalInfo(SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_SHOW));

    signals.add(new SignalInfo(SIGNAL_ADMOB_INTERSTITIAL_CLICKED));
    signals.add(new SignalInfo(SIGNAL_ADMOB_INTERSTITIAL_DISMISSED_FULLSCREEN_CONTENT));
    signals.add(new SignalInfo(SIGNAL_ADMOB_INTERSTITIAL_FAILED_TO_SHOW_FULLSCREEN_CONTENT));
    signals.add(new SignalInfo(SIGNAL_ADMOB_INTERSTITIAL_IMPRESSION));
    signals.add(new SignalInfo(SIGNAL_ADMOB_INTERSTITIAL_SHOWED_FULLSCREEN_CONTENT));


    return signals;
  }

  @Override
  public View onMainCreate(Activity activity) {
    firebaseAnalytics = FirebaseAnalytics.getInstance(activity);

    MobileAds.initialize(activity, new OnInitializationCompleteListener() {
      @Override
      public void onInitializationComplete(InitializationStatus initializationStatus){
        emitSignal(SIGNAL_ADMOB_INIT_COMPLETE);
        admobLoaded = true;
      }
    });

    View view = activity.getLayoutInflater().inflate(R.layout.hello_world_view, null);
    helloWorldContainer = view.findViewById(R.id.hello_world_container);
    return view;
  }

  /**
   * Show/hide, print and return "Hello World".
   */
  @UsedByGodot
  public String helloWorld() {
    if (helloWorldContainer != null) {
      helloWorldContainer.post(() -> {
        if (helloWorldContainer.getVisibility() == View.VISIBLE) {
          helloWorldContainer.setVisibility(View.GONE);
        } else {
          helloWorldContainer.setVisibility(View.VISIBLE);
        }
      });
    }

    return getPluginName();
  }













  @UsedByGodot
  private void loadAdIntersticial(String adUnitId) {
    AdRequest adRequest = new AdRequest.Builder().build();

    getActivity().runOnUiThread(()-> {
      InterstitialAd.load(getActivity(), adUnitId, adRequest,
              new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                  // The mInterstitialAd reference will be null until
                  // an ad is loaded.
                  mInterstitialAd = interstitialAd;
                  emitSignal(SIGNAL_ADMOB_INTERSTITIAL_LOADED);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                  // Handle the error
                  mInterstitialAd = null;
                  emitSignal(SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_LOAD, Integer.valueOf(loadAdError.getCode()), loadAdError.getCause(), loadAdError.getMessage());

                }
              });
    });

    emitSignal(SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_LOAD, Integer.valueOf(-42), "Admob disabled", "Pilum is not using Admob yet");


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
          emitSignal(SIGNAL_ADMOB_INTERSTITIAL_CLICKED);
          // Called when a click is recorded for an ad.

        }

        @Override
        public void onAdDismissedFullScreenContent() {
          // Called when ad is dismissed.
          // Set the ad reference to null so you don't show the ad a second time.
          emitSignal(SIGNAL_ADMOB_INTERSTITIAL_DISMISSED_FULLSCREEN_CONTENT);
          mInterstitialAd = null;
        }

        @Override
        public void onAdFailedToShowFullScreenContent(AdError adError) {
          // Called when ad fails to show.
          emitSignal(SIGNAL_ADMOB_INTERSTITIAL_FAILED_TO_SHOW_FULLSCREEN_CONTENT, adError.getCode(), adError.getCause(), adError.getMessage());
          mInterstitialAd = null;
        }

        @Override
        public void onAdImpression() {
          // Called when an impression is recorded for an ad.
          emitSignal(SIGNAL_ADMOB_INTERSTITIAL_IMPRESSION);
        }

        @Override
        public void onAdShowedFullScreenContent() {
          // Called when ad is shown.
          emitSignal(SIGNAL_ADMOB_INTERSTITIAL_SHOWED_FULLSCREEN_CONTENT);
        }
      });

      getActivity().runOnUiThread(()-> mInterstitialAd.show(getActivity()));


    } else {
      emitSignal(SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_SHOW);
    }
    emitSignal(SIGNAL_ADMOB_INTERSTITIAL_FAIL_TO_SHOW);
  }


  @UsedByGodot
  private void registerEventStartBasicMatch(String dificuldade, String cenario) {
    Bundle params = new Bundle();
    params.putString("dificuldade", dificuldade);
    params.putString("cenario", cenario);
    firebaseAnalytics.logEvent("INICIOU_PARTIDA_BASICA", params);
  }

  @UsedByGodot
  private void registerEventLoseBasicMatch(String dificuldade, String cenario, int pontosJogador, int pontosAdversario, int acertosBolinha, int tentativasAcertarBolinha) {
    Bundle params = new Bundle();
    params.putString("dificuldade", dificuldade);
    params.putString("cenario", cenario);
    params.putInt("pontos_jogador", pontosJogador);
    params.putInt("pontos_adversario", pontosAdversario);
    params.putInt("acertos_bolinha", acertosBolinha);
    params.putInt("tentativas_acertar_bolinha", tentativasAcertarBolinha);
    firebaseAnalytics.logEvent("PERDEU_PARTIDA_BASICA", params);
  }

  @UsedByGodot
  private void registerEventAbandonBasicMatch(String dificuldade, String cenario, int pontosJogador, int pontosAdversario, int acertosBolinha, int tentativasAcertarBolinha) {
    Bundle params = new Bundle();
    params.putString("dificuldade", dificuldade);
    params.putString("cenario", cenario);
    params.putInt("pontos_jogador", pontosJogador);
    params.putInt("pontos_adversario", pontosAdversario);
    params.putInt("acertos_bolinha", acertosBolinha);
    params.putInt("tentativas_acertar_bolinha", tentativasAcertarBolinha);
    firebaseAnalytics.logEvent("ABANDONOU_PARTIDA_BASICA", params);
  }

  @UsedByGodot
  private void registerEventWinBasicMatch(String dificuldade, String cenario, int pontosJogador, int pontosAdversario, int acertosBolinha, int tentativasAcertarBolinha) {
    Bundle params = new Bundle();
    params.putString("dificuldade", dificuldade);
    params.putString("cenario", cenario);
    params.putInt("pontos_jogador", pontosJogador);
    params.putInt("pontos_adversario", pontosAdversario);
    params.putInt("acertos_bolinha", acertosBolinha);
    params.putInt("tentativas_acertar_bolinha", tentativasAcertarBolinha);
    firebaseAnalytics.logEvent("GANHOU_PARTIDA_BASICA", params);
  }


}
