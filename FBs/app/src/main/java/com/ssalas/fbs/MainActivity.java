package com.ssalas.fbs;


import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.ssalas.fbs.modelo.Usuario;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    MyArrayAdapter arrayAdapter;
    List<Usuario> usuarioList;
    ListView usuarioListView;
    private FirebaseFunctions mFunctions;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbReference;
    private EditText etNomSensor, etTipo, etValor,etUbicacion,etObs;



    private Usuario usuarioactual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNomSensor = (EditText) findViewById(R.id.nomSensor);
        etTipo = (EditText) findViewById(R.id.nomTipo);
        etValor = (EditText) findViewById(R.id.nomValor);
        etUbicacion=(EditText)findViewById(R.id.nomUbicacion);
        etObs=(EditText)findViewById(R.id.nomObs);

        usuarioList = new ArrayList<>();
        arrayAdapter = new MyArrayAdapter(usuarioList, getBaseContext(), getLayoutInflater());
        usuarioListView = (ListView) findViewById(R.id.lv_usurios);
        usuarioListView.setAdapter(arrayAdapter);

        mFunctions = FirebaseFunctions.getInstance();

        initFirebaseDB();
        loadDataFromFirebase();

        usuarioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                etNomSensor.setText(usuarioList.get(i).getNSensor());
                etTipo.setText(usuarioList.get(i).getTipoS());
                etValor.setText(usuarioList.get(i).getValorS());
                etUbicacion.setText(usuarioList.get(i).getUbicacionS());
                etObs.setText(usuarioList.get(i).getObservacion());
                usuarioactual = usuarioList.get(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_agregar:
                saveNewUser();
                break;
            case R.id.menu_item_save:
                updateUser();
                break;
            case R.id.menu_item_eliminar:
                deleteUser();
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveNewUser() {

        String nUsuario = etNomSensor.getText().toString();
        String TipoS = etTipo.getText().toString();
        String ValorS = etValor.getText().toString();
        String Ubicacion= etUbicacion.getText().toString();
        String Observacion =etObs.getText().toString();


        if (!(nUsuario == null) && !(TipoS == null) && !(ValorS == null)&& ! (Ubicacion==null)&& !(Observacion ==null)) {
            Usuario usuario = new Usuario(
                    UUID.randomUUID().toString(),
                    nUsuario,
                    TipoS,
                    new Date(),
                    ValorS,
                    Ubicacion,
                    Observacion
            );
            dbReference.child("sensores").child(usuario.getSenId()).setValue(usuario);
            Toast.makeText(this, "Sensor guardado!", Toast.LENGTH_LONG).show();
        }
    }
    private void updateUser() {
        usuarioactual.setNSensor(etNomSensor.getText().toString());
        usuarioactual.setValorS(etValor.getText().toString());
        usuarioactual.setTipo(etTipo.getText().toString());
        usuarioactual.setUbicacionS(etUbicacion.getText().toString());
        usuarioactual.setObservacion(etObs.getText().toString());
        dbReference.child("sensor").child(usuarioactual.getSenId()).setValue(usuarioactual);
        Toast.makeText(this, "Sensor actualizado!", Toast.LENGTH_SHORT).show();
    }
    private void deleteUser(){
        dbReference.child("sensores").child(usuarioactual.getSenId()).removeValue();
        Toast.makeText(this, "Sensor eliminado!", Toast.LENGTH_SHORT).show();
    }
    private void loadDataFromFirebase() {
        dbReference.child("sensores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    usuarioList.add(ds.getValue(Usuario.class));
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getBaseContext(), "Error, "+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initFirebaseDB() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReference = firebaseDatabase.getReference();
    }

    private Task<String> addMessage(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("addMessage")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }

}