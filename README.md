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

### Add permissions, APPLICATION_ID and collection_enabled default to FALSE adressing GDPR

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
        
        <!-- SET the collection_enabled to false adressing GDPR -->
        <meta-data android:name="firebase_analytics_collection_enabled" android:value="false" />
        <meta-data android:name="firebase_crashlytics_collection_enabled" android:value="false" />        


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
        classpath 'com.google.gms:google-services:4.3.15'                    // <==== ADD This
        classpath 'com.google.firebase:firebase-crashlytics-gradle:2.9.5'    // <==== ADD This
    }
}

plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'

}

apply from: 'config.gradle'

allprojects {
    repositories {
        google()
        mavenCentral()

        // Godot user plugins custom maven repos
        String[] mavenRepos = getGodotPluginsMavenRepos()
        if (mavenRepos != null && mavenRepos.size() > 0) {
            for (String repoUrl : mavenRepos) {
                maven {
                    url repoUrl
                }
            }
        }
    }
}

configurations {
    // Initializes a placeholder for the devImplementation dependency configuration.
    devImplementation {}
}

dependencies {
    implementation libraries.kotlinStdLib
    implementation libraries.androidxFragment

    implementation platform("com.google.firebase:firebase-bom:32.3.1")
    implementation 'com.google.firebase:firebase-crashlytics'                // <==== ADD This
    implementation "com.google.firebase:firebase-analytics"                  // <==== ADD This
    implementation "com.google.android.gms:play-services-ads:22.5.0"         // <==== ADD This
    implementation 'com.google.android.ump:user-messaging-platform:2.1.0'    // <==== ADD This

    if (rootProject.findProject(":lib")) {
...
//At the end of your build.gradle file

apply plugin: 'com.google.gms.google-services' // <==== ADD THIS to enbale Firebase
apply plugin: 'com.google.firebase.crashlytics'// <==== ADD THIS to enable Crashlytics

```

### Add you google-services.json

Inside **android/build** copy and paste your **google-services.json**



### Build the Plugin

### Building the Hello World plugin

Use the following commands to build the plugin:

```
cd pilum-godot
./gradlew :plugins:pilum:assemble
```

### Enable the plugin

Inside your Godot 4.2+ project create the folder **addons** if it doesn't exist

Copy the folder **pilum-godot/plugins/pilum/demo/addons/pilum_plugin** to your Godot4.2+ folder **addons**

Enable the plugin in Godot4.2+: **Project -> Project Settings... -> Plugins (tab)**




