package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Model.ForcastDay;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

public class MeteoAdapter  extends ArrayAdapter<ForcastDay> {

    private final ArrayList<ForcastDay> m_lsItems;

    public MeteoAdapter(@NonNull Context context, int resource) {
        super(context, resource);

        //Initialise les variables de classe
        m_lsItems = new ArrayList<ForcastDay>();
    }

    @Override
    public int getPosition(ForcastDay item) {
        return m_lsItems.indexOf(item);
    }

    @Override
    public int getCount() {
        return m_lsItems.size();
    }

    @Override
    public ForcastDay getItem(int position) {
        return m_lsItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Obtient un objet Inflater
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inflate le layout XML listview_item
        View rowView = inflater.inflate(R.layout.listview_item, parent, false);

        //Référence les vues
        TextView textViewDay = (TextView) rowView.findViewById(R.id.textviewDay);
        TextView textViewDesc = (TextView) rowView.findViewById(R.id.textviewdDesc);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgviewMeteo);

        //Le FcstDay à afficher sur cet item de la list
        ForcastDay dayToShow = getItem(position);

        //Valorise les vues avec le FcstDay correspondant
        textViewDay.setText(dayToShow.getDay_long());
        textViewDesc.setText(dayToShow.getDate());
        //textViewDay.setText(dayToShow.getTmin() + "");
        Picasso.get().load(dayToShow.getIcon()).into(imageView);

        return rowView;
    }

    @Override
    public void addAll(@NonNull Collection<? extends ForcastDay> collection){
        //Vide la liste actuelle
        m_lsItems.clear();

        //Ajoute les nouveaux éléments à la liste
        m_lsItems.addAll(collection);

        //Met à jour la ListView
        notifyDataSetChanged();
    }

    @Override
    public void add(@Nullable ForcastDay object) {
        super.add(object);
    }
}