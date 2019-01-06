package com.example.darius.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.darius.myapplication.MainActivity;
import com.example.darius.myapplication.R;
import com.example.darius.myapplication.model.Car;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CarsAdapter extends ArrayAdapter<Car> {
    private MainActivity parent;

    public CarsAdapter(Context context, List<Car> cars) {
        super(context, 0, cars);
        this.parent = (MainActivity) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Car car = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.car_layout, parent, false);
        }

        TextView brand = (TextView) convertView.findViewById(R.id.brand);
        TextView model = (TextView) convertView.findViewById(R.id.model);
        TextView id = (TextView) convertView.findViewById(R.id.idTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        Button updateButton = convertView.findViewById(R.id.updateButton);
        addDeleteHandler(deleteButton, car.getId());
        addUpdateHandler(updateButton, convertView);
        brand.setText(car.getBrand());
        model.setText(car.getModel());
        id.setText(car.getId());
        brand.setEnabled(false);
        model.setEnabled(false);

        return convertView;
    }

    private void addDeleteHandler(Button button, final String id) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("car").child(id).removeValue();
                notifyDataSetChanged();
                parent.fetchCars();
            }
        });
    }

    private void addUpdateHandler(Button button, final View convertView) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ((TextView) convertView.findViewById(R.id.idTextView)).getText().toString();
                String oldBrand = ((TextView) convertView.findViewById(R.id.brand)).getText().toString();
                String oldModel = ((TextView) convertView.findViewById(R.id.model)).getText().toString();
                parent.initEditCarView(id, oldBrand, oldModel);
            }
        });
    }
}
