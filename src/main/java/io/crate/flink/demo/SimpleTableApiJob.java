package io.crate.flink.demo;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.connector.jdbc.catalog.JdbcCatalog;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.util.Arrays;
import java.util.logging.Logger;

import static org.apache.flink.table.api.Expressions.$;

public class SimpleTableApiJob {

    private static final Logger LOGGER = Logger.getLogger("SimpleTableApiJob");
    private static final String CATALOG_NAME = "my_catalog";

    /**
     * Initialize your CrateDB with:
     * <pre
     * CREATE TABLE t1(s string) with (refresh_interval=1);
     * CREATE TABLE t2(a int) with (refresh_interval=1);
     * CREATE TABLE t3(name string, cnt int) with(refresh_interval=1);
     * CREATE TABLE my_schema.t4(name string, avg int, min int, max int) with (refresh_interval=1);
     * INSERT INTO t2(a) SELECT * from generate_series(1, 5, 1);
     * INSERT INTO t3(name, cnt) VALUES('Apache', 1), ('Apache', 2), ('Flink', 11), ('Flink', 22), ('Flink', 33), ('CrateDB', 111), ('CrateDB', 333);
     * </pre>
     */
    public static void main(String[] args) throws Exception {
        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
        TableEnvironment streamTableEnv = TableEnvironment.create(settings);
        EnvironmentSettings batchSettings = EnvironmentSettings.newInstance().inBatchMode().build();
        TableEnvironment batchTableEnv = TableEnvironment.create(batchSettings);


        String defaultDatabase = "crate";
        String username        = "crate";
        String password        = "crate";
        String baseUrl         = "jdbc:crate://localhost:5432";

        JdbcCatalog catalog = new JdbcCatalog(
                Thread.currentThread().getContextClassLoader(),
                CATALOG_NAME,
                defaultDatabase,
                username,
                password,
                baseUrl,
                null
                );
        streamTableEnv.registerCatalog(CATALOG_NAME, catalog);
        batchTableEnv.registerCatalog(CATALOG_NAME, catalog);

        LOGGER.info("Begin");

        // set the JdbcCatalog as the current catalog of the session
        streamTableEnv.useCatalog(CATALOG_NAME);
        batchTableEnv.useCatalog(CATALOG_NAME);

        // List tables
        LOGGER.info(() -> "Tables: " + Arrays.toString(streamTableEnv.listTables()));

        // Insert to CrateDB
        streamTableEnv.fromValues("foo", "bar", "Apache", "Flink", "CrateDB")
                .executeInsert("t1");
        LOGGER.info("SELECT FROM t1 after INSERT");
        streamTableEnv.executeSql("select s from t1").print();


        // Select with functions
        LOGGER.info("SELECT with functions");
        streamTableEnv.from("t1")
                .select($("s").trimLeading().charLength().as("length"))
                .execute()
                .print();

        // Select and Insert
        LOGGER.info("SELECT and INSERT");
        streamTableEnv.executeSql("select * from t2").print();
        streamTableEnv.executeSql("insert into t2(a) values (6), (7)");
        streamTableEnv.executeSql("select * from t2").print();

        // Count - stream
        LOGGER.info("COUNT DISTINCT in streaming mode");
        streamTableEnv.from("t2")
                .select($("a").count().distinct().as("distinct count"))
                .execute()
                .print();

        // Count - batch
        LOGGER.info("COUNT DISTINCT in batch mode");
        batchTableEnv.from("t2")
                .select($("a").count().distinct().as("distinct count"))
                .execute()
                .print();


        // Union & Filtering
        LOGGER.info("UNION and filtering with Java DSL");
        streamTableEnv.from("t2")
                .unionAll(streamTableEnv.from("t2"))
                .filter($("a").isGreater(4))
                .execute()
                .print();

        // Same with SQL
        LOGGER.info("UNION and filtering with SQL");
        streamTableEnv.executeSql(
                "SELECT * FROM(SELECT * FROM t2 UNION ALL SELECT * FROM t2) WHERE a > 4")
                .print();

        // Join
        LOGGER.info("JOIN db table with stream");
        streamTableEnv.from("t2")
                .join(streamTableEnv.fromValues(5, 6, 7, 11, 12, 13).as("b"), $("a").isEqual($("b")))
                .execute()
                .print();

        // Aggregations - stream
        LOGGER.info("Aggregations in streaming mode");
        streamTableEnv.from("t3")
                .groupBy($("name"))
                .select(
                        $("name"),
                        $("cnt").avg().as("avg"),
                        $("cnt").min().as("min"),
                        $("cnt").max().as("max"))
                .execute()
                .print();

        // Aggregations - batch - insert
        LOGGER.info("Aggregations in batch mode and insert into table");
        batchTableEnv.from("t3")
                .groupBy($("name"))
                .select(
                        $("name"),
                        $("cnt").avg().as("avg"),
                        $("cnt").min().as("min"),
                        $("cnt").max().as("max"))
                .insertInto("`my_schema.t4`")
                .execute()
                .await();

        streamTableEnv.from("`my_schema.t4`")
                .select($("*"))
                .execute()
                .print();

        // Datastream
        LOGGER.info("Table to datastream");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment streamTableEnv2 = StreamTableEnvironment.create(env);
        Table resultTable = streamTableEnv.sqlQuery("SELECT * FROM `my_schema.t4`");
        streamTableEnv2.toDataStream(resultTable)
                .print();
        env.execute();

        LOGGER.info("Table to datastream and filtering");
        //noinspection DataFlowIssue
        streamTableEnv2.toDataStream(resultTable)
                .filter((FilterFunction<Row>) value ->
                        ((String) value.getField("name")).endsWith("DB"))
                .print();
        env.execute();

        LOGGER.info("Table to datastream and map function");
        //noinspection DataFlowIssue
        streamTableEnv2.toDataStream(resultTable)
                .map((MapFunction<Row, Object>) value -> (Integer) value.getField(2) * 10)
                .print();
        env.execute();
    }
}
