package com.cdaliaga.socios.models;

public class Socio {
    private int num;
    private String nombre;
    private boolean pagado;

    public Socio(int num, String nombre, boolean pagado) {
        this.num = num;
        this.nombre = nombre;
        this.pagado = pagado;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }
}
