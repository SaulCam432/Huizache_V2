package com.example.huizache.Modelo;

public class RecetasDAO {
    String nombre, imagen, ingredientes, descripcion,idReceta, idUsuario;

    public RecetasDAO(String idReceta, String nombre, String imagen, String ingredientes, String descripcion, String idUsuario) {
        this.idReceta = idReceta;
        this.nombre = nombre;
        this.imagen = imagen;
        this.ingredientes = ingredientes;
        this.descripcion = descripcion;
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdReceta() {
        return idReceta;
    }

    public void setIdRecetas(String idReceta) {
        this.idReceta = idReceta;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
