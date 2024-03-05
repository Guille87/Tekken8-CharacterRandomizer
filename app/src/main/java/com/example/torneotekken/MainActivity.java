package com.example.torneotekken;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String[] JUGADORES = {"Jugador 1", "Jugador 2", "Jugador 3", "Jugador 4"};
    private static final String[] PERSONAJES = {"Jin", "Kazuya", "Jun", "King", "Paul", "Lars", "Law", "Jack-8", "Nina", "Xiaoyu", "Leroy", "Asuka", "Lili", "Hwoarang",
            "Bryan Fury", "Claudio", "Raven", "Azucena", "Leo", "Yoshimitsu", "Steve", "Shaheen", "Dragunov", "Kuma", "Panda", "Zafina", "Lee", "Alisa", "Devil Jin", "Victor", "Reina", "Feng"};

    private JugadorAdapter adapter;
    Button btnCambiarPersonajeJugador1;
    Button btnCambiarPersonajeJugador2;
    Button btnCambiarPersonajeJugador3;
    Button btnCambiarPersonajeJugador4;
    Button btnGanador;
    Button btnVerVictorias;
    private TextView textTiempoRestanteJugador1;
    private TextView textTiempoRestanteJugador2;
    private TextView textTiempoRestanteJugador3;
    private TextView textTiempoRestanteJugador4;
    // Define una variable para guardar el último botón ganador
    Button ultimoBotonGanador = null;
    private int cuentasRegresivasActivas = 0; // Contador para realizar un seguimiento de las cuentas regresivas activas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);
        adapter = new JugadorAdapter(this, new ArrayList<>()); // Usar el adaptador personalizado
        listView.setAdapter(adapter);

        Button btnAsignarPersonajes = findViewById(R.id.btnAsignarPersonajes);
        btnCambiarPersonajeJugador1 = findViewById(R.id.btnCambiarPersonajeJugador1);
        btnCambiarPersonajeJugador2 = findViewById(R.id.btnCambiarPersonajeJugador2);
        btnCambiarPersonajeJugador3 = findViewById(R.id.btnCambiarPersonajeJugador3);
        btnCambiarPersonajeJugador4 = findViewById(R.id.btnCambiarPersonajeJugador4);
        btnGanador = findViewById(R.id.btnGanador);
        btnVerVictorias = findViewById(R.id.btnVerVictorias);
        textTiempoRestanteJugador1 = findViewById(R.id.textTiempoRestanteJugador1);
        textTiempoRestanteJugador2 = findViewById(R.id.textTiempoRestanteJugador2);
        textTiempoRestanteJugador3 = findViewById(R.id.textTiempoRestanteJugador3);
        textTiempoRestanteJugador4 = findViewById(R.id.textTiempoRestanteJugador4);

        // Define una lista de botones para los jugadores
        List<Button> botonesJugadores = new ArrayList<>();
        botonesJugadores.add(btnCambiarPersonajeJugador1);
        botonesJugadores.add(btnCambiarPersonajeJugador2);
        botonesJugadores.add(btnCambiarPersonajeJugador3);
        botonesJugadores.add(btnCambiarPersonajeJugador4);

        btnAsignarPersonajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignarPersonajesAleatoriamente();
                btnCambiarPersonajeJugador1.setVisibility(View.VISIBLE);
                btnCambiarPersonajeJugador2.setVisibility(View.VISIBLE);
                btnCambiarPersonajeJugador3.setVisibility(View.VISIBLE);
                btnCambiarPersonajeJugador4.setVisibility(View.VISIBLE);
                btnGanador.setVisibility(View.VISIBLE);
                btnVerVictorias.setVisibility(View.VISIBLE);
                btnAsignarPersonajes.setVisibility(View.GONE);
            }
        });

        // Cambiar personaje jugador 1
        btnCambiarPersonajeJugador1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarPersonajeJugador(0);
                inhabilitarBoton(btnCambiarPersonajeJugador1, 0);
            }
        });

        // Cambiar personaje jugador 2
        btnCambiarPersonajeJugador2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarPersonajeJugador(1);
                inhabilitarBoton(btnCambiarPersonajeJugador2, 1);
            }
        });

        // Cambiar personaje jugador 3
        btnCambiarPersonajeJugador3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarPersonajeJugador(2);
                inhabilitarBoton(btnCambiarPersonajeJugador3, 2);
            }
        });

        // Cambiar personaje jugador 4
        btnCambiarPersonajeJugador4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarPersonajeJugador(3);
                inhabilitarBoton(btnCambiarPersonajeJugador4, 3);
            }
        });

        // Define un diccionario para almacenar el color original de cada botón ganador
        Map<Button, Integer> coloresOriginales = new HashMap<>();
        // Botón para decidir quien ha ganado la ronda
        btnGanador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un diálogo de selección para que el usuario elija quién ha ganado
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Selecciona el ganador");
                builder.setItems(JUGADORES, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Obtener el nombre del jugador seleccionado como ganador
                        String ganador = JUGADORES[which];

                        // Llamar al método para incrementar las victorias del jugador ganador
                        incrementarVictoriasJugador(ganador);

                        // Determinar si el ganador es una chica
                        boolean esChica = ganador.equalsIgnoreCase("NaraSayumi");

                        // Construir el mensaje del Toast
                        String mensaje = esChica ? (ganador + " es la ganadora!") : (ganador + " es el ganador!");

                        // Mostrar mensaje con el ganador seleccionado
                        Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();

                        // Deshabilitar y cambiar el color de fondo del botón del jugador ganador
                        Button botonGanadorActual = botonesJugadores.get(which);
                        botonGanadorActual.setEnabled(false);

                        // Guardar el color original del botón antes de cambiarlo, solo si no está en gris
                        if (botonGanadorActual.getBackground() instanceof ColorDrawable) {
                            int colorOriginal = ((ColorDrawable) botonGanadorActual.getBackground()).getColor();
                            if (colorOriginal != getResources().getColor(R.color.colorGris)) {
                                coloresOriginales.put(botonGanadorActual, colorOriginal);
                                Log.d("Mi app", "onClick colorOriginal: " + colorOriginal);
                            }
                        }

                        botonGanadorActual.setBackgroundColor(getResources().getColor(R.color.colorGris));

                        // Restaurar el color del botón del último ganador solo si hay un último botón ganador y el botón actual es diferente
                        if (ultimoBotonGanador != null && !botonGanadorActual.equals(ultimoBotonGanador)) {
                            int colorOriginalUltimoGanador = coloresOriginales.get(ultimoBotonGanador);
                            ultimoBotonGanador.setBackgroundColor(colorOriginalUltimoGanador);
                            ultimoBotonGanador.setEnabled(true);
                            Log.d("Mi app", "onClick ultimoBotonGanador: " + ultimoBotonGanador);
                        }

                        // Actualizar el último botón ganador
                        ultimoBotonGanador = botonGanadorActual;
                    }
                });

                // Mostrar el diálogo de selección
                builder.show();
            }
        });

        btnVerVictorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir la actividad VictoriasActivity
                Intent intent = new Intent(MainActivity.this, VictoriasActivity.class);
                intent.putExtra("jugadores", JUGADORES);
                startActivity(intent);
            }
        });

        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remover el listener para evitar llamadas repetidas
                listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // Obtener la posición vertical del final del ListView
                int listViewBottom = listView.getBottom();
                // Añadir un margen para asegurar que el botón esté completamente debajo del ListView
                int marginBottom = 16; // Puedes ajustar este valor según tus necesidades
                // Ajustar las restricciones del botón para ubicarlo debajo del ListView
                ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
                // Crear un nuevo conjunto de restricciones para el botón
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                // Establecer las restricciones del botón
                constraintSet.connect(btnAsignarPersonajes.getId(), ConstraintSet.TOP, listView.getId(), ConstraintSet.BOTTOM, marginBottom);
                // Aplicar los cambios
                constraintSet.applyTo(constraintLayout);
            }
        });
    }

    private void asignarPersonajesAleatoriamente() {
        List<Jugador> jugadores = new ArrayList<>();
        List<String> personajesList = new ArrayList<>();
        Collections.addAll(personajesList, PERSONAJES);
        Collections.shuffle(personajesList);

        for (int i = 0; i < JUGADORES.length; i++) {
            Jugador jugador = new Jugador(JUGADORES[i], personajesList.get(i)); // Crear un objeto Jugador
            jugadores.add(jugador); // Agregar el jugador a la lista
        }

        adapter.clear();
        adapter.addAll(jugadores); // Usar el método addAll para agregar la lista de jugadores al adaptador
    }

    private void cambiarPersonajeJugador(int posicionJugador) {
        // Obtener una instancia de Random
        Random random = new Random();
        // Generar un índice aleatorio dentro del rango de la lista de personajes
        int indiceAleatorio = random.nextInt(PERSONAJES.length);
        // Obtener el personaje correspondiente al índice aleatorio
        String nuevoPersonaje = PERSONAJES[indiceAleatorio];

        // Obtener el objeto Jugador correspondiente en el adaptador
        Jugador jugador = adapter.getItem(posicionJugador);

        // Actualizar el personaje del jugador
        jugador.setNombrePersonaje(nuevoPersonaje);

        // Notificar al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged();
    }

    private void inhabilitarBoton(final Button boton, final int jugador) {
        // Incrementar el contador de cuentas regresivas activas
        cuentasRegresivasActivas++;

        // Guardar el color original del botón
        final int colorOriginal = ((ColorDrawable) boton.getBackground()).getColor();
        // Desactivar el botón
        boton.setEnabled(false);
        btnGanador.setEnabled(false);
        boton.setBackgroundColor(getResources().getColor(R.color.colorGris)); // Cambiar color a gris

        // Iniciar un temporizador de 5 segundos (5,000 milisegundos)
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Actualizar el TextView para mostrar el tiempo restante
                long segundosRestantes = millisUntilFinished / 1000;
                String nombreJugador = JUGADORES[jugador]; // Obtener el nombre del jugador
                String textoTiempoRestante = "Tiempo restante " + nombreJugador + ": " + segundosRestantes + " segundos";
                if (jugador == 0) {
                    textTiempoRestanteJugador1.setText(textoTiempoRestante);
                    textTiempoRestanteJugador1.setVisibility(View.VISIBLE);
                } else if (jugador == 1) {
                    textTiempoRestanteJugador2.setText(textoTiempoRestante);
                    textTiempoRestanteJugador2.setVisibility(View.VISIBLE);
                } else if (jugador == 2) {
                    textTiempoRestanteJugador3.setText(textoTiempoRestante);
                    textTiempoRestanteJugador3.setVisibility(View.VISIBLE);
                } else if (jugador == 3){
                    textTiempoRestanteJugador4.setText(textoTiempoRestante);
                    textTiempoRestanteJugador4.setVisibility(View.VISIBLE);
                }
            }

            public void onFinish() {
                // Decrementar el contador de cuentas regresivas activas
                cuentasRegresivasActivas--;
                // Habilitar el botón después de que haya pasado 1 minuto
                boton.setEnabled(true);
                boton.setBackgroundColor(colorOriginal); // Restaurar color original
                if (jugador == 0) {
                    textTiempoRestanteJugador1.setVisibility(View.GONE);
                } else if (jugador == 1) {
                    textTiempoRestanteJugador2.setVisibility(View.GONE);
                } else if (jugador == 2) {
                    textTiempoRestanteJugador3.setVisibility(View.GONE);
                } else if (jugador == 3) {
                    textTiempoRestanteJugador4.setVisibility(View.GONE);
                }
                // Activar btnGanador solo si no hay más cuentas regresivas activas
                if (cuentasRegresivasActivas == 0) {
                    btnGanador.setEnabled(true);
                }
            }
        }.start();
    }

    // Método para incrementar el contador de victorias de un jugador y guardar el valor en SharedPreferences
    private void incrementarVictoriasJugador(String nombreJugador) {
        SharedPreferences prefs = getSharedPreferences("victorias", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Obtener el número actual de victorias y victorias consecutivas del jugador
        int victorias = prefs.getInt(nombreJugador, 0);
        int victoriasConsecutivas = prefs.getInt(nombreJugador + "_consecutivas", 0);

        // Incrementar el número de victorias
        victorias++;

        // Guardar el número actual de victorias y victorias consecutivas
        editor.putInt(nombreJugador, victorias);
        editor.putInt(nombreJugador + "_consecutivas", victoriasConsecutivas);

        // Guardar el nombre del último ganador
        editor.putString("ultimoGanador", nombreJugador);

        editor.apply();
    }
}