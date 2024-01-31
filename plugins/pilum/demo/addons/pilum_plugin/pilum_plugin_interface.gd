class_name PilumPlugin extends Object

## Interface used to access the functionality provided by this plugin

var _pilum_singleton

func _init():
	if Engine.has_singleton("Pilum"):
		_pilum_singleton = Engine.get_singleton("Pilum")
	else:
		printerr("Initialization error: unable to access the java logic")

## Toggle between showing and hiding the hello world text
func testPlugin(visible:bool)->void:
	if _pilum_singleton:
		_pilum_singleton.testPlugin(visible)
	else:
		printerr("Initialization error")


func loadNativeTools(test_mode:bool, test_device_id:String)->bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.loadNativeTools(test_mode, test_device_id)
	else:
		printerr("loadAdAds error")
		success = false

	return success

func deleteAllUserData()->bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.deleteFirebaseUserData()
		success = _pilum_singleton.clearAppData()
	else:
		printerr("deleteAllUserData error")
		success = false
	return success


func registerForFirebase(firebaseError: Callable, admobInit: Callable, gdprConsentError: Callable)-> bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.connect("FirebaseAnalyticsError", firebaseError)
		_pilum_singleton.connect("AdmobInicializationComplete", admobInit)
		_pilum_singleton.connect("AdmobErrorGdprConsent", gdprConsentError)
	else:
		printerr("Unable to register for registerForFirebase")
		success = false

	return success

func isAdmobLoaded()->bool:
	var loaded = true;
	if _pilum_singleton:
			loaded = _pilum_singleton.isAdmobLoaded()
	else:
		printerr("Unable to register for isAdmobLoaded")
		loaded = false

	return loaded

func registerForInterstitial(admobInterstitialLoaded: Callable, admobInterstitialFailedToLoad: Callable)-> bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.connect("AdmobInterstitialLoaded", admobInterstitialLoaded)
		_pilum_singleton.connect("AdmobInterstitialFailToLoad", admobInterstitialFailedToLoad)
	else:
		printerr("Unable to register for registerForInterstitial")
	success = false

	return success



func registerForRewarded(admobRewardedLoaded: Callable, admobRewardedFailedToLoad: Callable, admobReward: Callable)-> bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.connect("AdmobRewardedLoaded", admobRewardedLoaded)
		_pilum_singleton.connect("AdmobRewardedFailToLoad", admobRewardedFailedToLoad)
		_pilum_singleton.connect("AdmobRewarded", admobReward)
	else:
		printerr("Unable to register for registerForRewarded")
	success = false

	return success




func registerEvent(event_name:String, params:Dictionary)->bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.registerEvent(event_name, params)
	else:
		printerr("Unable to registerEvent")
		success = false

	return success


func loadAdRewarded(adUnitId:String)->bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.loadRewarded(adUnitId)
	else:
		printerr("Unable to loadAdRewarded")
		success = false

	return success


func showAdRewardedLoaded()->bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.showLoadedRewadedAd()
	else:
		printerr("Unable to showLoadedRewadedAd")
		success = false

	return success

func loadAdInterstitial(adUnitId:String)->bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.loadAdIntersticial(adUnitId)
	else:
		printerr("Unable to loadAdInterstitial")
		success = false

	return success

func showAdInterstitialLoaded()->bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.showAdIntersticialLoaded()
	else:
		printerr("Unable to showAdInterstitialLoaded")
		success = false

	return success

func checkInternetConnection()->bool:
	var connected = false
	if _pilum_singleton:
		connected = _pilum_singleton.isConnected()
	else:
		printerr("Unable to check internet connection")

	return connected