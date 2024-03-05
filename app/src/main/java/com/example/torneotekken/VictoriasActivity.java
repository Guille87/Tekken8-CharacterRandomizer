package com.example.torneotekken;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VictoriasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victorias);

        // Habilitar la navegación hacia atrás
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Obtener nombres de jugadores del intent
        String[] jugadores = getIntent().getStringArrayExtra("jugadores");

        // Referenciar al TextView en el layout
        TextView textViewVictorias = findViewById(R.id.textViewVictorias);
        Button btnReiniciarVictorias = findViewById(R.id.btnReiniciarVictorias);

        // Mostrar las victorias de los jugadores
        mostrarVictorias(jugadores, textViewVictorias);

        // Configurar OnClickListener para el botón de reiniciar victorias
        btnReiniciarVictorias.setOnClickListener(v -> {
            reiniciarVictorias(jugadores);
            mostrarVictorias(jugadores, textViewVictorias); // Actualizar la visualización después de reiniciar
        });
    }

    // Método para mostrar las victorias de los jugadores en la interfaz de usuario
    private void mostrarVictorias(String[] jugadores, TextView textView) {
        SharedPreferences prefs = getSharedPreferences("victorias", MODE_PRIVATE);
        StringBuilder stringBuilder = new StringBuilder();

        for (String jugador : jugadores) {
            int victorias = prefs.getInt(jugador, 0);
            String textoVictorias = "Victorias de " + jugador + ": " + victorias;

            // Agregar el textoVictorias al StringBuilder con un salto de línea
            stringBuilder.append(textoVictorias).append("\n");
        }

        // Establecer el texto en el TextView
        textView.setText(stringBuilder.toString());
    }

    // Método para reiniciar las victorias de los jugadores
    private void reiniciarVictorias(String[] jugadores) {
        SharedPreferences.Editor editor = getSharedPreferences("victorias", MODE_PRIVATE).edit();
        for (String jugador : jugadores) {
            editor.putInt(jugador, 0);
            editor.putInt(jugador + "_consecutivas", 0); // Reiniciar las victorias
            editor.putInt(jugador + "_max_consecutivas", 0); // Reiniciar las victorias consecutivas
        }
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Manejar el botón de navegación hacia atrás en la barra de aplicaciones
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}