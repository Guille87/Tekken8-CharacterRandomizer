package com.example.torneotekken;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class JugadorAdapter extends ArrayAdapter<Jugador> {

    public JugadorAdapter(Context context, List<Jugador> jugadores) {
        super(context, 0, jugadores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener el jugador en la posición actual
        Jugador jugador = getItem(position);

        // Reutilizar la vista si está disponible; de lo contrario, inflarla
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        }

        // Obtener referencias a los elementos de la vista
        TextView textViewNombreJugador = convertView.findViewById(R.id.textViewNombreJugador);
        TextView textViewNombrePersonaje = convertView.findViewById(R.id.textViewNombrePersonaje);

        textViewNombreJugador.setText(jugador.getNombre());
        textViewNombrePersonaje.setText(jugador.getNombrePersonaje());

        return convertView;
    }

    // Método para obtener un jugador en la posición dada
    @Override
    public Jugador getItem(int position) {
        return super.getItem(position);
    }
}
