package com.jkstudiogroup.template;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.platform.IPlatform;
import com.ss.GMain;

import java.util.Locale;

public class AndroidLauncher extends AndroidApplication {

	FrameLayout rootView;
	AdView adView;
	InterstitialAd interstitialAd;
	RewardedVideoAd rewardedVideoAd;
	long lastInterstitialTime;
	RewardItem rewardItemReturn;
	boolean isVideoLoad = false;

	private FirebaseAnalytics mFirebaseAnalytics;
	private FirebaseRemoteConfig mFirebaseRemoteConfig;
	private static final String ADMOB_APP_ID = "ca-app-pub-9108876944724815~8160462448";
	private static final String ADMOB_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
	private static final String ADMOB_FULLSCREEN_ID = "ca-app-pub-3940256099942544/1033173712";
	private static final String ADMOB_VIDEO_ID = "ca-app-pub-3940256099942544/5224354917";

	private IPlatform.OnVideoRewardClosed videoRewardCallback = null;
	boolean bannerVisible = false;

	//--begin leaderboard
	private static final String LEADERBOARD_ID = "CgkIm7CnlaEREAIQAA";
	private GoogleSignInClient mGoogleSignInClient;
	private LeaderboardsClient mLeaderboardsClient;
	private static final int RC_LEADERBOARD_UI = 9004;
	private static final int RC_UNUSED = 5001;
	private static final int RC_SIGN_IN = 9001;
	//--end leaderboard --

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();


