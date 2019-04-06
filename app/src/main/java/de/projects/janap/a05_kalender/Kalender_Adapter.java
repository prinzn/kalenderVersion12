package de.projects.janap.a05_kalender;

import android.content.Context;
import android.graphics.Color;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Kalender_Adapter extends ArrayAdapter<Calendar> {

    //Darstellung eines Monats
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Attribute
    private LayoutInflater inflater;
    private Kalender_GUI strg;
    private List<Termin> terminListe;
    private Boolean notAgain = false;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Konstruktor
    public Kalender_Adapter(Context pContext, ArrayList<Calendar> pTage, Kalender_GUI pStrg, List pTerminListe){
        super(pContext, R.layout.zelle_aktueller_monat, pTage);
        inflater = LayoutInflater.from(pContext);
        strg = pStrg;
        terminListe = pTerminListe;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Methoden

    private void termineAnzeigen(TextView terminAnzeige1,TextView terminAnzeige2,TextView terminAnzeige3,TextView terminAnzeige4, String pFarbe){
        //Anzeige der Termine
                    if (terminAnzeige1.getVisibility() != View.VISIBLE) {
                        // bisher kein Termin an diesem Tag
                        terminAnzeige1.setVisibility(View.VISIBLE);
                        terminAnzeige1.setBackgroundColor(Color.parseColor(pFarbe)); //bei einem Termin soll es eingefärbt werden

                    } else if (terminAnzeige1.getVisibility() == View.VISIBLE
                            && terminAnzeige2.getVisibility() != View.VISIBLE) {
                        // bisher ein Termin an diesem Tag
                        terminAnzeige2.setVisibility(View.VISIBLE);
                        terminAnzeige2.setBackgroundColor(Color.parseColor(pFarbe)); //bei einem Termin soll es eingefärbt werden

                    } else if (terminAnzeige1.getVisibility() == View.VISIBLE
                            && terminAnzeige2.getVisibility() == View.VISIBLE
                            && terminAnzeige3.getVisibility() != View.VISIBLE) {
                        // bisher zwei Termine an diesem Tag
                        terminAnzeige3.setVisibility(View.VISIBLE);
                        terminAnzeige3.setBackgroundColor(Color.parseColor(pFarbe)); //bei einem Termin soll es eingefärbt werden

                    } else if (terminAnzeige1.getVisibility() == View.VISIBLE
                            && terminAnzeige2.getVisibility() == View.VISIBLE
                            && terminAnzeige3.getVisibility() == View.VISIBLE
                            && terminAnzeige4.getVisibility() != View.VISIBLE) {
                        // bisher drei Termine an diesem Tag
                        terminAnzeige4.setVisibility(View.VISIBLE);
                        terminAnzeige4.setBackgroundColor(Color.parseColor(pFarbe)); //bei einem Termin soll es eingefärbt werden
                    }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        try {
            Calendar tag = getItem(position);    //Aus dem Array der diesen Monat mit den jeweiligen Tagen beschreibt

            //wenn covertView = 0 ist, wird eine neue Zelle festgelegt
            if (view == null) {
                view = inflater.inflate(R.layout.zelle_aktueller_monat, parent, false);
            }

            TextView terminAnzeige1 = view.findViewById(R.id.txt_Termin1);
            TextView terminAnzeige2 = view.findViewById(R.id.txt_Termin2);
            TextView terminAnzeige3 = view.findViewById(R.id.txt_Termin3);
            TextView terminAnzeige4 = view.findViewById(R.id.txt_Termin4);

            TextView textView = view.findViewById(R.id.textview_tag);
            textView.setText(String.valueOf(tag.get(Calendar.DAY_OF_MONTH)));  //Das TextView bekommt die Zahl des jeweiligen Tages (tag weiß den Tag des Monats, durch Festlegung in der KalenderSteuerung) zugeteilt

            //Überprüfung ob es sich um den heutigen Tag handelt
            if ((tag.get(Calendar.DAY_OF_MONTH) == Kalender_Steuerung.heute.get(Calendar.DAY_OF_MONTH))
                    && (tag.get(Calendar.MONTH) == Kalender_Steuerung.heute.get(Calendar.MONTH))
                    && (tag.get(Calendar.YEAR) == Kalender_Steuerung.heute.get(Calendar.YEAR))) {
                String aktuelleFarbedesMonats = Kalender_Steuerung.FARBEN[Kalender_Steuerung.kalender.get(Calendar.MONTH)];
                textView.setTextColor(Color.parseColor(aktuelleFarbedesMonats));  //beim heutigen Tag soll es eingefärbt werden
            }

            //Überprüfung ob es Termine an diesem Tag gibt
            Termin termin;

            for (int i = 0; i < terminListe.size(); i++) {
                termin = terminListe.get(i);



                if (notAgain == false){
                    //Termin Start und Ende in einem Monat
                    if ( (tag.get(Calendar.MONTH) == termin.getStart().get(Calendar.MONTH) && tag.get(Calendar.YEAR) == termin.getStart().get(Calendar.YEAR)) && tag.get(Calendar.DAY_OF_MONTH) >= termin.getStart().get(Calendar.DAY_OF_MONTH)
                            && (tag.get(Calendar.MONTH) == termin.getEnde().get(Calendar.MONTH) && tag.get(Calendar.YEAR) == termin.getEnde().get(Calendar.YEAR)) && tag.get(Calendar.DAY_OF_MONTH) <= termin.getEnde().get(Calendar.DAY_OF_MONTH)){

                        termineAnzeigen(terminAnzeige1, terminAnzeige2, terminAnzeige3, terminAnzeige4, "#4FC3F7");
                        Log.d("Termin", termin.toString() + " - Termin angezeigt - " + tag.get(Calendar.DAY_OF_MONTH) + " - " +i);

                    }
                    //Termin Start in anderem Monat als das Ende
                    else if ((tag.get(Calendar.MONTH) == termin.getStart().get(Calendar.MONTH) && tag.get(Calendar.YEAR) == termin.getStart().get(Calendar.YEAR)) && tag.get(Calendar.DAY_OF_MONTH) >= termin.getStart().get(Calendar.DAY_OF_MONTH)
                            && (tag.get(Calendar.MONTH) != termin.getEnde().get(Calendar.MONTH))){

                        termineAnzeigen(terminAnzeige1, terminAnzeige2, terminAnzeige3, terminAnzeige4, "#4DB6AC");

                    }
                    //Termin Ende in anderem Monat als der Start
                    else if ((tag.get(Calendar.MONTH) == termin.getEnde().get(Calendar.MONTH) && tag.get(Calendar.YEAR) == termin.getEnde().get(Calendar.YEAR)) && tag.get(Calendar.DAY_OF_MONTH) <= termin.getEnde().get(Calendar.DAY_OF_MONTH)
                            && (tag.get(Calendar.MONTH) != termin.getStart().get(Calendar.MONTH))){


                        termineAnzeigen(terminAnzeige1, terminAnzeige2, terminAnzeige3, terminAnzeige4, "#81C784");
                    }

                    //Termin Start und Ende in anderem Monat  -> Termin geht jeden Tag in diesem Monat
                    else if ((tag.get(Calendar.MONTH) > termin.getStart().get(Calendar.MONTH) && tag.get(Calendar.MONTH) < termin.getEnde().get(Calendar.MONTH))
                            && (tag.get(Calendar.YEAR) >= termin.getStart().get(Calendar.YEAR) && tag.get(Calendar.YEAR) <= termin.getEnde().get(Calendar.YEAR))){

                        termineAnzeigen(terminAnzeige1, terminAnzeige2, terminAnzeige3, terminAnzeige4, "#AED581");
                    }

                    Log.d("Termin", "fertig ueberpreuft" + position + " - " + tag.get(Calendar.DAY_OF_MONTH) + " - ");


                }


                if (position != 0 && tag.get(Calendar.DAY_OF_MONTH) != 1){
                    notAgain = true;
                }


            }

        return view;

    } catch (Exception e) {
        e.printStackTrace();
    }

     return view;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Ende der Klasse
}
