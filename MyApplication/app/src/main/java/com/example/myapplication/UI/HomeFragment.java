package com.example.myapplication.UI;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.MeteoAdapter;
import com.example.myapplication.Model.DonneesMeteo;
import com.example.myapplication.R;

import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static final int GEOLOCATION_PERMISSION_REQUEST_CODE = 42;

    MeteoAdapter Adapter;
    ListView ListViewDays;
    SwipeRefreshLayout Refresher;
    TextView WelcomeTextView;
    DonneesMeteo Meteo;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        WelcomeTextView = v.findViewById(R.id.textviewWelcome);
        Refresher = v.findViewById(R.id.swiperefreshlayout);
        ListViewDays = v.findViewById(R.id.listview);

        String sUserLogin = getActivity().getPreferences(Context.MODE_PRIVATE)
                .getString("connectedUserLogin", "");

        WelcomeTextView.setText("Salut " + sUserLogin + " !! Voici la meteo de la semaine :");

        //Instancie l'adapter et l'associe a la listview
        Adapter = new MeteoAdapter(getActivity(), 0);
        ListViewDays.setAdapter(Adapter);

        //Lorsque Rafraichi manuellement la page
        Refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });

        Refresher.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateData();
            }
        }, 0);

        ListViewDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("dayToShow", Adapter.getItem(position).getJson());
                getActivity().startActivity(intent);
            }
        });

        //Manage geolocalisation
        manageGeolocalisation();

        return v;
    }

    private void updateData(){
        Refresher.setRefreshing(true);

        String sURL = "https://www.prevision-meteo.ch/services/json/grenoble";

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.GET, sURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jSon = response;
                        /*Meteo = new Gson().fromJson(jSon, DonneesMeteo.class);
                        Meteo.setJsonString(jSon);*/

                        try {
                            Meteo = new DonneesMeteo(jSon);
                        }
                        catch (JSONException e){
                            String sErrMsg = "Erreur durant la desserialisation du Json";

                            if (jSon.contains("City or coordinate not found"))
                                sErrMsg = "Ville inconnue. Merci d'en saisir une autre !";

                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Erreur")
                                    .setMessage(sErrMsg)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //nothing to do
                                        }
                                    })
                            .show();
                        }

                        //Peupler la Listview
                        Adapter.addAll(Meteo.getJoursSemaines());

                        //Cacher le refresher
                        Refresher.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ;
                    }
                }
        );

        queue.add(request);
    }


    public void updateDataWithGeolocation(){

        Refresher.setRefreshing(true);

        Location currentUserLocation = null;

        try {
            LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            //Si le gps est actif
            if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                currentUserLocation = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            //Si le network est actif
            if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                Location networkLoc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                //Verifie si la localisation network est plus precise que le GPS et retient la plus precise.
                if (networkLoc.hasAccuracy() &&
                        currentUserLocation.hasAccuracy() &&
                        networkLoc.getAccuracy() < currentUserLocation.getAccuracy())
                    currentUserLocation = networkLoc;
            }
        }
        catch (SecurityException e){
            ;// Nothing to do : no new location, no refresh needed.
        }



        String sURL = "https://www.prevision-meteo.ch/services/json/" +
                "lat=" + currentUserLocation.getLatitude() +
                "lng=" + currentUserLocation.getLongitude();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.GET, sURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String jSon = response;
                        /*Meteo = new Gson().fromJson(jSon, DonneesMeteo.class);
                        Meteo.setJsonString(jSon);*/

                        try {
                            Meteo = new DonneesMeteo(jSon);
                        }
                        catch (JSONException e){
                            String sErrMsg = "Erreur durant la desserialisation du Json";

                            if (jSon.contains("City or coordinate not found"))
                                sErrMsg = "Ville inconnue. Merci d'en saisir une autre !";

                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Erreur")
                                    .setMessage(sErrMsg)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //nothing to do
                                        }
                                    })
                                    .show();
                        }

                        //Peupler la Listview
                        Adapter.addAll(Meteo.getJoursSemaines());

                        //Cacher le refresher
                        Refresher.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ;
                    }
                }
        );

        queue.add(request);
    }


    private void manageGeolocalisation(){

        boolean bHasPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        bHasPermission = bHasPermission && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!bHasPermission){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Géolocalisation")
                    .setMessage("Pour fonctionner correctement, l'application a besoin d'accéder au GPS de votre téléphone.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Affiche la fenêtre système pour demander l'autorisation à l'utilisateur
                            //Le resultat sera catché par l'activité, avec le request code GEOLOCATION_PERMISSION_REQUEST_CODE
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                    GEOLOCATION_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .show();
        }
        else
        {
            updateDataWithGeolocation();
        }
    }
}
