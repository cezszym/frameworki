# EasyHotel
Website for renting apartments and adding your own ads.

## Table of contents
* [General info](#general-info)
* [Use cases](#use-cases)
* [Lucene search](#lucene-search)
* [Authors](#authors)

## General info
Internet service for renting apartments from private persons. An authorized user can book a flat and add his own advertisement. For a better search for flats, the application has been equipped with a search engine (Lucene queryparser). API resources are available through JWT.

## Use cases
### Login & Register
<p align="center">
  <img width="460" height="300" src="https://user-images.githubusercontent.com/64873606/170527926-febc39ed-52a5-4f1f-8248-cf9e6601895e.png">
</p>

### Posting & Reservation
<p align="center">
  <img width="460" height="300" src="https://user-images.githubusercontent.com/64095970/170123445-f905e9b4-c63c-4fac-97a4-9e3ab45e1da8.png">
</p>

### Flat & FlatDetails
<p align="center">
  <img width="460" height="300" src="https://user-images.githubusercontent.com/64095970/170123843-683c7ed8-48f5-4438-89af-2480ed0a608b.png">
</p>

### Reservations
<p align="center">
  <img width="460" height="300" src="https://user-images.githubusercontent.com/64095970/170205766-45c5f81b-848f-42bf-8531-eacbfa906152.png">
</p>

## Lucene search
### Instalation
1. ```git clone```
2. install meaven dependencies
### Search
In order to search user needs to set a query value that needs to be encoded. User needs to go to tab Params in postman and set key as query and value as query value.

### Encoding process:


### Query options:
#### Post queries:
* **price**
* **title**
* **description**

#### Flat queries:
* **city**
* **country**
* **address**
* **metrage**
* **number of rooms** as numberOfRooms

#### Query options:
* **field specification**: in order to search by other field than default \(Post: price, Flat: city\) user needs to specify field by adding \<\<fieldName\>\>:query e.g. rooms:5
* **range search**: in numeric field search user can use range search like \<\<fieldName\>\>:\[min_value TO max_value\] e.g. rooms:\[1 TO 5\]
* **proximity search**: user can search for similar to query words by adding "edit distance" parameter to search like \<\<fieldName\>\>:query~\<\<proximity_value\>\> e.g. city:Lublin~4
* **full documentation**: [Lucene queries](https://lucene.apache.org/core/9_1_0/queryparser/org/apache/lucene/queryparser/flexible/standard/StandardQueryParser.html)

## Authors
- Jakub Sieczka
- Bogumił Sokołowski-Duda
- Patryk Suruło
- Cezary Szymański
- Mykola Ostapczuk

