package org.rb.utils.csv2sql;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by er23851 on 27.03.2017.
 */
public class ScriptGenerator {

    private String tableName = null;

    private LoadCSV loader;

    public ScriptGenerator(LoadCSV data)
    {
        this.loader = data;
    }

    public String getCreateTable()
    {
        List<String> columnDefList = new ArrayList<>();
        List<String> labelDefList = new ArrayList<>();
        this.tableName  = loader.getShortFileName().replace(".","").toUpperCase();

        for(Field f:loader.getFieldList()){

            String colDef = String.format("%s  VARCHAR(%s)",f.getName().toUpperCase(),f.getSize());
            columnDefList.add(colDef);

            String labelDef = String.format("%s IS '%s'",f.getName().toUpperCase(),f.getDescription());
            labelDefList.add(labelDef);
        }

        String createDef = String.format("CREATE TABLE %s (%s);", tableName, String.join(",",columnDefList));
        String labelDef = String.format("LABEL ON COLUMN %s (%s);",tableName,String.join(",",labelDefList));

        return createDef + "\n" + labelDef;
    }

    public String getInsertScript()
    {
        List<String> fieldDefList = new ArrayList<>();
        List<String> insertList = new ArrayList<>();
        String templateString;

        for (Field field:loader.getFieldList()){

            fieldDefList.add(field.getName().toUpperCase());
        }
        templateString = String.format("INSERT INTO %s(%s) ",this.tableName,String.join(",",fieldDefList));

        for (List<String> dataList:loader.getDataList()){

            List<String> valueList = new ArrayList<>();

            for (int i=0;i<loader.getFieldList().size();i++)
            {
                if (i<dataList.size()) {
                    valueList.add(String.format("'%s'", dataList.get(i)));
                }
                else{
                    valueList.add("''");
                }
            }
            insertList.add(templateString + String.format(" VALUES(%s)", String.join(",",valueList)));
        }

        return String.join(";\n",insertList)+";";
    }

    public void saveCreateScript(String fileName) throws IOException {
        Path filePath = Paths.get(fileName);
        Files.write(filePath,this.getCreateTable().getBytes(Charset.forName("cp1251")), StandardOpenOption.CREATE);
    }

    public void saveInsertScript(String fileName) throws IOException {
        Path filePath = Paths.get(fileName);
        Files.write(filePath,this.getInsertScript().getBytes(Charset.forName("cp1251")), StandardOpenOption.CREATE);
    }

}
