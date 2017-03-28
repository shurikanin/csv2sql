package org.rb.utils.csv2sql;

import java.io.IOException;

/**
 * Created by er23851 on 24.03.2017.
 */
public class Program {

    public static void main(String[] args) throws IOException {
        System.out.println("Convert CSV to SQL create table and inserts row");

        if (args.length>0) {
            LoadCSV loadCSV = new LoadCSV(args[0]);
            loadCSV.load();
            System.out.println(String.format("File %s is loaded", loadCSV.getFileName()));

            ScriptGenerator sg = new ScriptGenerator(loadCSV);
            sg.saveCreateScript("create.sql");
            sg.saveInsertScript("insert.sql");
            System.out.println(String.format("File %s is generated", "create.sql"));
            System.out.println(String.format("File %s is generated", "insert.sql"));
            System.out.println("Working complete");
        }
        else {
            System.out.println("csv2sql fileName.csv");
        }
    }

}
