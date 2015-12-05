# Assignment 03: SOAP Web Services

In this **[assignment](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/assignments/assignment-3)** I have an application working via SOAP. I created two different Github repositories - one for server another for client. My server application should be deployed on Heroku, so anybody can call it. I generated the classes for my client using **wsimport**. 
I don't work in pairs, then I implemented my client for my own server and saved all the output in a logfile and also commited it into client repository.


## Pre-Requisites

* Lab Session 07: [Webpage](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/lab-session-7 "Permalink to LAB07: Reading and writing from Databases & JPA (Java Persistence API)")
* Lab Session 08: [Webpage](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/lab-session-8 "Permalink to LAB08: SOAP Web Services (1)")
* Lab Session 09: [Webpage](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/lab-session-9 "Permalink to LAB09: SOAP Web Services (2)")


## Project Structure

### Server Repository
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
 CREATE TABLE Person (
    idPerson    INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    firstname   TEXT NOT NULL,
    lastname    TEXT NOT NULL,
    birthdate   DATE NOT NULL,
    UNIQUE(firstname, lastname)
);

CREATE TABLE Measure (
    idMeasure       INTEGER PRIMARY KEY AUTOINCREMENT,
    dateRegistered  DATE NOT NULL,
    measureType     TEXT NOT NULL,
    measureValue    TEXT NOT NULL,
    valueType       TEXT NOT NULL,
    isCurrent       INTEGER NOT NULL DEFAULT '1',
    idPerson        INTEGER NOT NULL,
    FOREIGN KEY(idPerson) REFERENCES Person(idPerson)
);
```

### Client Repository
* **[introsde.document.client](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/client) -**
* **[introsde.document.wsimport](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/wsimport) -**
* **[client-server-xml.log](https://github.com/yuly-sanchez/introsde-2015-assignment-2/blob/master/client-server-xml.log) -** is a log file of the client calling my server own using format XML format


## Usage
This project contains the `ant build script` to compile source code, run tests and generate documentation directly from the command line:
```
 ant execute.client
```
This target calls the following targets defined in the build file:
* `execute.client.myServer.xml` send all requests to my server with the body in XML format. This generate the ouput saved into [client-server-xml.log](https://github.com/yuly-sanchez/introsde-2015-assignment-2/blob/master/client-server-xml.log) file. 
* `execute.client.myServer.json` send all requests to my server with the body in JSON format. This generate the ouput saved into [client-server-json.log](https://github.com/yuly-sanchez/introsde-2015-assignment-2/blob/master/client-server-json.log) file. 
<DA COMPLETARE>


## Services through SOAP APIs
* **Method #1 :**  [readPersonList()](#readpersonList)
* **Method #2 :**  [readPersonList(Long id)](#get-personid) 
* **Method #3 :**  [updatePerson(Person p)](#put-personid)
* **Method #4 :**  [createPerson(Person p)](#post-person)
* **Method #5 :**  [deletePerson(Long id)](#delete-personid) 
* **Method #6 :**  [readPersonHistory(Long id, String measureType)](#get-personidmeasuretype)
* **Method #7 :**  [readMeasureTypes()](#get-personidmeasuretypemid)
* **Method #8 :**  [readPersonMeasure(Long id, String measureType, Long mid)](#post-personidmeasuretype)
* **Method #9 :**  [savePersonMeasure(Long id, Measure m)](#get-measuretypes)
* **Method #10 :** [updatePersonMeasure(Long id, Measure m)](#put-personidmeasuretypemid)


#### ReadPersonList()
Return all people saved into the database (personal info + currentHealth)
#####POST Request
```xml

<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
    soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
    <soap:Body xmlns:m="http://soap.assignment.introsde/">
        <m:readPersonList />
    </soap:Body>
</soap:Envelope>
```

#####Response status
HTTP Status: 200

**XML**
```xml

 <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
    <S:Body>
        <ns2:readPersonListResponse xmlns:ns2="http://soap.document.introsde/">
            <people>
                <person>
                    <personId>1</personId>
                    <firstname>Stefano</firstname>
                    <lastname>Pernat</lastname>
                    <birthdate>1989-07-02</birthdate>
                    <currentHealth>
                        <measure>
                            <mid>1</mid>
                            <dateRegistered>2014-08-10</dateRegistered>
                            <measureType>weight</measureType>
                            <measureValue>62.5</measureValue>
                            <measureValueType>Double</measureValueType>
                        </measure>
                        <measure>
                            <mid>2</mid>
                            <dateRegistered>2014-08-10</dateRegistered>
                            <measureType>height</measureType>
                            <measureValue>172</measureValue>
                            <measureValueType>Integer</measureValueType>
                        </measure>
                        <measure>
                            <mid>3</mid>
                            <dateRegistered>2014-11-10</dateRegistered>
                            <measureType>steps</measureType>
                            <measureValue>3000</measureValue>
                            <measureValueType>Integer</measureValueType>
                        </measure>
                    </currentHealth>
                </person>
                
                <!--more people-->
                
            </people>
        </ns2:readPersonListResponse>
    </S:Body>
</S:Envelope>
```
***