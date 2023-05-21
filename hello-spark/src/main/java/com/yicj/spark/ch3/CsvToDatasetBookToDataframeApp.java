package com.yicj.spark.ch3;

import com.yicj.spark.model.Book;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import java.text.SimpleDateFormat;
import static org.apache.spark.sql.functions.*;

/**
 * @author: yicj
 * @date: 2023/5/21 20:54
 */
public class CsvToDatasetBookToDataframeApp {

    public static void main(String[] args) {

        new CsvToDatasetBookToDataframeApp().start();
    }


    private void start() {
        SparkSession spark = SparkSession.builder()
                .appName("CSV to dataframe to Dateset<Book> and back")
                .master("local")
                .config("spark.sql.legacy.timeParserPolicy", "LEGACY")
                .getOrCreate();
        // Needed by Spark v3.0.0 (Thanks @dapeng09)
        //spark.sql("set spark.sql.legacy.timeParserPolicy=LEGACY");
        //
        String filename = "data/books.csv";
        Dataset<Row> df = spark.read()
                .format("csv")
                .option("header", "true")
                .option("inferSchema", "true")
                .load(filename);
        //
        System.out.println("**** Books ingested in a dataframe");
        df.show(5);
        df.printSchema();
        //
        Dataset<Book> bookDS = df.map(new BookMapper(), Encoders.bean(Book.class));
        System.out.println("****** Books are now in a dataset of books ");
        bookDS.show(5, 17);
        bookDS.printSchema();
        //
        Dataset<Row> df2 = bookDS.toDF();
        df2= df2.withColumn("releaseDateAsString",
                concat(
                        expr("releaseDate.year + 1900"), lit("-"),
                        expr("releaseDate.month + 1"), lit("-"),
                        df2.col("releaseDate.date")
                ));
        //
        df2 = df2.withColumn("releaseDateAsDate",
                to_date(df2.col("releaseDateAsString"), "yyyy-MM-dd"))
                .drop("releaseDateAsString") ;
        //
        System.out.println("***** Books are back in a dataframe");
        df2.show(5, 13);
        df2.printSchema();
    }

    static class BookMapper implements MapFunction<Row, Book> {
        private SimpleDateFormat parser = new SimpleDateFormat("M/d/yy");

        @Override
        public Book call(Row value) throws Exception {
            Book book = new Book();
            book.setId(value.getAs("id"));
            book.setAuthorId(value.getAs("authorId"));
            book.setLink(value.getAs("link"));
            book.setTitle(value.getAs("title"));
            // data case
            String dateAsString = value.getAs("releaseDate");
            if (dateAsString != null) {
                book.setReleaseDate(parser.parse(dateAsString));
            }
            return book;
        }
    }
}
