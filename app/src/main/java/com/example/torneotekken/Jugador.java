package com.example.torneotekken;

public class Jugador {
    private String nombre;
    private String nombrePersonaje;

    public Jugador(String nombre, String nombrePersonaje) {
        this.nombre = nombre;
        this.nombrePersonaje = nombrePersonaje;
    }

    // Método para obtener el nombre del jugador
    public String getNombre() {
        return nombre;
    }

    // Método para establecer el nombre del jugador
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método para obtener el nombre del personaje del jugador
    public String getNombrePersonaje() {
        return nombrePersonaje;
    }

    // Método para establecer el nombre del personaje del jugador
    public void setNombrePersonaje(String nombrePersonaje) {
        this.nombrePersonaje = nombrePersonaje;
    }
}
