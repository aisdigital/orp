[![](https://jitpack.io/v/aistech/orp.svg)](https://jitpack.io/#aistech/orp)

Object Reference Passer
=======================

A solution to pass objects references between activities. This lib was made to avoid the use of Serialization and/or Parcel, making the objects transitions more fast, avoiding the use o `activityForResult()`, This approach is trying to copy the same behavior in iOS ViewController's transitions.

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
  compile 'com.github.aistech:orp:1.0.2'
}
```

How to Use
==========


Both source and destination Activities must extend from `ORPActivity`. You can pass any Object or Interface to the destination activity using our Builder. See the example below:


```java
class SourceActivity extends ORPActivity {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.source_activity);
    
    User user = new User("Master Chief", 117);
    
    new ORPBuilder(this)
            .withDestinationActivity(DestinationActivity.class)
            .passingObject("user", user)
            .passingObject("listener", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do your stuff
                }
            })
            .start();
    
    ...
    
    @Override
    public ORPActivity getInstance() {
        return this;
    }
  }
}
```
One important thing, the activity must also implement getInstance in order to properly load the variables annotated.

Furthermore, to retrieve the object passed in the destination activity you just need to put the `@DestinationExtraObject` on the respective field. 
Also, you must use the `super.onCreate(savedInstanceState, this)` method in order to load and fullfill the fields.


```java
class DestinationActivity extends ORPActivity {

  @DestinationExtraObject("user")
  private User user;
  
  @DestinationExtraObject("listener")
  private View.OnClickListener listener;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState, this);
    setContentView(R.layout.destination_activity);
    
    // By changing the user name, the User in SourceActivity will already get the changes.
    this.user.setName("John-117");
    
    // This will trigger the anonymous inner class instantiated in the SourceActivity
    this.listener.onClick(someView);
    
    ...
  }
}
```

Parceler 
==========

This library start to use Parceler to save objects in activity state, so every module that has model objects must include the code below:

```groovy
dependencies {
  annotationProcessor 'org.parceler:parceler:1.1.6'
}
```

Also every model has to include the annotation ```java @Parcel(Parcel.Serialization.BEAN) ```
