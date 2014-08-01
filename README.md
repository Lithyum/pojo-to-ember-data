# POJO2EmberData : POJOS To Ember Data Models #

## What does it do ? ##

This tool converts Java model classes to their Ember Data equivalent.
Here is an example :

**POJO model**

```java
public class User {

    private String firstName;

    private String lastName;

    private User manager;

    private String email;

    private Date birthDate;

    private Boolean active;

    @JsonIgnore
    private String password;
    
    @JsonBackReference
    private Group group;
    
}
```

**Ember Data model**

```javascript
App.User = DS.Model.extend({
	firstName: DS.attr('string'),
	lastName: DS.attr('string'),
	manager: DS.belongsTo('user'),
	email: DS.attr('string'),
	birthDate: DS.attr('date'),
	active: DS.attr('boolean')
});
```

As you can see in the example, Jackson Annotations are supported out of the box.

## Installation ##

* Clone this repository: `git clone https://github.com/worldline/pojo-to-ember-data.git`
* Build with Gradle: `./gradlew build`
* Drop the generated JAR located in *build\libs* inside your project classpath.

## Usage ##

Here's a basic usage example :

```java
String[] packagesToScan = new String[] { "org.yourcompany.yourproject" };
String emberModelNamespace = "App";
Config config = new Config(PACKAGES_TO_SCAN, EMBER_MODEL_NAMESPACE);
POJO2EmberData converter = new POJO2EmberData(config);
converter.convert(); // Your models will be generated in the generated_models dir
```

Check on the test classes for more detailed examples.