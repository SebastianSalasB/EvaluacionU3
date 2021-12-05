package com.ssalas.fbs.modelo;


import java.util.Date;

public class Usuario {

    private String SenId;
    private String nSensor;
    private String TipoS;
    private Date fecha_Ingreso;
    private String ValorS;
    private String UbicacionS;
    private String Observacion;

    public Usuario() {
    }

    public Usuario(String SenId, String nSensor, String TipoS, Date fecha_Ingreso, String ValorS, String UbicacionS,String Observacion) {
        this.nSensor = nSensor;
        this.TipoS = TipoS;
        this.fecha_Ingreso = fecha_Ingreso;
        this.ValorS = ValorS;
        this.SenId = SenId;
        this.UbicacionS=UbicacionS;
        this.Observacion = Observacion;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }

    public String getUbicacionS() {
        return UbicacionS;
    }

    public void setUbicacionS(String ubicacionS) {
        UbicacionS = ubicacionS;
    }

    public String getNSensor() {
        return nSensor;
    }

    public void setNSensor(String nSensor) {
        this.nSensor = nSensor;
    }

    public String getTipoS() {
        return TipoS;
    }

    public void setTipo(String tipoS) {
        this.TipoS = tipoS;
    }

    public Date getFechaIngreso() { return fecha_Ingreso; }

    public void setFechaIngreso(Date fecha_Ingreso) { this.fecha_Ingreso = fecha_Ingreso; }

    public String getValorS() {
        return ValorS;
    }

    public void setValorS(String valorS) {
        this.ValorS = valorS;
    }

    public String getSenId() {
        return SenId;
    }

    public void setSenId(String SenId) {
        this.SenId = SenId;
    }
}
