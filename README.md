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

### Spark-App
This Spark-App subsequently reads all json files into a dataframe, validates the schema with help of case classes, explodes all arrays into additional tables and writes remaining attributes into the parent table.
The parent table furthermore contains the array values as comma-separated string (helpful for some analyses to avoid joining).
Spark-App is written in `Scala` and build with `SBT`. It utilizes `scalatest` for unit and integration test.
Find the sources are in `./src`.

### Spark-Zeppelin Docker Image
To test the Spark-App with spark-submit this project also provides Spark 2.1 together with Zeppelin as docker image.
The docker image is uploaded in [dockerhub](https://hub.docker.com/r/mirkoprescha/spark-zeppelin/) in a public repository.
A sample zeppelin notebook to analyze exploded tables is here `./zeppelin_notebooks/dataset-analysis.json.`


## Works with
- docker 1.13.1
- spark 2.1
- scala 2.17
- sbt 0.13.9


## Getting Started

Follow these steps to use compiled spark-app in provided docker image.



1. download tar file from [Yelp Dataset Challenge round#9](https://www.yelp.com/dataset_challenge)


2. run docker container

it will download image from dockerhub and run it in a container
```
docker run -it -p 8088:8080   mirkoprescha/spark-zeppelin
```
If you want to use zeppelin immediately, wait roughly 10 second until daemon started

3. Copy yelp_dataset_challenge_round9.tgz to docker container

Start another shell session and copy the file into the docker container.
(your latest started container)
```
docker cp yelp_dataset_challenge_round9.tgz $(docker ps  -l -q):/home/
```

4. run spark job

Go back to your first session. You should be connected as root in the docker container

```
cd /home
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

You can verfiy result on your machine with ` du -h output/`.

This should produce an output like this.
```
root@c6c0a39bc1fa:/home# du -h output/
4.8M	output/businessCategories
17M	output/checkinAsTable
4.2M	output/businessHours
4.8M	output/businessAttributes
703M	output/userAsTable
712M	output/userFriends
25M	output/userElite
9.5M	output/checkinTimes
1.8G	output/review
21M	output/businessAsTable
55M	output/tip
3.3G	output/
```

5. goto zeppelin ui: http://localhost:8088/#/

Open the Notebook called `analysis`.
Accept ("save") the interpreter bindings.
In the menu bar click to *play* button to run all paragraphs.

if the notebook is not available you have to download it from this git repo and import into zeppelin.
Alternatively check the results of the notebook in [zeppelin hub](https://www.zeppelinhub.com/viewer/notebooks/aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL21pcmtvcHJlc2NoYS9zcGFyay1qc29uLXRvLXRhYmxlL21hc3Rlci96ZXBwZWxpbl9ub3RlYm9va3MvZGF0YXNldC1hbmFseXNpcy5qc29u)


## deploy changes in spark-app

Clone this project.

After any changes to the spark-app you need to build a new package with

 ```
 sbt package
 ```

If all test are successful, place the package here
`./spark-docker/bin/spark-json-to-table_2.11-1.0.jar`


## changes in dockerfile

After changes in `Dockerfile` goto project home dir and run
```
docker build --file spark-docker/Dockerfile -t mirkoprescha/spark-zeppelin .
```

