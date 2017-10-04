[![](https://jitpack.io/v/aistech/orp.svg)](https://jitpack.io/#aistech/orp)

Object Reference Passer
=======================

Wanna pass objects between activities, and then get it back easily. You definitely should use this.

Installation
============

In your project build.gradle

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

In your app's build.gradle

```groovy
dependencies {
  compile 'com.github.aistech:orp:{LATEST_JITPACK_VERSION}'
}
```

How to Use
==========


Both source and destination Activities must extend from `ORPActivity`. You can pass any object  to the destination activity using our Builder. See the example below:


```java
class MainActivity extends ORPActivity {

    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.source_activity);
    
        this.user = new User("Noble Six");
    
        new ORPBuilder(MainActivity.this)
            .withDestinationActivity(Main2Activity.class)
            .withObject("user", user)
            .start();
    }
    
    @Override
    public ORPActivity getInstance() {
        return this;
    }
    
    @Override
    protected void onExtrasRestored() {
        // Executed when onRestoreInstanceState() or onActivityResult() were executed
    }
}
```
One important thing, the activity must also implement getInstance in order to properly load the variables annotated.

Furthermore, to retrieve the object passed in the destination activity you just need to put the `@Extra` on the respective field. 

```java
class Main2Activity extends ORPActivity {

    @Extra("user")
    private User user;
  
    @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.destination_activity);
    
    // By changing the user name, the User in SourceActivity will already get the changes.
    this.user.setName("SPARTAN-B312");
    }
  
    @Override
    protected void onExtrasRestored() {
      // Executed when onRestoreInstanceState() or onActivityResult() were executed
    }
}
```

Parceler 
==========

This library use Parceler to save objects in activity state, so every model must include the code below:

```groovy
dependencies {
  annotationProcessor 'org.parceler:parceler:1.1.9'
}
```

Also every model has to include the annotation: 

```java
@Parcel(Parcel.Serialization.BEAN)
class User {
}
```
