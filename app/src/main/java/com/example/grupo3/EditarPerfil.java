package com.example.grupo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tablas.Usuario;

public class EditarPerfil extends AppCompatActivity implements View.OnClickListener {
    private Bundle usuario;
    private EditText textoContrasena;
    private EditText textoCorreo;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        usuario = getIntent().getExtras();

        MaterialToolbar encabezado = findViewById(R.id.encabezadoEditarPerfil);
        textoContrasena = findViewById(R.id.editTextContrasena);
        textoCorreo = findViewById(R.id.editTextCorreoEditarPerfil);
        FloatingActionButton botonCambios = findViewById(R.id.botonCambiosEditarPerfil);

        dbRef = FirebaseDatabase.getInstance().getReference().child("usu");

        dbRef.orderByChild("nombre").equalTo(usuario.getString("usuarioInicio")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    Usuario u=ds.getValue(Usuario.class);
                    textoCorreo.setText(u.getCorreo());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        encabezado.setNavigationOnClickListener(this);
        botonCambios.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent actividadPerfil = new Intent(EditarPerfil.this, Perfil.class);
        if (v.getId() == R.id.botonCambiosEditarPerfil) {
            String contrasena = textoContrasena.getText().toString().trim();
            String correo = textoCorreo.getText().toString().trim();

            if (contrasena.isEmpty() || correo.isEmpty()) {
                RelativeLayout layout = findViewById(R.id.layoutEditarPerfil);
                Snackbar.make(layout, R.string.errorTextosVacíos, Snackbar.LENGTH_SHORT).show();
            } else {
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombreOriginal=usuario.getString("usuarioInicio");
                        if(snapshot.exists()){
                            dbRef.child(nombreOriginal).child("correo").setValue(correo);
                            dbRef.child(nombreOriginal).child("contrasena").setValue(contrasena);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

                actividadPerfil.putExtras(usuario);
                startActivity(actividadPerfil);
            }
        } else {
            actividadPerfil.putExtras(usuario);
            startActivity(actividadPerfil);
            /*La diferencia entre putExtras y putExtra es que en la primera
            se envía un Bundle y en la segunda se envía una clave y un valor*/
        }
    }
}