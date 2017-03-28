package org.rb.utils.csv2sql;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by er23851 on 24.03.2017.
 */
public class LoadCSV {

    private List<Field> fieldList = new ArrayList<Field>();
    private List<List<String>> dataList = new ArrayList<List<String>>();
    private String fileName;
    private String shortFileName;

    public LoadCSV(String fName)
    {
        this.fileName = fName;
    }

    public void load() throws IOException {
        Path filePath = Paths.get(this.fileName);
        this.shortFileName = filePath.getFileName().toString();
        Transliterator tl = new Transliterator();
        if (Files.exists(filePath))
        {
            List<String> listData = Files.readAllLines(filePath, Charset.forName("cp1251"));

            if (!listData.isEmpty())
            {
                String firstRow = listData.get(0);
                String[] firstRowArray = firstRow.split(";");
                listData.remove(0);

                for(String s:firstRowArray) {

                    Field f = new Field();
                    String loadString = tl.transliterate(s).replace(".","").replace(",","").replace(" ","");
                    f.setName(loadString.substring(0,Math.min(loadString.length(),10)));
                    f.setDescription(s);
                    long fieldCount = 1;

                    while (fieldCount>0) {
                        Predicate<Field> fieldPredicate = new Predicate<Field>() {
                            @Override
                            public boolean test(Field field) {
                                return field.getName().equals(f.getName());
                            }
                        };

                        fieldCount = fieldList.stream().filter(fieldPredicate).count();
                        if (fieldCount > 0) {
                            f.setName(f.getName()+String.format("%s",fieldCount+1));
                        }
                    }

                    fieldList.add(f);
                }

                for (String row:listData) {

                    List<String> rowList = new ArrayList<>();
                    String[] arow = row.split(";");

                    int i=0;
                    for(String cell:arow){
                        rowList.add(cell.trim());
                        if (i<this.fieldList.size()) {
                            int maxLen = this.fieldList.get(i).getSize();
                            this.fieldList.get(i).setSize(Math.max(maxLen, cell.length()));
                        }
                        i++;
                    }

                    this.dataList.add(rowList);
                }

                int a=1;
            }
        }

    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public List<List<String>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<String>> dataList) {
        this.dataList = dataList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getShortFileName() {
        return shortFileName;
    }

    public void setShortFileName(String shortFileName) {
        this.shortFileName = shortFileName;
    }
}
