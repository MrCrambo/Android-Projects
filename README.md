# Android-Studio-AR-projects
 Android Studio projects with using Augmented Reality libraries
 
#### Requirements
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

## AugmentedR_1
Placing simple 3d object loaded from [Googles' poly](https://poly.google.com/) website to the plane detected near you.

Here will be image soon..

## AugmentedR_2
Here will be description and image too...
