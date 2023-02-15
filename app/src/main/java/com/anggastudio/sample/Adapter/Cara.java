package com.anggastudio.sample.Adapter;

public class Cara {
    private int id;
    private String numerocara;

    public Cara(int id, String numerocara,Integer foto){
        this.id = id;
        this.numerocara = numerocara;
    }
    public Cara(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNumerocara() {
        return numerocara;
    }
    public void setNumerocara(String numerocara){
        this.numerocara = numerocara;
    }
    @Override
    public String toString(){
        return "Cara{"+
                "id="+ id +
                ", numerocara=' "+ numerocara +
                '}';
    }
}
