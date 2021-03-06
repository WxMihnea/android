package com.example.mihnea.myapplication.adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.darius.myapplication.R;
import com.example.mihnea.myapplication.adapters.model.Phone;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        initLoginView();
    }

    private void initMainView() {
        setContentView(R.layout.main);
        addLogoutButtonListener();
        addPhoneAddInitButtonListener();
        fetchPhones();
    }

    public void fetchPhones() {
        final List<Phone> Phones = new ArrayList<>();
        final MainActivity self = this;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Phone");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Phone Phone = childSnapshot.getValue(Phone.class);
                    if (Phone.getUserEmail() != null && Phone.getUserEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        Phones.add(Phone);
                    }
                }

                PhonesAdapter adapter = new PhonesAdapter(self, Phones);
                ListView listView = (ListView) findViewById(R.id.PhoneListView);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void initLoginView() {
        setContentView(R.layout.activity_login);
        addLoginButtonListener();
        ((TextView) findViewById(R.id.invalidCredentialsView)).setVisibility(TextView.INVISIBLE);
    }

    public void initEditPhoneView(String id, String oldBrand, String oldModel) {
        setContentView(R.layout.phone_edit);
        ((EditText) findViewById(R.id.newBrandText)).setText(oldBrand);
        ((EditText) findViewById(R.id.newModelText)).setText(oldModel);
        addFinishEditButtonListener(id);
        addBackFromEditButtonListener();
    }

    private void addFinishEditButtonListener(final String id) {
        Button finishEditButton = findViewById(R.id.executeUpdateButton);
        final MainActivity self = this;
        finishEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                String brand = ((EditText) findViewById(R.id.newBrandText)).getText().toString();
                String model = ((EditText) findViewById(R.id.newModelText)).getText().toString();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                database.child("Phone").child(id).removeValue();
                String id = database.push().getKey();
                Phone Phone = new Phone(id, brand, model, email);
                database.child("Phone").child(id).setValue(Phone);
                self.initMainView();
            }
        });
    }

    private void addBackFromEditButtonListener() {
        Button backButton = findViewById(R.id.backToMainFromEdit);
        final MainActivity self = this;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.initMainView();
            }
        });
    }

    private void initAddView() {
        setContentView(R.layout.add);
        addBackFromAddButtonListener();
        addFinishAddButtonListener();
    }

    private void addBackFromAddButtonListener() {
        Button backButton = findViewById(R.id.backToMainFromAddButton);
        final MainActivity self = this;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.initMainView();
            }
        });
    }

    private void addFinishAddButtonListener() {
        Button finishAddButton = findViewById(R.id.finishAddButton);
        final MainActivity self = this;
        finishAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                String brand = ((EditText) findViewById(R.id.brandAddText)).getText().toString();
                String model = ((EditText) findViewById(R.id.modelAddText)).getText().toString();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String id = database.push().getKey();
                Phone Phone = new Phone(id, brand, model, email);
                database.child("Phone").child(id).setValue(Phone);
                self.initMainView();
            }
        });
    }

    private void addPhoneAddInitButtonListener() {
        Button addButton = findViewById(R.id.addPhoneButton);
        final MainActivity self = this;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.initAddView();
            }
        });
    }


    private void addLoginButtonListener() {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Button btnLogin = findViewById(R.id.loginbutton);
        final MainActivity self = this;
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = ((EditText) findViewById(R.id.userfield)).getText().toString();
                final String password = ((EditText) findViewById(R.id.passwordfield)).getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(self, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    self.initMainView();
                                }

                                if (task.getException() != null) {
                                    ((TextView) findViewById(R.id.invalidCredentialsView)).setVisibility(TextView.VISIBLE);
                                }
                            }
                        });
            }
        });
    }

    private void addLogoutButtonListener() {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        Button logoutButton = findViewById(R.id.logoutbutton);
        final MainActivity self = this;
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                initLoginView();
            }
        });
    }
}
