class_name PilumPlugin extends Object

## Interface used to access the functionality provided by this plugin

var _pilum_singleton

func _init():
	if Engine.has_singleton("Pilum"):
		_pilum_singleton = Engine.get_singleton("Pilum")
	else:
		printerr("Initialization error: unable to access the java logic")

## Toggle between showing and hiding the hello world text
func testPlugin():
	if _pilum_singleton:
		_pilum_singleton.testPlugin()
	else:
		printerr("Initialization error")


func registerForFirebase(firebaseError: Callable, admobInit: Callable)-> bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.connect("FirebaseAnalyticsError", firebaseError)
		_pilum_singleton.connect("AdmobInicializationComplete", admobInit)
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

func registerEventStartMatch(dificuldade:String, cenario:String)->bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.registerEventStartBasicMatch(dificuldade, cenario)
	else:
		printerr("Unable to registerEventStartBasicMatch")
		success = false

	return success



func registerEventLoseBasicMatch(dificuldade:String, cenario:String, pontosJogador:int, pontosAdversario:int, acertosBolinha:int, errosBolinha:int)->bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.registerEventLoseBasicMatch(dificuldade, cenario, pontosJogador, pontosAdversario, acertosBolinha, errosBolinha)
	else:
		printerr("Unable to registerEventLoseBasicMatch")
		success = false

	return success


func registerEventAbandonBasicMatch(dificuldade:String, cenario:String, pontosJogador:int, pontosAdversario:int, acertosBolinha:int, errosBolinha:int)->bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.registerEventAbandonBasicMatch(dificuldade, cenario, pontosJogador, pontosAdversario, acertosBolinha, errosBolinha)
	else:
		printerr("Unable to registerEventAbandonBasicMatch")
		success = false

	return success

func registerEventWinBasicMatch(dificuldade:String, cenario:String, pontosJogador:int, pontosAdversario:int, acertosBolinha:int, errosBolinha:int)->bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.registerEventWinBasicMatch(dificuldade, cenario, pontosJogador, pontosAdversario, acertosBolinha, errosBolinha)
	else:
		printerr("Unable to registerEventWinBasicMatch")
		success = false

	return success


func registerEvent(event_name:String, params_string:Dictionary, params_ints:Dictionary)->bool:
	var success = true;
	if _pilum_singleton:
		_pilum_singleton.registerEvent(event_name, params_string, params_ints)
	else:
		printerr("Unable to registerEvent")
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