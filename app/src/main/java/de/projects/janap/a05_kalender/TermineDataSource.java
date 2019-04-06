package de.projects.janap.a05_kalender;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TermineDataSource {

    private SQLiteDatabase database;
    private TermineDatenbankHelfer dbHelfer;


    public TermineDataSource(Context pContext) {
        dbHelfer = new TermineDatenbankHelfer(pContext);
    }

    public void open() {
        Log.d("da", "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelfer.getWritableDatabase();
        Log.d("da", "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelfer.close();
        Log.d("da", "Datenbank mit Hilfe des DbHelfers geschlossen.");
    }

    private String[] spalten = {
            TermineDatenbankHelfer.SPALTE_ID,
            TermineDatenbankHelfer.SPALTE_TITEL,
            TermineDatenbankHelfer.SPALTE_START_TAG,
            TermineDatenbankHelfer.SPALTE_START_MONAT,
            TermineDatenbankHelfer.SPALTE_START_JAHR,
            TermineDatenbankHelfer.SPALTE_START_STUNDE,
            TermineDatenbankHelfer.SPALTE_START_MINUTE,
            TermineDatenbankHelfer.SPALTE_ENDE_TAG,
            TermineDatenbankHelfer.SPALTE_ENDE_MONAT,
            TermineDatenbankHelfer.SPALTE_ENDE_JAHR,
            TermineDatenbankHelfer.SPALTE_ENDE_STUNDE,
            TermineDatenbankHelfer.SPALTE_ENDE_MINUTE,
            TermineDatenbankHelfer.SPALTE_GANZTAEGIG
    };

    public void loescheTermin(Termin pTermin){
        long id = pTermin.getId();

        database.delete(TermineDatenbankHelfer.TABELLE_NAME_TERMINE,
                TermineDatenbankHelfer.SPALTE_ID + "=" + id,
                null);
        Log.d("da", "Eintrag gelöscht! ID: " + id + " Inhalt: " + pTermin.toString());
    }

    //Um Datensätze(Termine) in die Tabelle einzufügen
    public Termin erstelleTermin(String pTitel, Calendar pStart, Calendar pEnde, String pGanztaegig){
        ContentValues values = new ContentValues();
        values.put(TermineDatenbankHelfer.SPALTE_TITEL, pTitel);
        values.put(TermineDatenbankHelfer.SPALTE_START_TAG, pStart.get(Calendar.DAY_OF_MONTH));
        values.put(TermineDatenbankHelfer.SPALTE_START_MONAT, pStart.get(Calendar.MONTH));
        values.put(TermineDatenbankHelfer.SPALTE_START_JAHR, pStart.get(Calendar.YEAR));
        values.put(TermineDatenbankHelfer.SPALTE_START_STUNDE, pStart.get(Calendar.HOUR));
        values.put(TermineDatenbankHelfer.SPALTE_START_MINUTE, pStart.get(Calendar.MINUTE));
        values.put(TermineDatenbankHelfer.SPALTE_ENDE_TAG, pEnde.get(Calendar.DAY_OF_MONTH));
        values.put(TermineDatenbankHelfer.SPALTE_ENDE_MONAT, pEnde.get(Calendar.MONTH));
        values.put(TermineDatenbankHelfer.SPALTE_ENDE_JAHR, pEnde.get(Calendar.YEAR));
        values.put(TermineDatenbankHelfer.SPALTE_ENDE_STUNDE, pEnde.get(Calendar.HOUR));
        values.put(TermineDatenbankHelfer.SPALTE_ENDE_MINUTE, pEnde.get(Calendar.MINUTE));
        values.put(TermineDatenbankHelfer.SPALTE_GANZTAEGIG, pGanztaegig);

        long insertId = database.insert(TermineDatenbankHelfer.TABELLE_NAME_TERMINE, null, values);

        Cursor cursor = database.query(TermineDatenbankHelfer.TABELLE_NAME_TERMINE,
                        spalten, TermineDatenbankHelfer.SPALTE_ID + " = " + insertId,
                        null, null, null, null);
        cursor.moveToFirst();
        Termin termin = cursorZuTermin(cursor);
        cursor.close();
        return termin;
    }

    //Um einen Datensatz(einen Termin) aus der Tabelle auszulesen
    private Termin cursorZuTermin(Cursor pCursor){
        int idIndex = pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_ID);
        int idTerminTitel = pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_TITEL);
        int idStartTag = pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_START_TAG);
        int idStartMonat = pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_START_MONAT);
        int idStartJahr = pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_START_JAHR);
        int idStartStunde = pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_START_STUNDE);
        int idStartMinute = pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_START_MINUTE);
        int idEndTag= pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_ENDE_TAG);
        int idEndMonat = pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_ENDE_MONAT);
        int idEndJahr = pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_ENDE_JAHR);
        int idEndStunde = pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_ENDE_STUNDE);
        int idEndMinute = pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_ENDE_MINUTE);
        int idGanztaegig = pCursor.getColumnIndex(TermineDatenbankHelfer.SPALTE_GANZTAEGIG);

        String titel = pCursor.getString(idTerminTitel);

        //StartDaten des Termins aus Tabelle bekommen und abspeichern
        int startTag = pCursor.getInt(idStartTag);
        int startMonat = pCursor.getInt(idStartMonat);
        int startJahr = pCursor.getInt(idStartJahr);
        int startStunde = pCursor.getInt(idStartStunde);
        int startMinute = pCursor.getInt(idStartMinute);

        Calendar start = Calendar.getInstance();
        start.set(startJahr, startMonat, startTag, startStunde, startMinute);

        //EndDaten des Termins aus Tabelle bekommen und abspeichern
        int endTag = pCursor.getInt(idEndTag);
        int endMonat = pCursor.getInt(idEndMonat);
        int endJahr = pCursor.getInt(idEndJahr);
        int endStunde = pCursor.getInt(idEndStunde);
        int endMinute = pCursor.getInt(idEndMinute);

        Calendar ende = Calendar.getInstance();
        ende.set(endJahr, endMonat, endTag, endStunde, endMinute);


        String sGanztaegig = pCursor.getString(idGanztaegig);
        Boolean ganztaegig;
        if (sGanztaegig.equals("true")){
            ganztaegig = true;
        }else if (sGanztaegig.equals("false")){
            ganztaegig = false;
        }else{
            ganztaegig = false;
        }

        long id = pCursor.getLong(idIndex);

        Termin termin = new Termin(id, titel, start, ende, ganztaegig);


        return termin;
    }

    //Um alle vorhandenen Datensätze aus der Tabelle auszulesen
    public List<Termin> getAlleTermine(){
        List<Termin> terminListe = new ArrayList<>();
        Cursor cursor = database.query(TermineDatenbankHelfer.TABELLE_NAME_TERMINE,
                spalten, null, null, null, null, null);

        cursor.moveToFirst();
        Termin termin;

        while (!cursor.isAfterLast()){
            termin = cursorZuTermin(cursor);
            terminListe.add(termin);
            Log.d("da: ", "ID: " + termin.getId() + ", Inhalt: " + termin.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return terminListe;
    }
}
