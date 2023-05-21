package com.yicj.spark.ch3;

import org.apache.spark.Partition;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.*;

/**
 * @author: yicj
 * @date: 2023/5/21 19:50
 */
public class HelloCombineDataframesApp {

    private SparkSession spark;

    public static void main(String[] args) {
        new HelloCombineDataframesApp().start();
    }

    private void start() {
        spark = SparkSession.builder()
                .appName("combine dataframe app")
                .master("local")
                .getOrCreate();

        Dataset<Row> df1 = this.buildWakeRestaurantsDataframe();
        Dataset<Row> df2 = this.buildDurhamRestaurantsDataframe();
        this.combineDataframe(df1, df2);
        // 停止spark
        spark.stop();
    }


    private void combineDataframe(Dataset<Row> df1, Dataset<Row> df2){
        Dataset<Row> df = df1.unionByName(df2);
        df.show(5);
        df.printSchema();
        System.out.println("We have " + df.count() + "record .");
        //
        Partition[] partitions = df.rdd().partitions();
        int partitionCount = partitions.length;
        System.out.println("Partition count : " + partitionCount);
    }


    private Dataset<Row> buildWakeRestaurantsDataframe() {
        Dataset<Row> df = this.spark.read()
                .format("csv")
                .option("header", "true")
                .load("data/Restaurants_in_Wake_County_NC.csv");
        df = df.withColumn("county", lit("Wake"))
                .withColumnRenamed("HSISID", "datasetId")
                .withColumnRenamed("NAME", "name")
                .withColumnRenamed("ADDRESS1", "address1")
                .withColumnRenamed("ADDRESS2", "address2")
                .withColumnRenamed("CITY", "city")
                .withColumnRenamed("STATE", "state")
                .withColumnRenamed("POSTALCODE", "zip")
                .withColumnRenamed("PHONENUMBER", "tel")
                .withColumnRenamed("RESTAURANTOPENDATE", "dateStart")
                .withColumn("dateEnd", lit(null))
                .withColumnRenamed("FACILITYTYPE", "type")
                .withColumnRenamed("X", "geoX")
                .withColumnRenamed("Y", "geoY")
                .drop("OBJECTID")
                .drop("PERMITID")
                .drop("GEOCODESTATUS");
        // 补充唯一主键列
        df = df.withColumn("id", concat(
                df.col("state"), lit("_"),
                df.col("county"), lit("_"),
                df.col("datasetId")
        ));
        return df;
    }

    private Dataset<Row> buildDurhamRestaurantsDataframe() {
        Dataset<Row> df = this.spark.read()
                .format("json")
                .load("data/Restaurants_in_Durham_County_NC.json");

        df = df.withColumn("county", lit("Durham"))
                .withColumn("datasetId", df.col("fields.id"))
                .withColumn("name", df.col("fields.premise_name"))
                .withColumn("address1", df.col("fields.premise_address1"))
                .withColumn("address2", df.col("fields.premise_address2"))
                .withColumn("city", df.col("fields.premise_city"))
                .withColumn("state", df.col("fields.premise_state"))
                .withColumn("zip", df.col("fields.premise_zip"))
                .withColumn("tel", df.col("fields.premise_phone"))
                .withColumn("dateStart", df.col("fields.opening_date"))
                .withColumn("dateEnd", df.col("fields.closing_date"))
                .withColumn("type", split(df.col("fields.type_description"), "-").getItem(1))
                .withColumn("geoX", df.col("fields.geolocation").getItem(0))
                .withColumn("geoY", df.col("fields.geolocation").getItem(1))
                .drop(df.col("fields"))
                .drop(df.col("geometry"))
                .drop(df.col("record_timestamp"))
                .drop(df.col("recordid"));
        // 补充唯一主键列
        df = df.withColumn("id", concat(
                df.col("state"), lit("_"),
                df.col("county"), lit("_"),
                df.col("datasetId")
        ));

        return df;
    }
}
