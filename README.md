# Teliver

Teliver is your one place stop for all GPS Based Location tracking solutions. With simplified integrations for iOS and Android, Teliver synchronizes with applications that require location tracking. Advanced options with the inclusion of Custom markers, Multiple Operator Tracking and Push notifications to enhance user satisfaction and business productivity are accomplished.

Live locality shares are now taken a step forward with Teliver. Real time activity stream for On-Demand applications are built on certain crucial qualities:

Accuracy: the base quality being accuracy, Teliver strives in delivering the best of results.
Multi Business Solutions: Real-Time solutions for business requiring it is delivered with precision.
Advanced Customization: With micro managerial possibilities, the opportunity to customize is practically infinite.
Create your teliver account today : https://app.teliver.io.

# Configuration

To begin with – The configuration steps

1. Open your build.gradle file of Module:app.  
   Add `compile 'com.teliver.sdk:TeliverSdk:1.0.18'`as dependency.

2. Obtain the map key from Google maps [page](https://developers.google.com/maps/documentation/android-api/).

3. Open your AndroidManifest.xml file and paste the following code under application tag after embedding your map key obtained from Google.

```markdown
<meta-data
android:name="com.google.android.geo.API_KEY"
android:value="API_KEY_FOR_MAP"/>
```

> Note: You can skip steps 2 and 3 if you have already got map key and added it in manifest or you just want the location updates alone.

##### 

# Integration

##### Let’s see the magical spells now!!

* Initiate our SDK by adding the following code snippet in your Application class

```java
Teliver.init(this,"TELIVER_KEY");
```

> Note: Obtain the Teliver key from the dashboard, Use `TLog.setVisible(true);`to enable logging for development.

* Next, setup the transmission  for the operator app for whom the location has to be tracked.

```java
Teliver.startTrip(new TripBuilder("Tracking_Id").build());
```

> Note: The Tracking\_Id here is your unique identifier for the trip; basically it’s just the order id or driver id in your system

* Since our operator app is ready for transmission, we will now setup our consumer side to locate on map.

```java
Teliver.startTracking(new TrackingBuilder(new MarkerOption("Tracking_Id")).build());
```

> Note: The Tracking\_Id here is same as the id you given in previous step of operator start trip.

![](https://s3.amazonaws.com/teliverbucket/docs/android.gif)

Ref: The above view will appear on calling startTracking

**Yay!! That’s all... Now you can track an Operator.**

* ###### Stop Trip

```java
Teliver.stopTrip("Tracking_Id");
```

> Call this method with the tracking id to stop the trip on Operator side.

---

* ###### Stop Tracking

```java
Teliver.stopTracking("Tracking_Id");
```

> Call this method to stop tracking of Operator from Consumer side.

---








