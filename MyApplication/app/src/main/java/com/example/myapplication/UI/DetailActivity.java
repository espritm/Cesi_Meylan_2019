package com.example.myapplication.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Model.ForcastDay;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    ForcastDay DayToShow;
    ImageView imgViewMeteo;
    TextView txtviewDate ;
    TextView txtvDayName ;
    TextView txtviewTmin ;
    TextView txtvDayTmax ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Display back arrow in navigation bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String jsonDayToShow = getIntent().getStringExtra("dayToShow");

        if (jsonDayToShow == null){
            showErrrorMsgAndFinishActivity();
        }

        try {
            DayToShow = new ForcastDay(jsonDayToShow);
        }
        catch (JSONException e){
            showErrrorMsgAndFinishActivity();
        }

        //update UI here//Retrouver les références de nos vues avec findViewByID
        imgViewMeteo = findViewById(R.id.imgMeteo);
        txtviewDate = findViewById(R.id.txtViewDayDate);
        txtvDayName = findViewById(R.id.txtViewDayName);
        txtviewTmin = findViewById(R.id.txtViewTmin);
        txtvDayTmax = findViewById(R.id.txtViewTmax);

        //Configurer l'interface avec les info du FcstDay à afficher.
        Picasso.get().load(DayToShow.getIcon()).into(imgViewMeteo);
        txtvDayName.setText(DayToShow.getDay_long());
        txtviewDate.setText(DayToShow.getDate());
        txtviewTmin.setText(String.valueOf(DayToShow.getTmin()) + "°C");
        txtvDayTmax.setText(DayToShow.getTmax() + "°C");
    }

    private void showErrrorMsgAndFinishActivity(){
        new AlertDialog.Builder(this)
                .setTitle("Erreur")
                .setMessage("Données introuvables, merci de réessayer...")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DetailActivity.this.finish();
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_detail_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == R.id.menu_share){
            shareOnSocialNetwork();
        }

        //Si on a cliqué la flèche de retour dans la ToolBar
        if (item.getItemId() == android.R.id.home) {
            //finish();
            onBackPressed();
        }

        return true;
    }

    private void shareOnSocialNetwork() {

        //Paramètrer l'intent
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");

        //Ajouter le texte qu'on souhaite partager
        intent.putExtra(Intent.EXTRA_TEXT,
                DayToShow.getDay_long() + " il fera " +
                        DayToShow.getCondition() + " et les températures iront jusqu'à " +
                        DayToShow.getTmax() + "°C !");

        //Démarre l'Intent et offre la possibilité à l'utilisateur de choisir
        //son appli préférée
        startActivity(Intent.createChooser(intent, "Partager la météo"));
    }
}
