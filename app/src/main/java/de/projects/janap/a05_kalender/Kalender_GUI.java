package de.projects.janap.a05_kalender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Kalender_GUI extends AppCompatActivity {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Attribute

    /*-------------------------Darstellung--------------------------------------------------------*/
    private TextView txtMonatAnzeige, txtMomentanesDatum;
    private TextView txtMo, txtDi, txtMi, txtDo, txtFr, txtSa, txtSo;
    private ListView listViewTermineDesTages;

    private TextView neuerTermin;
    private GridView tabelleAktuellerMonat;
    private Button txtHeutigerTag;
    private ImageButton btnZuvor, btnWeiter;
    private RelativeLayout monatWechseln;

    /*-------------------------Andere Variablen---------------------------------------------------*/
    private Kalender_Steuerung strg;
    private View altesView;

    private TermineDataSource dataSource;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Methoden

    /*-------------------------Get Methoden-------------------------------------------------------*/
    public ListView getListViewTermineDesTages() {
        return listViewTermineDesTages;
    }
    public GridView getTabelleAktuellerMonat() {
        return tabelleAktuellerMonat;
    }
    public View getAltesView() {
        return altesView;
    }
    public TextView getNeuerTermin(){return neuerTermin;}
    public TermineDataSource getDataSource(){
        return dataSource;
    }

    /*-------------------------Set Methoden-------------------------------------------------------*/
    public void setTxtMonatAnzeige(String pTxtMonatAnzeige) {
        this.txtMonatAnzeige.setText(pTxtMonatAnzeige);
    }
    public void setTxtHeutigerTag(String pTxtHeutigerTag) {
        this.txtHeutigerTag.setText(pTxtHeutigerTag);
    }
    public void setTxtMomentanesDatum(String pTxtMomentanesDatum) {
        this.txtMomentanesDatum.setText(pTxtMomentanesDatum);
    }
    public void setListViewTermineDesTages(ListView pTermineDesTages) {
        this.listViewTermineDesTages = pTermineDesTages;
    }
    public void setAltesView(View pAltesView) {
        this.altesView = pAltesView;
    }


    /*-------------------------public Methoden----------------------------------------------------*/
    public void oeffneActifityNeuerTermin(){
        Intent intent = new Intent(this, TerminErstellung_GUI.class);
        startActivity(intent);
    }
    public void aendereFarbe(int pMonat) {
        //Umfärben, da jeder Monat eine eigene Farbe hat
        int farbe = Color.parseColor(Kalender_Steuerung.FARBEN[pMonat]);

        txtMomentanesDatum.setBackgroundColor(farbe);
        listViewTermineDesTages.setBackgroundColor(farbe);
        monatWechseln.setBackgroundColor(farbe);
        txtHeutigerTag.setTextColor(farbe);

        txtMo.setTextColor(farbe);
        txtDi.setTextColor(farbe);
        txtMi.setTextColor(farbe);
        txtDo.setTextColor(farbe);
        txtFr.setTextColor(farbe);
        txtSa.setTextColor(farbe);
        txtSo.setTextColor(farbe);
    }

    /*-------------------------private Methoden---------------------------------------------------*/
    private void initialisieren(){
        tabelleAktuellerMonat = findViewById(R.id.gridView_Kalender_Tabelle_AktuellerMonat);
        listViewTermineDesTages = findViewById(R.id.listView_Termine);

        txtMonatAnzeige = findViewById(R.id.txt_Kalender_Monat);
        txtHeutigerTag = findViewById(R.id.txt_Kalender_HeutigerTag);
        txtMomentanesDatum = findViewById(R.id.txt_Kalender_Momentanes_Datum);
        btnZuvor = findViewById(R.id.btn_Kalender_Zuvor);
        btnWeiter = findViewById(R.id.btn_Kalender_Weiter);
        neuerTermin = findViewById(R.id.txt_neuerTermin);
        monatWechseln = findViewById(R.id.layoutMonatWechseln);

        txtMo = findViewById(R.id.txt_Kalender_Mo);
        txtDi = findViewById(R.id.txt_Kalender_Di);
        txtMi = findViewById(R.id.txt_Kalender_Mi);
        txtDo = findViewById(R.id.txt_Kalender_Do);
        txtFr = findViewById(R.id.txt_Kalender_Fr);
        txtSa = findViewById(R.id.txt_Kalender_Sa);
        txtSo = findViewById(R.id.txt_Kalender_So);

        strg = new Kalender_Steuerung(this);
        dataSource  = new TermineDataSource(this);
    }


    private void setztenDerOnClickListener(){
        btnZuvor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strg.btnZuvorGeklickt();
            }
        });

        btnWeiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strg.btnWeiterGeklickt();
            }
        });

        txtHeutigerTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strg.txtHeutigerTagGeklickt();
            }
        });

        neuerTermin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strg.btnNeuerTerminGeklickt();
            }
        });

        tabelleAktuellerMonat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                strg.einTagInDerTabelleDesAktuellenMonatsGeklickt(view, position);
            }
        });

        tabelleAktuellerMonat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                strg.einTagInDerTabelleDesAktuellenMonatsLangGeklickt(view, position, id);
                return true;
            }
        });

        listViewTermineDesTages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ListView termineListView = (ListView) findViewById(R.id.listView_Termine);
                strg.einTerminWurdeGeklickt(position, id, termineListView);
            }
        });


    }

    /*-------------------------andere Methoden----------------------------------------------------*/
    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
        strg.aktualisiereKalender();
        strg.zeigeAlleTermineAn();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }
    @Override
    public void onBackPressed() {
        finish();
    } //App wird beendet wenn die Rücktaste benutzt wurde

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Erstellung
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalender__gui);

        initialisieren();   //Allen Variablen wird ihr Wert zugeordnet

        dataSource.open();
        setztenDerOnClickListener();
        strg.aktualisiereKalender();     //der Monat wird mit den momentanen Daten des Kalenders dargestellt
        txtMomentanesDatum.setText(new SimpleDateFormat("dd.MM.yyyy").format(strg.heute.getTime()));

        try {
            try {
                Kalender_Steuerung.kalender.set(Calendar.MONTH,Integer.parseInt(getIntent().getExtras().getString(Kalender_Steuerung.LETZTER_MONAT_UEBERGABE)));
                Kalender_Steuerung.kalender.set(Calendar.YEAR,Integer.parseInt(getIntent().getExtras().getString(Kalender_Steuerung.LETZTES_JAHR_UEBERGABE)));
                strg.aktualisiereKalender();
            }catch (NumberFormatException s){}

        }catch (NullPointerException e){}



        strg.zeigeAlleTermineAn();
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Ende der Klasse
}

