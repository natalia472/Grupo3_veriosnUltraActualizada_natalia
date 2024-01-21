package com.example.grupo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import tablas.Usuario;

public class Perfil extends AppCompatActivity implements View.OnClickListener {
    private Bundle usuario;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        usuario = getIntent().getExtras();

        MaterialToolbar encabezado = findViewById(R.id.encabezadoPerfil);
        TextView textoNombre = findViewById(R.id.textViewNombrePerfil);
        TextView textoCorreo = findViewById(R.id.textViewCorreoPerfil);
        FloatingActionButton botonEditarPerfil = findViewById(R.id.botonEditarPerfil);

        dbRef= FirebaseDatabase.getInstance().getReference().child("usu");

        dbRef.orderByChild("nombre").equalTo(usuario.getString("usuarioInicio")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    Usuario u=ds.getValue(Usuario.class);
                    textoNombre.setText(u.getNombre());
                    textoCorreo.setText(u.getCorreo());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        encabezado.setNavigationOnClickListener(this);
        botonEditarPerfil.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.botonEditarPerfil) {
            Intent actividadEditarPerfil = new Intent(Perfil.this, EditarPerfil.class);
            actividadEditarPerfil.putExtras(usuario);
            startActivity(actividadEditarPerfil);
        }else {
            Intent actividadMenuPrincipal = new Intent(Perfil.this, MenuPrincipal.class);
            actividadMenuPrincipal.putExtras(usuario);
            startActivity(actividadMenuPrincipal);
        }
    }
}