		IPlatform plat = new IPlatform() {

			@Override
			public void log(String str) {
				Log.i("Game", str);
			}

			@Override
			public String GetDefaultLanguage() {
				String lang = Locale.getDefault().getLanguage();
				Log.i("LANG", lang);
				return lang;
			}

			@Override
			public boolean isVideoRewardReady() {
				return isVideoLoad;
			}

			@Override
			public void ShowVideoReward(OnVideoRewardClosed callback) {
				videoRewardCallback = callback;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try{
							if (rewardedVideoAd.isLoaded()) {
								rewardedVideoAd.show();
							}
						}catch(Exception e){}
					}
				});
			}

			@Override
			public void ShowFullscreen() {
				long fullscreenTime = GetConfigIntValue("fullscreenTime", 0);
				if(System.currentTimeMillis() - lastInterstitialTime > fullscreenTime) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (interstitialAd.isLoaded()) {
								TrackCustomEvent("fullscreenShow");
								interstitialAd.show();
								lastInterstitialTime = System.currentTimeMillis();
							}

						}
					});
				}
			}

			@Override
			public void ShowBanner(boolean visible) {
				ShowGameBanner(visible);
				bannerVisible = visible;
			}

			@Override
			public int GetConfigIntValue(String name, int defaultValue) {
				try {

					String v = GetConfigStringValue(name, "");
					if(v.equals(""))
						return defaultValue;

					return Integer.parseInt(v);
				}
				catch(Exception e){
					return defaultValue;
				}
			}

			@Override
			public String GetConfigStringValue(String name, String defaultValue) {
				try {
					String v = mFirebaseRemoteConfig.getString(name);
					Log.i("remoteConfig", "name=" + name + " v="+v);
					if (v.equals(""))
						return defaultValue;
					return v;
				}
				catch(Exception e){
					return defaultValue;
				}
			}

			@Override
			public void CrashKey(String key, String value) {
				Crashlytics.setString(key, value);
			}

			@Override
			public void CrashLog(String log) {
				Crashlytics.log(log);
			}

			@Override
			public void TrackCustomEvent(String event) {
				try{
					Bundle bundle = new Bundle();
					mFirebaseAnalytics.logEvent(event, bundle);
				}catch(Exception e){}
			}

			@Override
			public void TrackLevelInfo(String event, int mode, int difficult, int level) {
				try{
					Bundle bundle = new Bundle();
					bundle.putInt("mode", mode);
					bundle.putInt("difficult", difficult);
					bundle.putInt("level", level);
					mFirebaseAnalytics.logEvent(event, bundle);

					Bundle bundle2 = new Bundle();
					mFirebaseAnalytics.logEvent(event+""+mode+""+difficult+"_"+level, bundle2);


				}catch(Exception e){}
			}

			@Override
			public void TrackPlayerInfo(String event, int mode, int difficult, int level) {
				try{
					Bundle bundle = new Bundle();
					bundle.putInt("mode", mode);
					bundle.putInt("difficult", difficult);
					bundle.putInt("level", level);
					mFirebaseAnalytics.logEvent(event, bundle);
				}catch(Exception e){}
			}

			@Override
			public void TrackPlaneInfo(String event, int planeid, int level) {
				try{
					Bundle bundle = new Bundle();
					bundle.putInt("planeid", planeid);
					bundle.putInt("level", level);
					mFirebaseAnalytics.logEvent(event, bundle);
				}catch(Exception e){}
			}

			@Override
			public void TrackVideoReward(String type) {
				try{
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					mFirebaseAnalytics.logEvent("VideoReward", bundle);
				}catch(Exception e){}

			}

			@Override
			public void TrackPlayerDead(String event, int mode, int difficult, int level, int parentModel, int shooterModel, boolean isBoss) {

			}

			@Override
			public void ReportScore(long score) {
				ReportGameScore(LEADERBOARD_ID, score);
			}

			@Override
			public void ShowLeaderboard() {
				ShowGameLeaderBoard();
			}
		};
		//initialize(new GMain(plat), config);

		View libgdxview = initializeForView(new GMain(plat));

		rootView = new FrameLayout(this);
		rootView.addView(libgdxview);
		setContentView(rootView);

		InitAd();
		InitGA();
		InitLeaderboard();

		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				InitRemoteConfig();
			}
		});
	}

	public void InitRemoteConfig(){
		try {
			mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
			FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
							///.setDeveloperModeEnabled(BuildConfig.DEBUG)
							.build();
			mFirebaseRemoteConfig.setConfigSettings(configSettings);
			mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
			int cacheExpiration = 1;
			mFirebaseRemoteConfig.fetch(cacheExpiration)
							.addOnCompleteListener(this, new OnCompleteListener<Void>() {
								@Override
								public void onComplete(Task<Void> task) {
									if (task.isSuccessful()) {
										Log.i("remoteconfig", "ok");
										mFirebaseRemoteConfig.activateFetched();
									} else {
										Log.i("remoteconfig", "false");
									}

								}
							});
		}
		catch(Exception e){

		}
	}

	public void InitAd() {
		MobileAds.initialize(this, ADMOB_APP_ID);

		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					InitVideoReward();
					InitBanner();
					InitInterstitial();
				}catch(Exception e){}
			}
		});
	}

	void InitVideoReward(){
		rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
		rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
			@Override
			public void onRewardedVideoAdLoaded() {
				Log.i("VIDEO", "onRewardedVideoAdLoaded");
				isVideoLoad = true;
			}

			@Override
			public void onRewardedVideoAdOpened() {
				Log.i("VIDEO", "onRewardedVideoAdOpened");
			}

			@Override
			public void onRewardedVideoStarted() {
				Log.i("VIDEO", "onRewardedVideoStarted");
			}

			@Override
			public void onRewardedVideoAdClosed() {
				Log.i("VIDEO", "onRewardedVideoAdClosed");

				if(videoRewardCallback!=null){
					if(rewardItemReturn != null) {
						videoRewardCallback.OnEvent(true);
						//GameAnalytics.addDesignEventWithEventId("rewardedVideo");
						//zenObj.TrackCustomEvent("videoShow");

					}
					else
						videoRewardCallback.OnEvent(false);
					videoRewardCallback = null;
				}

				isVideoLoad = false;
				LoadRewardedVideoAd();


			}

			@Override
			public void onRewarded(RewardItem rewardItem) {
				lastInterstitialTime = System.currentTimeMillis();
				rewardItemReturn = rewardItem;

				Log.i("VIDEO", "onRewarded");

			}

			@Override
			public void onRewardedVideoAdLeftApplication() {
				Log.i("VIDEO", "onRewardedVideoAdLeftApplication");
			}

			@Override
			public void onRewardedVideoAdFailedToLoad(int i) {
				Log.i("VIDEO", "onRewardedVideoAdFailedToLoad");

				isVideoLoad = false;
				LoadRewardedVideoAd();
			}

			@Override
			public void onRewardedVideoCompleted() {

			}
		});
		LoadRewardedVideoAd();
	}
	private void LoadRewardedVideoAd() {

		rewardItemReturn = null;
		AdRequest.Builder adrequest = new AdRequest.Builder();

		rewardedVideoAd.loadAd(ADMOB_VIDEO_ID,  adrequest.build());
	}


	void InitInterstitial(){
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(ADMOB_FULLSCREEN_ID);
		interstitialAd.loadAd(new AdRequest.Builder().build());
		lastInterstitialTime = System.currentTimeMillis();

		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLeftApplication() {
				interstitialAd.loadAd(new AdRequest.Builder().build());
			}

			@Override
			public void onAdClosed() {
				interstitialAd.loadAd(new AdRequest.Builder().build());
			}
		});
	}

	void InitBanner(){
		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(ADMOB_BANNER_ID);//
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
		adView.setLayoutParams(params);


		final LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.TOP);
		layout.addView(adView);



		rootView.addView(layout);


		adView.setAdListener(new AdListener(){
			@Override
			public void onAdLoaded() {
				super.onAdLoaded();
				adView.setVisibility(View.GONE);
				//adView.setVisibility(View.VISIBLE);
				//layout.addView(adView);
				//Log.d("TEST", "AdLoaded");

				ShowGameBanner(bannerVisible);

			}

			@Override
			public void onAdFailedToLoad(int i) {
				super.onAdFailedToLoad(i);
				adView.setVisibility(View.INVISIBLE);

			}
		});


		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);



	}

	void InitGA(){

		try {
			mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
			FirebaseInstanceId.getInstance().getInstanceId()
							.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
								@Override
								public void onComplete(Task<InstanceIdResult> task) {
									//Log.d("IID_TOKEN", task.getResult().getToken());
								}
							});
		}
		catch(Exception e){

		}
	}

	public void ShowGameBanner(boolean visible) {
		final boolean v = visible;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(v)
					adView.setVisibility(View.VISIBLE);
				else
					adView.setVisibility(View.GONE);
			}
		});

	}


	@Override
	protected void onResume() {
		super.onResume();
		try {
			if(rewardedVideoAd!=null)
				rewardedVideoAd.resume(this);

		}catch(Exception e){}
	}


	@Override
	public void onPause() {
		try {
			if(rewardedVideoAd!=null)
				rewardedVideoAd.pause(this);
		}catch(Exception e){}
		super.onPause();
	}

	@Override
	public void onDestroy() {
		try{
			if(rewardedVideoAd!=null)
				rewardedVideoAd.destroy(this);
		}catch(Exception e){}
		super.onDestroy();
	}

	//--begin leaderboard
	void InitLeaderboard() {
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
				.build();

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
	}


	public void ReportGameScore(String leaderboardID, long score) {
		try {
			if (isSignedIn()) {
				Games.getLeaderboardsClient(AndroidLauncher.this, GoogleSignIn.getLastSignedInAccount(AndroidLauncher.this))
						.submitScore(LEADERBOARD_ID, score);
			}
		}
		catch(Exception e){}
	}

	public void ShowGameLeaderBoard() {
		try {
			if (isSignedIn()) {
				Games.getLeaderboardsClient(AndroidLauncher.this, GoogleSignIn.getLastSignedInAccount(AndroidLauncher.this))
						.getLeaderboardIntent(LEADERBOARD_ID)
						.addOnSuccessListener(new OnSuccessListener<Intent>() {
							@Override
							public void onSuccess(Intent intent) {
								try {
									AndroidLauncher.this.startActivityForResult(intent, RC_LEADERBOARD_UI);
								}
								catch (Exception e){

								}
							}
						});
			} else {
				forceSignIn = true;
				signInSilently(new Runnable() {
					@Override
					public void run() {
						ShowGameLeaderBoard();
					}
				});
			}
		}
		catch (Exception e){}
	}

	private boolean isSignedIn() {
		return GoogleSignIn.getLastSignedInAccount(AndroidLauncher.this) != null;
	}

	boolean forceSignIn = false;

	Runnable signinAction;

	private void signInSilently(Runnable action) {
		signinAction = action;
		mGoogleSignInClient.silentSignIn().addOnCompleteListener(AndroidLauncher.this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
							Log.d("", "signInSilently(): success");
							if (signinAction != null)
								signinAction.run();

							signinAction = null;
							//onConnected(task.getResult());
						} else {
							Log.d("", "signInSilently(): failure", task.getException());
							//onDisconnected();
							if (forceSignIn) {
								startSignInIntent();
							}
						}
					}
				});
	}

	private void startSignInIntent() {
		try {
			this.startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
		}catch (Exception e){

		}

	}

	private void signOut() {
		Log.d("", "signOut()");

		if (!isSignedIn()) {
			Log.w("", "signOut() called, but was not signed in!");
			return;
		}

		mGoogleSignInClient.signOut().addOnCompleteListener(AndroidLauncher.this,
				new OnCompleteListener<Void>() {
					@Override
					public void onComplete(Task<Void> task) {
						boolean successful = task.isSuccessful();
						Log.d("", "signOut(): " + (successful ? "success" : "failed"));


					}
				});
	}
	//--end leaderboard
}
