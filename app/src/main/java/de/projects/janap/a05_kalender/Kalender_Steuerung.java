package de.projects.janap.a05_kalender;

import android.graphics.Color;
import android.nfc.Tag;
import android.view.View;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Kalender_Steuerung {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Attribute
    private Kalender_GUI dieGui;

    /*-------------------------Attribute für den Kalender-----------------------------------------*/
    public static Calendar kalender;
    public static Calendar heute;
    private ArrayList<Calendar> tage = new ArrayList<>();
    private String[] bezeichnungen = new String[11];
    public static String[] FARBEN = new String[11];

    /*-------------------------Attribute für die Termine-----------------------------------------*/
    public static ArrayList<Termin> termine;
    ArrayList<Termin> termineDesTages;

    /*-------------------------Schlüssel----------------------------------------------------------*/
    public static final String LETZTER_MONAT_UEBERGABE = "LetzterMonatÜbergabe";
    public static final String LETZTES_JAHR_UEBERGABE = "LetztesJahrÜbergabe";

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Konstruktor

    public Kalender_Steuerung(Kalender_GUI pGui){
        dieGui = pGui;
        initialisieren();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Methoden
    public ArrayList<Termin> getTermineDesTages(){
        return termineDesTages;
    }
    /*-------------------------Die On-Click Methoden----------------------------------------------*/
    public void btnZuvorGeklickt(){
        kalender.add(Calendar.MONTH, -1);    //der Monat des Kalenders wird um eins reduziert
        aktualisiereKalender(); //der Monat wird mit den momentanen Daten des Kalenders dargestellt
    }
    public void btnWeiterGeklickt(){
        kalender.add(Calendar.MONTH, 1); //der Monat des Kalenders wird um eins addiert
        aktualisiereKalender(); //der Monat wird mit den momentanen Daten des Kalenders dargestellt
    }
    public void txtHeutigerTagGeklickt() {
        kalender.set(Calendar.YEAR, heute.get(Calendar.YEAR));
        kalender.set(Calendar.MONTH, heute.get(Calendar.MONTH));
        aktualisiereKalender();
        dieGui.setTxtMomentanesDatum(new SimpleDateFormat("dd.MM.yyyy").format(heute.getTime()));
    }
    public void btnNeuerTerminGeklickt() {
        dieGui.oeffneActifityNeuerTermin();
    }
    public void einTagInDerTabelleDesAktuellenMonatsGeklickt(View view, int position) {
        if (tage.get(position) != null) {
            if (dieGui.getAltesView() == null) {
                dieGui.setAltesView(view);
            }
            Calendar tag = tage.get(position);
            kalender.set(Calendar.DAY_OF_MONTH, tag.get(Calendar.DAY_OF_MONTH));
            dieGui.setTxtMomentanesDatum(new SimpleDateFormat("dd.MM.yyyy").format(kalender.getTime()));
            dieGui.getAltesView().setBackgroundColor(Color.parseColor("#ffffff"));
            view.setBackgroundColor(Color.parseColor("#E7E7E7"));
            dieGui.setAltesView(view);

        }
    }
    public void einTagInDerTabelleDesAktuellenMonatsLangGeklickt(View view, int position, long id) {
        if (tage.get(position) != null) {
            dieGui.getTabelleAktuellerMonat().performItemClick(view, position, id);
            dieGui.getNeuerTermin().performClick();
        }
    }


    public void einTerminWurdeGeklickt(int pPosition, long pId, ListView pTermineListView){
        Termin termin = (Termin) pTermineListView.getItemAtPosition(pPosition);
        dieGui.getDataSource().loescheTermin(termin);
        zeigeAlleTermineAn();
        aktualisiereKalender();

    }

    /*-------------------------public Methoden----------------------------------------------------*/
    public void aktualisiereKalender() {
        setztenDesMonatsArray();
        List<Termin> terminListe = dieGui.getDataSource().getAlleTermine();
        Kalender_Adapter adapterAktuellerMonat = new Kalender_Adapter(dieGui, tage, dieGui, terminListe); //KalenderAdapter um den Kalender in der Tabelle darzustellen

        dieGui.setTxtMonatAnzeige(bezeichnungen[kalender.get(Calendar.MONTH)]);  //setzt die neue Monatsbezeichnung fest

        dieGui.setTxtMomentanesDatum("" + (kalender.get(Calendar.YEAR)));
        dieGui.getTabelleAktuellerMonat().setAdapter(adapterAktuellerMonat);    //Kalender wird dargestellt

        dieGui.aendereFarbe(kalender.get(Calendar.MONTH));
    }

    public void zeigeAlleTermineAn() {
        List<Termin> terminListe = dieGui.getDataSource().getAlleTermine();
        Termine_Adapter termineAdapter = new Termine_Adapter(dieGui, terminListe);

        dieGui.getListViewTermineDesTages().setAdapter(termineAdapter);
    }



    /*-------------------------private Methoden---------------------------------------------------*/

    private void setztenDesMonatsArray(){
        kalender.set(Calendar.DAY_OF_MONTH, 1);
        int ersterTag = kalender.get(Calendar.DAY_OF_WEEK)-2;   //da 1. Position 0 ist und - dem Tag
        tage = new ArrayList<>();
        int datum = 1;
        for (int i = 0; i < 42; i++) {

            /*--------------------Fehlerbehebung--------------------------------------------------*/
            if (i == 0){
                if (kalender.get(Calendar.DAY_OF_WEEK) == 0){
                    datum = 1;  //Da Fehler mit dem ersten des Monats auftrat
                }
                if (ersterTag == -1){
                    ersterTag = 6;
                }
                if (ersterTag == 7){
                    ersterTag = 0;
                }
            }
            /*--------------------Array füllen----------------------------------------------------*/
            if ( (i >= ersterTag) && !(i >= kalender.getActualMaximum(Calendar.DAY_OF_MONTH)+ ersterTag) ){    //wenn die aktuelle Position groeßer ist als der erste Tag des Monats & wenn die position groeßer ist als das Ende des Monats
                Calendar tag = Calendar.getInstance();
                tag.set(kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), datum, 0,0);
                datum++;
                tage.add(tag);
            }else{
                tage.add(null);
            }
        }
    }
    private void initialisieren(){
        bezeichnungen = dieGui.getResources().getStringArray(R.array.monate);
        FARBEN = dieGui.getResources().getStringArray(R.array.farben);
        heute = Calendar.getInstance();
        termine = new ArrayList<>();
        kalender = Calendar.getInstance();
        kalender.set(Calendar.DAY_OF_MONTH, 1);

        dieGui.setTxtHeutigerTag(new SimpleDateFormat("dd.MM.yyyy").format(heute.getTime()));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Ender der Klasse
}
