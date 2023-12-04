# Pilum Godot

This is a plugin for Godot using the Version 2 (v2) architecture for Android plugins.

**Requires Godot 4.2 or above**

Integration with:
- Firebase Analytics
- Firebase Crashlytics
- Google Admob

It was built using the project [Godot-Android-Samples](https://github.com/m4gr3d/Godot-Android-Samples) as a baseline to the project


## Instalation

Inside **android/build** folder

### Add permissions

Edit your **android/build/AndroidManifest.xml**

 
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.godot.game"
    android:versionCode="1"
    android:versionName="1.0"
    android:installLocation="auto" >

    ...

    <!-- ADD this permissions -->
    
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
    <!-- END permissions -->

    ...
```
### Add Admob com.google.android.gms.ads.APPLICATION_ID
```

<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.godot.game"
    android:versionCode="1"
    android:versionName="1.0"
    android:installLocation="auto" >

...

<application
        android:label="@string/godot_project_name_string"
        android:allowBackup="false"
        android:icon="@mipmap/icon"
        android:appCategory="game"
        android:isGame="true"
        android:hasFragileUserData="false"
        android:requestLegacyExternalStorage="false"
        tools:ignore="GoogleAppIndexingWarning" >
        
        
        ...
        
        
        <meta-data android:name='com.facebook.sdk.AutoLogAppEventsEnabled' android:value='false'/>
        <meta-data android:name="com.facebook.sdk.AutoInitEnabled" android:value="false"/>
        <meta-data android:name="firebase_messaging_auto_init_enabled" android:value="false" />
        <meta-data android:name="firebase_analytics_collection_enabled" android:value="false" />
        <meta-data android:name="firebase_crashlytics_collection_enabled" android:value="false" />
        <meta-data android:name="firebase_performance_collection_enabled" android:value="false" />



        <!--  AdMob App ID. -->
        <!-- Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 -->
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/>

...
```

### Add Dependecies

Edit your **android/build/build.gradle**


```
// Gradle build config for Godot Engine's Android port.
buildscript {
    apply from: 'config.gradle'

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath libraries.androidGradlePlugin
        classpath libraries.kotlinGradlePlugin
        classpath 'com.google.gms:google-services:4.3.15' // <==== ADD THIS to enable Admob Ads
        classpath 'com.google.firebase:firebase-crashlytics-gradle:2.9.5' // <==== ADD THIS to enable Firebase Crashlytics
        classpath 'com.google.android.ump:user-messaging-platform:2.1.0' // <==== ADD THIS to enable GDPR
    }
}

...
//At the end of your build.gradle file

apply plugin: 'com.google.gms.google-services' // <==== ADD THIS to enbale Firebase
apply plugin: 'com.google.firebase.crashlytics'// <==== ADD THIS to enable Crashlytics

```

### Add you google-services.json

Inside **android/build** copy and paste your **google-services.json**




