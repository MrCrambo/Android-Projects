# AR apps in Android Studio
Android Studio projects with using Augmented Reality libraries. Here will be simple apps with adding different objects to the scene, animations of 3d objects, games with shooting to the objects, face detection and masks, playing video on scenes and many other simple projects.
 
### Requirements for AR apps
Add to the `android` section in `buidl.gradle` following code after `buildTypes`.
```java
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
```

Add to the `dependencies` section in `buidl.gradle` following code after all implementations.
```java
    // Provides ARCore Session and related resources.
    implementation 'com.google.ar:core:1.11.0'

    // Provides ArFragment, and other UX resources.
    implementation 'com.google.ar.sceneform.ux:sceneform-ux:1.11.0'

    // Alternatively, use ArSceneView without the UX dependency.
    implementation 'com.google.ar.sceneform:core:1.11.0'
```

Add to the `manifest` section in `AndroidManifest.xml` permissions before `application` section and add `meta-data` inside of `application`
```java
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-feature android:name="android.hardware.camera.ar" android:required="true"/>
    ...
    <application
        ...
        <meta-data android:name="com.google.ar.core" android:value="required" />
    </application>
```


## AugmentedR_1
Placing simple 3d object loaded from [Googles' poly](https://poly.google.com/) website to the plane.
- Follow `Requirements` section
- Load `.obj` file from [Googles' poly](https://poly.google.com/)
- Create `Sample Data Directory`, copy and past your `.obj` and `.mtl` files there
- Click `Import Sceneform Asset` on your `.obj` file and your object will appear in `assets` folder
- Create `fragment` element in your activity layout
- Some magic (I mean code - **detect the plane and add your model, don't forget to do it transformable for resizing and moving**) in your activity class and it's ready!

![First App Capture](https://github.com/MrCrambo/Android-Studio-AR/blob/master/Samples/ar_1.gif)

## AugmentedR_2
Placing simple 3d objects like cube, sphere and cylinder to the plane.
- Follow `Requirements` section
- Create `fragment` element in your activity layout with buttons for each object
- Add on tap plane listener and create there selected object
- Don't forget to make it transformable for moving and resizing

![Second App Capture](https://github.com/MrCrambo/Android-Studio-AR/blob/master/Samples/ar_2.gif)

## AugmentedR_3
`ViewPager` with images on the plane
- Follow `Requirements` section
- Create `fragment` element in your activity layout and create layout with `ViewPager`
- Add on tap plane listener then create `ViewPager` with adapter and add them to the scene
- Make it NOT transformable for easy switching between images

![Third App Capture](https://github.com/MrCrambo/Android-Studio-AR/blob/master/Samples/ar_3.gif)

## AugmentedR_4
Adding fox mask to the face
- Follow `Requirements` section
- Load some mask for face with `.fbx` and import it
- Create `fragment` element in your activity layout
- Create Custom Fragment which extends `ArFragmet`
- Render it and add to the scene

##### No gifs for this app!

## AugmentedR_5
Game - Kill the flying president
- Follow `Requirements` section
- Load some 3d object file from [Googles' poly](https://poly.google.com/)
- Create `fragment` element in your activity layout, add buttons and UI elements
- Add elements to the scene in different points
- Add bullets and write shooting mechanism
- Detect collisions and remove the objects
- Add sound for bullets

![Fifth App Capture](https://github.com/MrCrambo/Android-Studio-AR/blob/master/Samples/ar_5.gif)

## Area 51 AR
As a final project I developed Augmented Reality shooter and [published it in Play Market](https://play.google.com/store/apps/details?id=com.drakosha.zone51ar).
Feel free to play it and leave your comments. Here is the instructions how to build your own shooter.
- Follow `Requirements` section
- Load some 3d objects from [Googles' poly](https://poly.google.com/) or other sites
- Create `fragment` element in your activity layout, add buttons and UI elements
- Add elements to the scene in different points in front of you and add thread for updating thier positions
- Add bullets and write shooting mechanism
- Detect collisions and remove the objects
- Add few more levels and bosses with abilities
- Add analytics, ads, music and promo codes
- Publish it!

Screenshot 1.              |  Screenshot 2.
:-------------------------:|:-------------------------:
![](https://lh3.googleusercontent.com/dP-G_2_QwEwMQrFgq-NKVfs_iONcnZiGF6IjljPxUElgIkNxId1XqofXs65wiZsJEx52=w3604-h2708)  |  ![](https://lh3.googleusercontent.com/PfOoi8WdLk84i396rG98EloXapxjk6XnCFD3z13Po9R-zDyVoFVAdwZi1Tjjp32dP5M=w3604-h2708)

## SolveIt
Simple project for solving math expressions using text detection.
- Implement `com.google.android.gms:play-services-vision` latest version
- Detect text using camera
- Add math expressions solver from string
- Add view for entering text, if user has troubles with detecting expression

<img src="https://github.com/MrCrambo/Android-Projects/blob/master/Samples/solve_it.jpeg" alt="drawing" width="400"/>
