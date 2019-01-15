package com.example.mihnea.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.darius.myapplication.R;
import com.example.mihnea.myapplication.adapters.model.Phone;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PhonesAdapter extends ArrayAdapter<Phone> {
    private MainActivity parent;

    public PhonesAdapter(Context context, List<Phone> Phones) {
        super(context, 0, Phones);
        this.parent = (MainActivity) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Phone Phone = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.phone_layout, parent, false);
        }

        TextView brand = (TextView) convertView.findViewById(R.id.brand);
        TextView model = (TextView) convertView.findViewById(R.id.model);
        TextView id = (TextView) convertView.findViewById(R.id.idTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        Button updateButton = convertView.findViewById(R.id.updateButton);
        addDeleteHandler(deleteButton, Phone.getId());
        addUpdateHandler(updateButton, convertView);
        brand.setText(Phone.getBrand());
        model.setText(Phone.getModel());
        id.setText(Phone.getId());
        brand.setEnabled(false);
        model.setEnabled(false);

        return convertView;
    }

    private void addDeleteHandler(Button button, final String id) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Phone").child(id).removeValue();
                notifyDataSetChanged();
                parent.fetchPhones();
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
                parent.initEditPhoneView(id, oldBrand, oldModel);
            }
        });
    }
}
