package de.projects.janap.a05_kalender;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DatumAuswaehlen extends DialogFragment{

    public static final int KENNZEICHNUNG_START_DATUM = 0;  //Schlüssel zur Identifizierung eines Startdatums in Terminerstellung
    public static final int KENNZEICHNUNG_ENDE_DATUM = 1;   //Schlüssel zur Identifizierung eines Enddatums in Terminerstellung

    private int kennzeichnung = 0;  //Kennzeichnung speichert den Identifizeierungsschlüssel für das jeweilige Objekt

    public void setKennzeichnung(int pKennzeichnung) {
        this.kennzeichnung = pKennzeichnung;    //Um Schlüssel des Objekts zu setzten
    }
    public int getKennzeichnung() {
        return kennzeichnung;
    }

    @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            Calendar kalender = Kalender_Steuerung.kalender;    //Datepickerdialog soll mit dem zuletzt ausgewählten (im normalen Kalender) Datum geöffnet werden
            int jahr = kalender.get(Calendar.YEAR);
            int monat = kalender.get(Calendar.MONTH);
            int tag = kalender.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), jahr, monat, tag);   //Ein Datepickerdialog mit den richtigen Kalenderdaten wird zurückgegeben um angezeigt werden zu können
        }




}
