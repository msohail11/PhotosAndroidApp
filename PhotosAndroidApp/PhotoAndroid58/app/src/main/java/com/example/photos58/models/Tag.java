package com.example.photos58.models;

import java.io.Serializable;

public class Tag implements Serializable {

    private static final long serialVersionUID = 4L;
    private String type;
    private String val;


    //-------constructor----------

    public Tag(String type, String val){
        this.type = type;
        this.val = val;
    }

    //-------key----------

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    //-------value----------

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }


    //-------toString----------

    public String toString(){
        return type + " = " + val;
    }


    //-------compare tags for equality ----------

    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tempTag = (Tag) o;
        return type.equalsIgnoreCase(tempTag.type) && val.equalsIgnoreCase(tempTag.val);
    }

}
