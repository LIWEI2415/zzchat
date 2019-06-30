package org.fitzeng.zzchat;

import java.util.Comparator;

public class Data  {
    private int imgId;
    private String content;

    // public Data() {}
    public Data(){

    }
    public Data(int imgId, String content) {
        this.imgId = imgId;
        this.content = content;
    }
    public int getImgId() {
        return imgId;
    }

    public String getContent() {
        return content;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static class DataComparator implements Comparator<Data>{
        public int compare(Data a,Data b){
            String str1=a.getContent();
            String str2=b.getContent();
            return str1.compareTo(str2);
        }
    }

}

