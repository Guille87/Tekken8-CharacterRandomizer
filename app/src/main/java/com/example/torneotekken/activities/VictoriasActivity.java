package com.example.torneotekken.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.torneotekken.databinding.ActivityVictoriasBinding;
import com.example.torneotekken.util.PreferencesUtils;

public class VictoriasActivity extends AppCompatActivity {

    private ActivityVictoriasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflar el layout y establecerlo como el contenido de la actividad
        binding = ActivityVictoriasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar la ActionBar para mostrar el botón de navegación hacia atrás
        setupActionBar();

        // Configurar las vistas y los botones de la actividad
        setupViews();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupViews() {
        // Obtener nombres de jugadores del intent
        String[] jugadores = getIntent().getStringArrayExtra("jugadores");

        // Mostrar las victorias de los jugadores
        mostrarVictorias(jugadores, binding.textViewVictorias);

        // Configurar OnClickListener para el botón de reiniciar victorias
        binding.btnReiniciarVictorias.setOnClickListener(v -> {
            reiniciarVictorias(jugadores);
            // Actualizar la visualización después de reiniciar
            mostrarVictorias(jugadores, binding.textViewVictorias);
        });

        // Configurar el botón de retroceso
        setupBackButton();
    }

    // Configurar el botón de retroceso para cerrar la actividad al ser presionado
    private void setupBackButton() {
        // Configurar el botón de retroceso
        OnBackPressedDispatcher onBackPressedDispatcher = this.getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    // Mostrar las victorias de los jugadores en el TextView
    private void mostrarVictorias(String[] jugadores, TextView textView) {
        StringBuilder stringBuilder = new StringBuilder();

        // Iterar sobre los nombres de los jugadores para obtener y mostrar sus victorias
        for (String jugador : jugadores) {
            int victorias = PreferencesUtils.obtenerVictorias(this, jugador);
            String textoVictorias = "Victorias de " + jugador + ": " + victorias;

            // Agregar el textoVictorias al StringBuilder con un salto de línea
            stringBuilder.append(textoVictorias).append("\n");
        }

        // Establecer el texto en el TextView
        textView.setText(stringBuilder.toString());
    }

    // Reiniciar las victorias de los jugadores
    private void reiniciarVictorias(String[] jugadores) {
        PreferencesUtils.reiniciarVictorias(this, jugadores);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Manejar el botón de navegación hacia atrás en la barra de aplicaciones
        if (item.getItemId() == android.R.id.home) {
            // Simular la pulsación del botón de retroceso
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}