# Assignment 03: SOAP Web Services

In this **[assignment](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/assignments/assignment-3)** I have an application working via SOAP. I created two different Github repositories - one for server another for client. My server application should be deployed on Heroku, so anybody can call it. I generated the classes for my client using **wsimport**. 
I don't work in pairs, then I implemented my client for my own server and saved all the output in a logfile and also commited it into client repository.


## Pre-Requisites
* Lab Session 07: [Webpage](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/lab-session-7 "Permalink to LAB07: Reading and writing from Databases & JPA (Java Persistence API)")
* Lab Session 08: [Webpage](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/lab-session-8 "Permalink to LAB08: SOAP Web Services (1)")
* Lab Session 09: [Webpage](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/lab-session-9 "Permalink to LAB09: SOAP Web Services (2)")


## Project Structure
```
 Server Repository
```
* **[introsde.document.endpoint](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/endpoint) -** will contain the class PeoplePublisher.java that is the stand alone server
* **[introsde.document.dao](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/dao) -** will contain classes whose purpose will be to provide the underlying connection to the database
* **[introsde.document.model](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/model) -** will include classes that represent my domain data model and map the content in my database to objects that can be manipulated in Java. My model classes composed as follows:
```java
 public class Person implements Serializable {
    private Long idPerson;
    private String firstname;
    private String lastname;
    private Date birthdate;
    private List<Measure> currentHealth;
    private List<Measure> healthHistory;
}

public class Measure implements Serializable {
    private Long idMeasure;
    private Date dateRegistered;
    private String measureType;
    private String measureValue;
    private String valueType;
    private int isCurrent; // 1 current Health, 0 History value
}
```
* **[introsde.document.soap](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/soap) -** 
* **[introsde.document.wrapper](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/wrapper) -** will contain the wrapper used to format XML and JSON
* **[introsde.document.adapter](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/adapter) -**
* **[introsde.document.converter](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/wrapper) -**
* **[persistence.xml](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/blob/master/WebContent/META-INF/persistence.xml) -** is a file presents into folder named META-INF  
* **[build.xml](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/blob/master/build.xml) -** is an ant script which automates repetitive tasks directly from the command line.
* **[ivy.xml](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/blob/master/ivy.xml) -** is a file which can specify the dependencies 
* **[LifeCoach.sqlite](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server) -** is the database that presents an evolved data model. 
My database is based on a sqlite database as follows:
```sql
 Person (idPerson, firstname, lastname)
 Measure (idMeasure, dateRegistered, measureType, measureValue, valueType, isCurrent, idPerson)
```
* * *

```
 Client Repository
```
* **[introsde.document.client](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/client) -**
* **[introsde.document.wsimport](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/wsimport) -**
* **[client-server-xml.log](https://github.com/yuly-sanchez/introsde-2015-assignment-2/blob/master/client-server-xml.log) -** is a log file of the client calling my server own using format XML format


# 