package de.projects.janap.a05_kalender;

import android.graphics.Color;
import android.nfc.Tag;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TerminErstellung_Steuerung {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Attribute
    private TerminErstellung_GUI dieGui;
    private Calendar kalender, start, ende;
    private DatumAuswaehlen datumAuswaehlerDialogFragment;
    private ZeitAuswaehlen zeitAuswaehlenFragment;

    private Boolean titelVorhanden = false;
    private Boolean startDatumVorhanden = false;
    private Boolean startZeitVorhanden = false;
    private Boolean enddatumVorhanden = false;
    private Boolean endzeitVorhanden = false;
    private Boolean ganztaegig = false;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Konstruktor
    public TerminErstellung_Steuerung(TerminErstellung_GUI pGui){
        dieGui = pGui;
        initialisieren();
        setzenDerBeispielhaftenDaten();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Methoden

    /*-------------------------Die On-Click Methoden----------------------------------------------*/
    public void abbruchGeklickt(){
        dieGui.oeffneActivityKalenderMonatsUebersicht();
    }
    public void speichernGeklickt(){
        Boolean etwasFehlt = terminDatenVollstaendigUeberpruefen();
        Boolean irgendwasIstFalsch = terminDatenAufFehlerUeberpruefen();
        if (!etwasFehlt) {
            if (!irgendwasIstFalsch){
                terminDatenSpeichern();

            }
        }
    }
    public void switchGanztaegigGeklickt(){
        if (ganztaegig) {
            ganztaegig = false;
            dieGui.allesSichtbarmachen();
        } else if (!ganztaegig) {
            ganztaegig = true;
            dieGui.lasseUnwichtigesVerschwinde();
        }
    }
    public void titelEingegeben(Editable s){
        if(s.length() > 0){
            titelVorhanden = true;
        }

    }
    public void datumZeitAngabeGecklickt(View v){
        try {
            int id = v.getId();
            if (id == R.id.txt_Terminerstellung_Eingabe_Datum_Start) {
                datumAuswaehlerDialogFragment.setKennzeichnung(DatumAuswaehlen.KENNZEICHNUNG_START_DATUM);
                datumAuswaehlerDialogFragment.show(dieGui.getSupportFragmentManager(), "Datum auswählen");
            } else if (id == R.id.txt_Terminerstellung_Eingabe_Datum_Ende) {
                datumAuswaehlerDialogFragment.setKennzeichnung(DatumAuswaehlen.KENNZEICHNUNG_ENDE_DATUM);
                datumAuswaehlerDialogFragment.show(dieGui.getSupportFragmentManager(), "Datum auswählen");
            } else if (id == R.id.txt_Terminerstellung_Eingabe_Zeit_Start) {
                zeitAuswaehlenFragment.setKennzeichnung(ZeitAuswaehlen.KENNZEICHNUNG_START_ZEIT);
                zeitAuswaehlenFragment.show(dieGui.getSupportFragmentManager(), "Zeit auswählen");
            } else if (id == R.id.txt_Terminerstellung_Eingabe_Zeit_Ende) {
                zeitAuswaehlenFragment.setKennzeichnung(ZeitAuswaehlen.KENNZEICHNUNG_ENDE_ZEIT);
                zeitAuswaehlenFragment.show(dieGui.getSupportFragmentManager(), "Zeit auswählen");
            }
        }catch (IllegalStateException e){}
    }
    public void datumGeklickt(int jahr, int monat, int tag){
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        if (datumAuswaehlerDialogFragment.getKennzeichnung() == DatumAuswaehlen.KENNZEICHNUNG_START_DATUM) {
            start.set(jahr, monat, tag);
            dieGui.getStartDatum().setText(format.format(start.getTime()));
            startDatumVorhanden = true;
        } else if (datumAuswaehlerDialogFragment.getKennzeichnung() == DatumAuswaehlen.KENNZEICHNUNG_ENDE_DATUM) {
            ende.set(jahr, monat, tag);
            dieGui.getEnddatum().setText(format.format(ende.getTime()));
            enddatumVorhanden = true;
        }
    }
    public void zeitGeklickt(int stunde, int minute){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        if (zeitAuswaehlenFragment.getKennzeichnung() == ZeitAuswaehlen.KENNZEICHNUNG_START_ZEIT) {

            start.set(Calendar.HOUR_OF_DAY, stunde);
            start.set(Calendar.MINUTE, minute);

            dieGui.getStartZeit().setText(format.format(start.getTime()));
            startZeitVorhanden = true;

        } else if (zeitAuswaehlenFragment.getKennzeichnung() == ZeitAuswaehlen.KENNZEICHNUNG_ENDE_ZEIT) {

            ende.set(Calendar.HOUR_OF_DAY, stunde);
            ende.set(Calendar.MINUTE, minute);

            dieGui.getEndzeit().setText(format.format(ende.getTime()));
            endzeitVorhanden = true;
        }
    }

    /*-------------------------public Methoden----------------------------------------------------*/
    public void setzenDerBeispielhaftenDaten(){
        dieGui.getStartDatum().setHint(new SimpleDateFormat("dd.MM.yyyy").format(kalender.getTime()));
        dieGui.getEnddatum().setHint(new SimpleDateFormat("dd.MM.yyyy").format(kalender.getTime()));

        dieGui.getStartZeit().setHint(new SimpleDateFormat("HH:mm").format(kalender.getTime()));
        kalender.add(Calendar.MINUTE, 1);
        dieGui.getEndzeit().setHint(new SimpleDateFormat("HH:mm").format(kalender.getTime()));
    }

    /*-------------------------private Methoden---------------------------------------------------*/
    private void initialisieren(){
        kalender = Kalender_Steuerung.kalender;
        start = Calendar.getInstance();
        ende = Calendar.getInstance();

        datumAuswaehlerDialogFragment = new DatumAuswaehlen();
        zeitAuswaehlenFragment = new ZeitAuswaehlen();
    }
    private Boolean terminDatenAufFehlerUeberpruefen(){
        Boolean fehlerVorhanden;

        if (ganztaegig){
            fehlerVorhanden = false;

        }else{
            long zeitInMiliStart = start.getTimeInMillis();
            long zeitInMiliEnde = ende.getTimeInMillis();

            if(zeitInMiliStart > zeitInMiliEnde){
                fehlerVorhanden = true;
                Toast toast = Toast.makeText(dieGui, R.string.toastDatenFalsch, Toast.LENGTH_SHORT);
                toast.show();
            }else{
                fehlerVorhanden = false;
            }
        }
        return fehlerVorhanden;
    }
    private Boolean terminDatenVollstaendigUeberpruefen() {
        Boolean min1DatenFehlen = false;
        if (!titelVorhanden) {
            dieGui.getTitel().setHintTextColor(Color.parseColor("#FFB60003"));
            min1DatenFehlen = true;
        }
        if (!startDatumVorhanden){
            dieGui.getStartDatum().setHintTextColor(Color.parseColor("#FFB60003"));
            min1DatenFehlen = true;
        }
        if (!ganztaegig){
            if (!startZeitVorhanden){
                dieGui.getStartZeit().setHintTextColor(Color.parseColor("#FFB60003"));
                min1DatenFehlen = true;
            }
            if (!enddatumVorhanden){
                dieGui.getEnddatum().setHintTextColor(Color.parseColor("#FFB60003"));
                min1DatenFehlen = true;
            }
            if (!endzeitVorhanden){
                dieGui.getEndzeit().setHintTextColor(Color.parseColor("#FFB60003"));
                min1DatenFehlen = true;
            }
        }
        if (min1DatenFehlen){
            Toast toast = Toast.makeText(dieGui, R.string.toastInfoFehlt, Toast.LENGTH_SHORT);
            toast.show();
        }
        return min1DatenFehlen;
    }
    private void terminDatenSpeichern() {

        Termin neuerTermin = null;
        dieGui.getDataSource().open();

        if (ganztaegig){
            start.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH), 0,0);
            ende.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH), 0,0);
            //neuerTermin = new Termin(Termin.LETZTE_ID, dieGui.getTitel().getText().toString(),start, ende, true); //Starttag und Endtag sind bei ganztaegig gleich
            dieGui.getDataSource().erstelleTermin(dieGui.getTitel().getText().toString(),start, ende, "true");
        }else if(!ganztaegig){
            //neuerTermin = new Termin(Termin.LETZTE_ID, dieGui.getTitel().getText().toString(),start, ende, false);
            dieGui.getDataSource().erstelleTermin(dieGui.getTitel().getText().toString(),start, ende, "false");

        }

        dieGui.getDataSource().close();
        Toast toast = Toast.makeText(dieGui, R.string.toastTerminGespeichert, Toast.LENGTH_SHORT);
        toast.show();
        dieGui.oeffneActivityKalenderMonatsUebersicht();


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Ender der Klasse
}
