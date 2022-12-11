package com.cdaliaga.socios.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

public class Socio implements Parcelable {
    private int num;
    private String nombre;
    private boolean pagado;

    public Socio() {
    }

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

    protected Socio(Parcel in) {
        num = in.readInt();
        nombre = in.readString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            pagado = in.readBoolean();
        }
    }

    public static final Creator<Socio> CREATOR = new Creator<Socio>() {
        @Override
        public Socio createFromParcel(Parcel in) {
            return new Socio(in);
        }

        @Override
        public Socio[] newArray(int size) {
            return new Socio[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(num);
        parcel.writeString(nombre);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            parcel.writeBoolean(pagado);
        }
    }
}
