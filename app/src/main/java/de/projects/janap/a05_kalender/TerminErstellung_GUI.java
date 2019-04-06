package de.projects.janap.a05_kalender;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import java.util.Calendar;

public class TerminErstellung_GUI extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Attribute
    /*-------------------------Darstellung--------------------------------------------------------*/
    private EditText titel;

    private TextView startDatum, startZeit, enddatum, endzeit, txtEnde, txtStart, txtUeberpruefung;
    private Switch ganztaegigJaNein;
    private Button speichern, abbruch;

    /*-------------------------Andere Variablen---------------------------------------------------*/
    private Intent intent = new Intent(this, Kalender_GUI.class);
    private TerminErstellung_Steuerung strg;
    private TermineDataSource dataSource;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Methoden

    /*-------------------------get-Methoden-------------------------------------------------------*/
    public EditText getTitel() {
        return titel;
    }
    public TextView getStartDatum() {
        return startDatum;
    }
    public TextView getStartZeit() {
        return startZeit;
    }
    public TextView getEnddatum() {
        return enddatum;
    }
    public TextView getEndzeit() {
        return endzeit;
    }
    public Button getSpeichern() {
        return speichern;
    }
    public TermineDataSource getDataSource(){
        return dataSource;
    }

    /*-------------------------public Methoden----------------------------------------------------*/
    public void oeffneActivityKalenderMonatsUebersicht() {
        intent.putExtra(Kalender_Steuerung.LETZTER_MONAT_UEBERGABE, ""+Kalender_Steuerung.kalender.get(Calendar.MONTH));
        intent.putExtra(Kalender_Steuerung.LETZTES_JAHR_UEBERGABE, ""+Kalender_Steuerung.kalender.get(Calendar.YEAR));

        startActivity(intent);
        finish();
    }
    public void lasseUnwichtigesVerschwinde() {
        startZeit.setVisibility(View.INVISIBLE);
        endzeit.setVisibility(View.INVISIBLE);
        enddatum.setVisibility(View.INVISIBLE);
        txtEnde.setVisibility(View.INVISIBLE);
        txtStart.setText(R.string.wann);
    }
    public void allesSichtbarmachen() {
        startZeit.setVisibility(View.VISIBLE);
        endzeit.setVisibility(View.VISIBLE);
        enddatum.setVisibility(View.VISIBLE);
        txtEnde.setVisibility(View.VISIBLE);
        txtStart.setText(R.string.start);
    }

    /*-------------------------private Methoden---------------------------------------------------*/

    private void initialisieren() {
        titel = findViewById(R.id.txt_Terminerstellung_Eingabe_Titel);
        ganztaegigJaNein = findViewById(R.id.switch_Terminerstellung_Eingabe_JaNein);
        startDatum = findViewById(R.id.txt_Terminerstellung_Eingabe_Datum_Start);
        startZeit = findViewById(R.id.txt_Terminerstellung_Eingabe_Zeit_Start);
        enddatum = findViewById(R.id.txt_Terminerstellung_Eingabe_Datum_Ende);
        endzeit = findViewById(R.id.txt_Terminerstellung_Eingabe_Zeit_Ende);
        speichern = findViewById(R.id.btn_Terminerstellung_Speichern);
        abbruch = findViewById(R.id.btn_Terminerstellung_Abbrechen);
        txtEnde = findViewById(R.id.txt_Terminerstellung_Beschriftung_Ende);
        txtStart = findViewById(R.id.txt_Terminerstellung_Beschriftung_Start);

        txtUeberpruefung = findViewById(R.id.txt_Gespeichert);

        strg = new TerminErstellung_Steuerung(this);
        dataSource = new TermineDataSource(this);

    }
    private void faerben(){
        int farbe = Color.parseColor(Kalender_Steuerung.FARBEN[Kalender_Steuerung.kalender.get(Calendar.MONTH)]);
        titel.setBackgroundColor(farbe);
        txtStart.setTextColor(farbe);
        txtEnde.setTextColor(farbe);
        speichern.setBackgroundColor(farbe);
        abbruch.setBackgroundColor(farbe);
    }

    private void setztenDerOnClickListener() {
        abbruch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strg.abbruchGeklickt();
            }
        });
        speichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strg.speichernGeklickt();
            }
        });
        startDatum.setOnClickListener(this);
        enddatum.setOnClickListener(this);
        startZeit.setOnClickListener(this);
        endzeit.setOnClickListener(this);
        ganztaegigJaNein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { strg.switchGanztaegigGeklickt();
            }
        });
        titel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                strg.titelEingegeben(s);
            }
        });
    }

    /*-------------------------Überschriebene Methode---------------------------------------------*/
    @Override
    public void onClick(View v) {
        try{
            strg.datumZeitAngabeGecklickt(v);
        }catch(IllegalStateException e){

        }

    }
    @Override
    public void onDateSet(DatePicker view, int jahr, int monat, int tag) {
        strg.datumGeklickt(jahr, monat, tag);
    }
    @Override
    public void onTimeSet(TimePicker view, int stunde, int minute) {
        strg.zeitGeklickt(stunde, minute);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Erstellung
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termin_erstellung);

        initialisieren();
        setztenDerOnClickListener();
        faerben();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Ender der Klasse

}
