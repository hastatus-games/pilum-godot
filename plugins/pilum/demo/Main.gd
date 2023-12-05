extends Node2D


## 1) Install Android Build Template GODOT 4.2+
## 2) Inside android/build copy and paste your google-services.json
## 3) Edit your android/build/AndroidManifest.xml AND android/build/build.gradle as described in README (github.com/hastatus-games/pilum-godot)
## 4) Build the Plugin: ./gradlew :plugins:pilum:assemble
## 5) Enable the plugin


const AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712" #SAMPLE - https://developers.google.com/admob/android/test-ads
var pilum : PilumPlugin
var _test_visible = false


func _ready():
	pilum = preload("res://addons/pilum_plugin/pilum_plugin_interface.gd").new()
	pilum.registerForFirebase(onFirebaseError, onAdmobInit, gdprConsentError)


func _on_bt_test_pressed():
	_test_visible = !_test_visible
	pilum.testPlugin(_test_visible)

func _on_bt_enable_pressed():
	
	#Add your test device programmatically
	#If you want to test ads in your app as you're developing, follow the steps below to programmatically register your test device.
	#Load your ads-integrated app and make an ad request.
	#Check the logcat output for a message that looks like the one below, which shows you your device ID and how to add it as a test device:
	#I/Ads: Use RequestConfiguration.Builder.setTestDeviceIds(Arrays.asList("33BE2250B43518CCDA7DE426D04EE231"))
	#to get test ads on this device."
	#Copy your test device ID to your clipboard.
	
	pilum.loadNativeTools(true, "PASTE_YOUR_DEVICE_ID_HERE")


func _on_bt_interstitial_pressed():
	loadAdIntersticial()


func _on_bt_event_pressed():
	# send your custom event
	#registerEvent("MY_EVENT_NAME", {
	#	"level": 1,
	#	"difficult": "EASY"		
	#})
	pass # Replace with function
	
	
	
	
	
	
func onFirebaseError()->void:
	printerr("onFirebaseError")
	
func gdprConsentError(code:int, message:String)->void:	
	printerr("gdprConsentError, code:", code, " message:", message)
	
func onAdmobInit()->void:	
	pilum.registerForInterstitial(onAdmobInterstitialLoaded, onAdmobInterstitialFailedToLoad)

	

func onAdmobInterstitialLoaded()->void:
	printerr("onAdmobInterstitialLoaded")
	showAdInterstitialLoaded()
	
func onAdmobInterstitialFailedToLoad(code:int, message:String)->void:
	printerr("onAdmobInterstitialFailedToLoad code:", code, " Message:", message)


func loadAdIntersticial()->void:		
	if pilum.isAdmobLoaded():
		print("loadAdInterstitial ")
		pilum.loadAdInterstitial(AD_UNIT_ID)
	else:
		printerr("admob not loaded!")
		
func showAdInterstitialLoaded()->void:	
	pilum.showAdInterstitialLoaded()
	
func registerEvent(event_name:String, params:Dictionary)->void:
	pilum.registerEvent(event_name, params)
	
	
