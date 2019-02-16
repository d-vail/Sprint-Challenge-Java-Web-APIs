# Sprint Challenge: Java Web APIs

## Introduction

Many different car models are produced each year. Keeping track of these models can become an entertaining exercise. 
Attached you will find a list of just over 1000 car models including their brand and year produced. You will be creating 
a RESTful application to help search this list.

## Instructions

The cars.json file contains the cars to use in your application. They are in a JSON format with the fields: Year, Brand 
Model. So for example:

```` json
{
    "year": 2010,
    "brand": "BMW",
    "model": "1 Series"
}
````

Tells us that a 1 Series BMW model car was produced in year 2010.

The agreement between the client and server is that JSON objects will be used and each JSON object exchanged will follow 
the format

```` json
{ 
    "id":1,
    "year": 2010,
    "brand": "BMW",
    "model": "1 Series"
}
````

So, no additional link data is necessary in the JSON object. Also notice the addition of the internally generated and 
maintained ID field. The ID will be created and used by the application but not be loaded through the JSON file.

Also, a message queue will be used to communicate logging information from the server. Not all transactions will be 
logged. When a logging message is sent to the message queue, the JSON object will be something like:

```` json
{
    "mgs":"Looked up cars"
    "date""2019-01-04 01:15:35 PM"
}
````

Thus we have sent the message Looked up cars time stamped (the time the message is created) 01:14:15 P on January 4, 2019.

Write a RESTful web application that exposes the following end points Expose the following RESTful end points:

| Method    | Endpoint              | Action                                    |
| --------- | --------------------- | ----------------------------------------- |
| GET       | `/cars/id/{id}`       | Returns the car based of of id                                                                            |
| GET       | `/cars/year/{year}`   | Returns a list of cars of that year model                                                                 |
| GET       | `/cars/brand/{brand}` | Returns a list of cars of that brand<br>This is logged with a message of "search for {brand}"             |
| POST      | `/cars/upload`        | Loads multiple sets of data from the RequestBody<br>This is logged with a message of "Data loaded"        |
| DELETE    | `/cars/delete/{id}`   | Deletes a car from the list based off of the id<br>This is logged with a message of "{id} Data deleted"   |
