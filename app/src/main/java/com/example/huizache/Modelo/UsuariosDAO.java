package com.example.huizache.Modelo;

public class UsuariosDAO {
    String nombre, apellidoPaterno, apellidoMaterno, user, correo, password;
    public static String id;

    public UsuariosDAO() {
    }

    public UsuariosDAO(String nombre, String apellidoPaterno, String apellidoMaterno, String user, String correo, String password) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.user = user;
        this.correo = correo;
        this.password = password;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        UsuariosDAO.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
