package com.axegurnov.android.earthshaker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexe on 02.03.2018.
 */

public class EarthAdapter extends ArrayAdapter<EarthDate>{

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthAdapter(Activity context, List<EarthDate> earthquakes) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, earthquakes);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }
        EarthDate dateEarth = getItem(position);

        TextView coordinateText = listItemView.findViewById(R.id.azimut);
        coordinateText.setText(dateEarth.getmCoordinate());

        TextView cityText = listItemView.findViewById(R.id.city);
        cityText.setText(dateEarth.getmPlace());

        TextView dateText = listItemView.findViewById(R.id.date);
        dateText.setText(dateEarth.getmDate());

        TextView timeText = listItemView.findViewById(R.id.time);
        timeText.setText(dateEarth.getmTime());

        TextView magText = listItemView.findViewById(R.id.mag);
        magText.setText(dateEarth.getmMag());

        return listItemView;
    }

}
