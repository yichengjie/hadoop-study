package com.yicj.spark.ch3;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

import static org.apache.spark.sql.functions.*;

/**
 * @author: yicj
 * @date: 2023/5/21 18:47
 */
public class IngestionSchemaManipulationApp {

    public static void main(String[] args) {
        //1. 创建Spark会话
        SparkSession spark = SparkSession.builder()
                .appName("Restaurants in Wake County, NC")
                .master("local")
                .getOrCreate();
        //2. 创建数据帧
        Dataset<Row> df = spark.read()
                .format("csv")
                .option("header", "true")
                .load("data/Restaurants_in_Wake_County_NC.csv");
        //3. 显示数据
        System.out.println("********Right after ingestion**********");
        //printlnBasic(df);
        printWithColumn(df);
        //4. 停止spark
        spark.stop();
    }


    private static void printWithColumn(Dataset<Row> df) {
        Dataset<Row> newDF = df.withColumn("county", lit("Wake"))
                .withColumnRenamed("HSISID", "datasetId")
                .withColumnRenamed("NAME", "name")
                .withColumnRenamed("ADDRESS1", "address1")
                .withColumnRenamed("ADDRESS2", "address1")
                .withColumnRenamed("CITY", "city")
                .withColumnRenamed("STATE", "state")
                .withColumnRenamed("POSTALCODE", "zip")
                .withColumnRenamed("PHONENUMBER", "tel")
                .withColumnRenamed("RESTAURANTOPENDATE", "dateStart")
                .withColumnRenamed("FACILITYTYPE", "type")
                .withColumnRenamed("X", "geoX")
                .withColumnRenamed("Y", "geoY")
                .drop("OBJECTID")
                .drop("PERMITID")
                .drop("GEOCODESTATUS");
        // 补充唯一表示列
        newDF.withColumn("id",
                concat(newDF.col("state"), lit("_"),
                        newDF.col("county"), lit("_"),
                        newDF.col("datasetId")
                ));
        // 打印记录
        System.out.println("**********Dataframe transformed*******");
        newDF.show(5);
        newDF.printSchema();
        StructType schema = newDF.schema();
        String schemaJson = schema.prettyJson();
        System.out.println("*** Schema as JSON : " + schemaJson);
    }

    /**
     * 打印基本信息
     *
     * @param df
     */
    private static void printlnBasic(Dataset<Row> df) {
        df.show(5);
        //4. 打印schame
        df.printSchema();
    }
}
