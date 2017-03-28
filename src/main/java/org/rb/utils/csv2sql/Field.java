package org.rb.utils.csv2sql;

/**
 * Created by er23851 on 24.03.2017.
 */
public class Field {

    private String name;
    private String description;
    private int size;

    public Field()
    {
        this.size = 0;
    }

    public Field(String n, String desc, int len)
    {
        this.name = n;
        this.description = desc;
        this.size = len;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
