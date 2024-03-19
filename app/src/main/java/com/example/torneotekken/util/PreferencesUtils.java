package com.example.torneotekken.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {
    private static final String PREF_NAME = "victorias";

    /**
     * Método estático para incrementar el contador de victorias de un jugador en las preferencias compartidas.
     * @param context     El contexto de la aplicación.
     * @param playerName  El nombre del jugador cuyo contador de victorias se va a incrementar.
     */
    public static void incrementPlayerVictories(Context context, String playerName) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int victorias = prefs.getInt(playerName, 0) + 1;
        editor.putInt(playerName, victorias);
        editor.apply();
    }

    /**
     * Obtiene el número de victorias de un jugador desde las preferencias compartidas.
     *
     * @param context    El contexto de la aplicación.
     * @param jugador    El nombre del jugador cuyas victorias se desean obtener.
     * @return           El número de victorias del jugador.
     */
    public static int obtenerVictorias(Context context, String jugador) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(jugador, 0);
    }

    /**
     * Reinicia las victorias de los jugadores en las preferencias compartidas.
     *
     * @param context    El contexto de la aplicación.
     * @param jugadores  Array de nombres de los jugadores cuyas victorias se van a reiniciar.
     */
    public static void reiniciarVictorias(Context context, String[] jugadores) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        for (String jugador : jugadores) {
            editor.putInt(jugador, 0);
            editor.putInt(jugador + "_consecutivas", 0);
            editor.putInt(jugador + "_max_consecutivas", 0);
        }
        editor.apply();
    }
}
