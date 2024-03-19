package com.example.torneotekken.activities;

import static com.example.torneotekken.util.Constantes.JUGADORES;
import static com.example.torneotekken.util.Constantes.PERSONAJES;
import static com.example.torneotekken.util.Constantes.TIEMPO_CONTADOR_MILISEGUNDOS;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.example.torneotekken.R;
import com.example.torneotekken.adapters.JugadorAdapter;
import com.example.torneotekken.databinding.ActivityMainBinding;
import com.example.torneotekken.models.Jugador;
import com.example.torneotekken.util.Constantes;
import com.example.torneotekken.util.PreferencesUtils;
import com.example.torneotekken.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    // Define una lista de botones para los jugadores
    private List<Button> botonesJugadores = new ArrayList<>();
    // Define un diccionario para almacenar el color original de cada botón ganador
    private Map<Button, Integer> coloresOriginales = new HashMap<>();
    private JugadorAdapter adapter;
    private Button ultimoBotonGanador = null;
    private int cuentasRegresivasActivas = 0; // Contador para realizar un seguimiento de las cuentas regresivas activas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configurarLayout();
    }

    private void configurarLayout() {
        configurarListaJugadores();
        inicializarBotones();
        configurarBotones();
        configurarLayoutLista();
    }

    private void configurarListaJugadores() {
        ListView listView = findViewById(R.id.listView);
        adapter = new JugadorAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);
    }

    private void inicializarBotones() {
        inicializarBotonesCambiarPersonaje();
        asignarPersonajesAleatoriamente();
    }

    private void configurarBotones() {
        binding.btnGanador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manejarSeleccionGanador();
            }
        });

        binding.btnVerVictorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividadVictorias();
            }
        });
    }

    private void abrirActividadVictorias() {
        Intent intent = new Intent(MainActivity.this, VictoriasActivity.class);
        intent.putExtra("jugadores", JUGADORES);
        startActivity(intent);
    }

    private void configurarLayoutLista() {
        // Obtener la referencia del ListView desde el layout mediante su ID
        ListView listView = findViewById(R.id.listView);

        // Agregar un Listener para ser notificado cuando la jerarquía de la vista global está completamente dibujada
        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remover este Listener después de ser invocado una vez para evitar llamadas repetidas
                listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Obtener la referencia del ConstraintLayout desde el layout mediante su ID
                ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

                // Crear un nuevo conjunto de restricciones a partir de las restricciones existentes del ConstraintLayout
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);

                // Aplicar las restricciones actualizadas al ConstraintLayout
                constraintSet.applyTo(constraintLayout);
            }
        });
    }

    // Método para inicializar los botones de cambio de personaje
    private void inicializarBotonesCambiarPersonaje() {
        // Agrega los botones de cambio de personaje a la lista
        botonesJugadores.add(binding.btnCambiarPersonajeJugador1);
        botonesJugadores.add(binding.btnCambiarPersonajeJugador2);
        botonesJugadores.add(binding.btnCambiarPersonajeJugador3);
        botonesJugadores.add(binding.btnCambiarPersonajeJugador4);

        // Configura el listener de clic para cada botón de cambio de personaje
        for (int i = 0; i < botonesJugadores.size(); i++) {
            final int index = i;
            botonesJugadores.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Llama al método para cambiar el personaje del jugador correspondiente
                    cambiarPersonajeJugador(index);
                    // Llama al método para inhabilitar el botón del jugador correspondiente
                    inhabilitarBoton(botonesJugadores.get(index), index);
                }
            });
        }
    }

    // Método para manejar la selección del ganador
    private void manejarSeleccionGanador() {
        // Crear un diálogo de selección para que el usuario elija quién ha ganado
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Selecciona el ganador");
        builder.setItems(JUGADORES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtener el nombre del jugador seleccionado como ganador
                String ganador = JUGADORES[which];

                // Llamar al método para incrementar las victorias del jugador ganador
                PreferencesUtils.incrementPlayerVictories(MainActivity.this, ganador);

                // Obtener el mensaje del ganador
                String mensaje = StringUtils.getWinnerMessage(ganador);

                // Mostrar mensaje con el ganador seleccionado
                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();

                // Deshabilitar y cambiar el color de fondo del botón del jugador ganador
                Button botonGanadorActual = botonesJugadores.get(which);
                botonGanadorActual.setEnabled(false);

                // Guardar el color original del botón antes de cambiarlo, solo si no está en gris
                if (botonGanadorActual.getBackground() instanceof ColorDrawable) {
                    int colorOriginal = ((ColorDrawable) botonGanadorActual.getBackground()).getColor();
                    // Verificar si el color original no es gris antes de guardarlo
                    if (colorOriginal != ContextCompat.getColor(MainActivity.this, R.color.colorGris)) {
                        // Guardar el color original en el diccionario coloresOriginales
                        coloresOriginales.put(botonGanadorActual, colorOriginal);
                    }
                }

                // Cambiar el color de fondo del botón al color gris
                botonGanadorActual.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorGris));

                // Restaurar el color del botón del último ganador solo si hay un último botón ganador y el botón actual es diferente
                if (ultimoBotonGanador != null && !botonGanadorActual.equals(ultimoBotonGanador)) {
                    // Obtener el color original del último botón ganador
                    int colorOriginalUltimoGanador = coloresOriginales.get(ultimoBotonGanador);
                    // Restaurar el color original del último botón ganador
                    ultimoBotonGanador.setBackgroundColor(colorOriginalUltimoGanador);
                    // Habilitar el último botón ganador
                    ultimoBotonGanador.setEnabled(true);
                }

                // Actualizar el último botón ganador
                ultimoBotonGanador = botonGanadorActual;
            }
        });
        builder.show();
    }

    private void asignarPersonajesAleatoriamente() {
        // Crear una lista para almacenar los jugadores asignados aleatoriamente
        List<Jugador> jugadores = new ArrayList<>();
        // Crear una lista para almacenar los personajes disponibles
        List<String> personajesList = new ArrayList<>();
        // Agregar todos los personajes disponibles a la lista
        Collections.addAll(personajesList, PERSONAJES);
        // Mezclar la lista de personajes para asignarlos aleatoriamente a los jugadores
        Collections.shuffle(personajesList);

        // Iterar sobre la lista de jugadores predefinidos
        for (int i = 0; i < JUGADORES.length; i++) {
            // Crear un objeto Jugador con el nombre del jugador actual y un personaje aleatorio
            Jugador jugador = new Jugador(JUGADORES[i], personajesList.get(i));
            // Agregar el jugador a la lista de jugadores
            jugadores.add(jugador);
        }

        // Limpiar el adaptador para eliminar cualquier jugador previamente asignado
        adapter.clear();
        // Agregar todos los jugadores asignados aleatoriamente al adaptador
        adapter.addAll(jugadores);
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
        binding.btnGanador.setEnabled(false);
        boton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGris)); // Cambiar color a gris

        // Iniciar un temporizador
        new CountDownTimer(TIEMPO_CONTADOR_MILISEGUNDOS, 1000) {
            public void onTick(long millisUntilFinished) {
                // Actualizar el TextView para mostrar el tiempo restante
                long segundosRestantes = millisUntilFinished / 1000;
                String nombreJugador = JUGADORES[jugador]; // Obtener el nombre del jugador
                String textoTiempoRestante = "Tiempo restante " + nombreJugador + ": " + segundosRestantes + " segundos";
                if (jugador == 0) {
                    binding.textTiempoRestanteJugador1.setText(textoTiempoRestante);
                    binding.textTiempoRestanteJugador1.setVisibility(View.VISIBLE);
                } else if (jugador == 1) {
                    binding.textTiempoRestanteJugador2.setText(textoTiempoRestante);
                    binding.textTiempoRestanteJugador2.setVisibility(View.VISIBLE);
                } else if (jugador == 2) {
                    binding.textTiempoRestanteJugador3.setText(textoTiempoRestante);
                    binding.textTiempoRestanteJugador3.setVisibility(View.VISIBLE);
                } else {
                    binding.textTiempoRestanteJugador4.setText(textoTiempoRestante);
                    binding.textTiempoRestanteJugador4.setVisibility(View.VISIBLE);
                }
            }

            public void onFinish() {
                // Decrementar el contador de cuentas regresivas activas
                cuentasRegresivasActivas--;
                // Habilitar el botón después de que haya pasado 1 minuto
                boton.setEnabled(true);
                boton.setBackgroundColor(colorOriginal); // Restaurar color original
                if (jugador == 0) {
                    binding.textTiempoRestanteJugador1.setVisibility(View.GONE);
                } else if (jugador == 1) {
                    binding.textTiempoRestanteJugador2.setVisibility(View.GONE);
                } else if (jugador == 2) {
                    binding.textTiempoRestanteJugador3.setVisibility(View.GONE);
                } else if (jugador == 3) {
                    binding.textTiempoRestanteJugador4.setVisibility(View.GONE);
                }
                // Activar btnGanador solo si no hay más cuentas regresivas activas
                if (cuentasRegresivasActivas == 0) {
                    binding.btnGanador.setEnabled(true);
                }
            }
        }.start();
    }
}