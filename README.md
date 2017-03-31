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
3. build package with sbt

2. download tar file from [Yelp Dataset Challenge round#9](https://www.yelp.com/dataset_challenge) (it is 1.8 GB large)

6. start docker container

```
cd spark-docker
docker run -it -p 8080:8080   mirkoprescha/spark-zeppelin
```
If you want to use zeppelin immediately, wait roughly 10 second until daemon started

5. Copy tar to docker container (your latest started container)
```
docker cp yelp_dataset_challenge_round9.tgz $(docker ps  -l -q):/home/
```

5.
```
spark-submit   --class com.mprescha.json2Table.Json2Table \
      /usr/local/bin/spark-json-to-table_2.11-1.0.jar \
      /home/yelp_dataset_challenge_round9.tgz
```

Spark processing will take roughly 5 minutes.

If the job ran successful, following output-structure is generated in /home/output/.
- businessAsTable
- businessAttributes
- businessCategories
- businessHours
- checkinAsTable
- checkinTimes
- review
- tip
- userAsTable
- userElite
- userFriends

Each subdir represents an entity-type that can be analyzed in zeppelin notebook.

6. goto zeppelin ui: http://localhost:8080/#/


## works with
- spark 2.1
- scala 2.17
- sbt
- docker 1.13.1
 
 
## Running the tests

```
sbt test
```

## build jar

```
sbt package
```



## Built With

* [SBT](http://www.scala-sbt.org/)


