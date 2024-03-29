# Assignment 03: SOAP Web Services

In this **[assignment](https://sites.google.com/a/unitn.it/introsde_2015-16/lab-sessions/assignments/assignment-3)** I have an application working via SOAP. I created two different Github repositories - one for server another for client. My server application should be deployed on Heroku, so anybody can call it. I generated the classes for my client using **wsimport**. 
I don't work in pairs, then I implemented my client for my own server and saved all the output in a logfile and also commited it into client repository.


## Server

* Server URL: [Webpage](https://agile-shelf-1769.herokuapp.com/)
* WSDL URL: [Webpage](https://agile-shelf-1769.herokuapp.com/soap/people?wsdl)
* Client Repository GitHub: [Webpage](https://github.com/yuly-sanchez/introsde-2015-assignment-3-client)


## Usage
This project contains the `ant build script` to compile source code, run tests and generate documentation directly from the command line. To run the server local:
```
 ant start
```


## Project Structure

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
* **[introsde.document.soap](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/soap) -** will contain SOAP Web Services
* **[introsde.document.wrapper](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/wrapper) -** will contain the wrapper used to format XML and JSON
* **[introsde.document.adapter](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/adapter) -** will contain a class used to format date Calendar
* **[introsde.document.converter](https://github.com/yuly-sanchez/introsde-2015-assignment-3-server/tree/master/src/introsde/document/converter) -** will contain a class used to format date Calendar
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


## SOAP Request
* **Method #1 :**  [readPersonList()](#readpersonlist)
* **Method #2 :**  [readPerson(Long id)](#readpersonlong-id) 
* **Method #3 :**  [updatePerson(Person p)](#updatepersonperson-p)
* **Method #4 :**  [createPerson(Person p)](#createpersonperson-p)
* **Method #5 :**  [deletePerson(Long id)](#deletepersonlong-id) 
* **Method #6 :**  [readPersonHistory(Long id, String measureType)](#readpersonhistorylong-id-string-measuretype)
* **Method #7 :**  [readMeasureTypes()](#readmeasuretypes)
* **Method #8 :**  [readPersonMeasure(Long id, String measureType, Long mid)](#readpersonmeasurelong-id-string-measuretype-long-mid)
* **Method #9 :**  [savePersonMeasure(Long id, Measure m)](#savepersonmeasurelong-id-measure-m)
* **Method #10 :** [updatePersonMeasure(Long id, Measure m, Long mid)](#updatepersonmeasurelong-id-measure-m-long-mid)


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
#### ReadPerson(Long id)
Return the person (personal info + currentHealth) of the person identified by id
#####POST Request
```xml

<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
    soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
    <soap:Body xmlns:m="http://soap.assignment.introsde/">
        <m:readPerson>
            <personId>3</personId>
        <m:readPerson/>
    </soap:Body>
</soap:Envelope>
```

#####Response status
HTTP Status: 200

**XML**
```xml

 <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
    <S:Body>
        <ns2:readPersonResponse xmlns:ns2="http://soap.document.introsde/">
            <person>
                <personId>3</personId>
                <firstname>Marco</firstname>
                <lastname>Pedrazzi</lastname>
                <birthdate>1989-12-13</birthdate>
                <currentHealth>
                    <measure>
                        <mid>7</mid>
                        <dateRegistered>2014-05-31</dateRegistered>
                        <measureType>weight</measureType>
                        <measureValue>75.7</measureValue>
                        <measureValueType>Double</measureValueType>
                    </measure>
                    <measure>
                        <mid>8</mid>
                        <dateRegistered>2014-07-05</dateRegistered>
                        <measureType>height</measureType>
                        <measureValue>178</measureValue>
                        <measureValueType>Integer</measureValueType>
                    </measure>
                    <measure>
                        <mid>9</mid>
                        <dateRegistered>2014-03-07</dateRegistered>
                        <measureType>steps</measureType>
                        <measureValue>5500</measureValue>
                        <measureValueType>Integer</measureValueType>
                    </measure>
                </currentHealth>
            </person>
        </ns2:readPersonResponse>
    </S:Body>
</S:Envelope>
```

***
#### UpdatePerson(Person p)
Update the personal information of the person identified by id (e.g., only the Person's information, not the measures of the health profile
#####PUT Request
```xml

<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
    soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
    <soap:Body xmlns:m="http://soap.assignment.introsde/">
        <m:updatePerson>
            <person>
                <personId>5</personId>
                <firstname>Guido</firstname>
                <lastname>Pugliese</lastname>
            </person>
        </m:updatePerson>
    </soap:Body>
</soap:Envelope>
```

#####Response status
HTTP Status: 200

**Before**
```xml

<person>
    <personId>5</personId>
    <firstname>Test</firstname>
    <lastname>Creation</lastname>
    <birthdate>1914-01-01</birthdate>
    <currentHealth/>
</person>
```

**After**
```xml

<person>
    <personId>5</personId>
    <firstname>Guido</firstname>
    <lastname>Pugliese</lastname>
    <birthdate>1914-01-01</birthdate>
    <currentHealth/>
</person>
```

***
#### CreatePerson(Person p)
Create a new person (only personal info, no currentHealth)
#####POST Request
```xml

<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
    soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
    <soap:Body xmlns:m="http://soap.assignment.introsde/">
        <m:createPerson>
            <person>
                <firstname>Melanie</firstname>
                <lastname>Escalante</lastname>
                <birthdate>1990-09-19</birthdate>
                <currentHealth>
                    <measure>
                        <measureType>weight</measureType>
                        <measureValue>70</measureValue>
                        <measureValueType>Double</measureValueType>
                    </measure>
                </currentHealth> 
            </person>
        </m:createPerson>
    </soap:Body>
</soap:Envelope>
```

#####Response status
HTTP Status: 200

**XML**
```xml

<person>
    <personId>106</personId>
    <firstname>Melanie</firstname>
    <lastname>Escalante</lastname>
    <birthdate>1990-09-19</birthdate>
    <currentHealth>
        <measure>
            <mid>484</mid>
            <dateRegistered>2015-12-06</dateRegistered>
            <measureType>weight</measureType>
            <measureValue>70</measureValue>
            <measureValueType>Double</measureValueType>
        </measure>
    </currentHealth>
</person>
```

***
#### DeletePerson(Long id)
Delete a person identified by {id} from the system
#####DELETE Request
```xml

<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
    soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
    <soap:Body xmlns:m="http://soap.assignment.introsde/">
        <m:deletePerson>
            <personId>106</personId>
        </m:deletePerson>
    </soap:Body>
</soap:Envelope>
```

#####Response status
HTTP Status: 200

**XML**
```xml

<message>Person with id: 106 deleted</message>
```

***
#### ReadPersonHistory(Long id, String measureType)
Return the history of a {measureType} for person identified by {id}
#####POST Request
```xml

<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
    soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
    <soap:Body xmlns:m="http://soap.assignment.introsde/">
         <m:readPersonHistory>
            <personId>1</personId>
            <measureType>weight</measureType>
         </m:readPersonHistory>
    </soap:Body>
</soap:Envelope>
```

#####Response status
HTTP Status: 200

**XML**
```xml

<healthProfile-history>
    <measure>
        <mid>13</mid>
        <dateRegistered>2014-01-03</dateRegistered>
        <measureType>weight</measureType>
        <measureValue>57.2</measureValue>
        <measureValueType>Double</measureValueType>
    </measure>
    <measure>
        <mid>14</mid>
        <dateRegistered>2013-03-30</dateRegistered>
        <measureType>weight</measureType>
        <measureValue>52.5</measureValue>
        <measureValueType>Double</measureValueType>
    </measure>
</healthProfile-history>
```

***
#### ReadMeasureTypes()
Return the list of the measures
#####POST Request
```xml

<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
    soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
    <soap:Body xmlns:m="http://soap.assignment.introsde/">
         <m:readMeasureTypes/>
    </soap:Body>
</soap:Envelope>
```

#####Response status
HTTP Status: 200

**XML**
```xml

<measureTypes>
    <measureType>height</measureType>
    <measureType>steps</measureType>
    <measureType>weight</measureType>
</measureTypes>
```

***
#### ReadPersonMeasure(Long id, String measureType, Long mid)
Return the value of {measureType} identified by {mid} for person identified by {id}
#####POST Request
```xml

<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
    soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
    <soap:Body xmlns:m="http://soap.assignment.introsde/">
        <m:readPersonMeasure>
            <personId>1</personId>
            <measureType>weight</measureType>
            <mid>13</mid>
        </m:readPersonMeasure>
    </soap:Body>
</soap:Envelope>
```

#####Response status
HTTP Status: 200

**XML**
```xml

<measure>57.2</measure>
```

***
#### SavePersonMeasure(Long id, Measure m)
Return a mid of the new measure of the person identified by {id} and archive the old value in the history
#####POST Request
```xml

<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
    soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
    <soap:Body xmlns:m="http://soap.assignment.introsde/">
        <m:savePersonMeasure>
            <personId>55</personId>
            <measure>
                <measureType>weight</measureType>
                <measureValue>39</measureValue>
                <measureValueType>Double</measureValueType>
            </measure>
        </m:savePersonMeasure>
    </soap:Body>
</soap:Envelope>
```

#####Response status
HTTP Status: 200

**XML**
```xml

<mid>39</mid>
```

***
#### updatePersonMeasure(Long id, Measure m, Long mid)
Return the measure identified with {m.mid} updated, related to the person identified by {id}. 
If m is a current health update the value. But if m is a history health, search one current measure with same measureType of the m and set his/her attribute isCurrent a 0. And m will become a current measure (set isCurrent a 1), update the current date and the value
#####POST Request
```xml

<soap:Envelope 
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" 
    soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding">
    <soap:Body xmlns:m="http://soap.assignment.introsde/">
        <m:updatePersonMeasure>
            <personId>55</personId>
            <measure>
                <measureValue>35</measureValue>
            </measure>
            <mid>434</mid>
        </m:updatePersonMeasure>
    </soap:Body>
</soap:Envelope>
```

#####Response status
HTTP Status: 200

**XML**
```xml

<measure>
    <mid>434</mid>
    <dateRegistered>2015-12-06</dateRegistered>
    <measureType>weight</measureType>
    <measureValue>35</measureValue>
    <measureValueType>Double</measureValueType>
</measure>
```
