# spark-json-to-parquet-table

This project demonstrates how to explode json-arrays into a relation format using *spark*.

Example:

This source Json:
 `{"business_id":"1","categories":["Tobacco Shops","Nightlife","Vape Shops"],"price":100} `
 
will be converted into such a table.
 
| business_id   | categorie      | price  |
| ------------- |:--------------:| -----:|
| 1             | Tobacco Shops  | 100 |
| 1             | Nightlife      | 100 |
| 1             | Vape Shop      | 100 |

It utilizes the json files provided by [Yelp Dataset Challenge round#9](https://www.yelp.com/dataset_challenge).

This Spark-App subsequently reads all json files into a dataframe, validates the schema with help of case classes, explodes all arrays into additional tables and writes remaining attributes into the parent table. The parent table furthermore contains the array values as comma-separated string (might helpful for certain usecases). 


## Getting Started

1. Clone this project.
2. download tar file from [Yelp Dataset Challenge round#9](https://www.yelp.com/dataset_challenge).
3. build package with sbt 
4. 

## Requirements
- spark 2.1
- scala 2.17
- sbt
 
 
## Running the tests

```
sbt test
```

## Built With

* [SBT](http://www.scala-sbt.org/)


