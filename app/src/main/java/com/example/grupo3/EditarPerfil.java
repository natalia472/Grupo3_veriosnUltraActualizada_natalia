package com.example.grupo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import Tablas.Usuario;

public class EditarPerfil extends AppCompatActivity implements View.OnClickListener {
    private Bundle usuario;
    private Usuario datosUsuario;
    private EditText textoNombre;
    private EditText textoCorreo;
    DatabaseReference dbRef;
    Usuario usu;
    String nomUsuario,nom,contr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        /*usuario = getIntent().getExtras();
        datosUsuario = usuario.getParcelable("usuario");*/

        MaterialToolbar encabezado = findViewById(R.id.encabezadoEditarPerfil);
        textoNombre = findViewById(R.id.editTextNombreEditarPerfil);
        textoCorreo = findViewById(R.id.editTextCorreoEditarPerfil);
        FloatingActionButton botonCambios = findViewById(R.id.botonCambiosEditarPerfil);

        /*textoNombre.setText(datosUsuario.getNombre());
        textoCorreo.setText(datosUsuario.getCorreo());*/

        dbRef= FirebaseDatabase.getInstance().getReference().child("usu");
        nomUsuario=getIntent().getStringExtra("nomUsu");
        contr=getIntent().getStringExtra("contras");

        encabezado.setNavigationOnClickListener(this);
        botonCambios.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent actividadPerfil = new Intent(EditarPerfil.this, Perfil.class);
        if (v.getId() == R.id.botonCambiosEditarPerfil) {
            String nombre = textoNombre.getText().toString();
            String correo = textoCorreo.getText().toString();
            if (nombre.isEmpty() || correo.isEmpty()) {
                Toast.makeText(this, R.string.errorTextosVacíos, Toast.LENGTH_SHORT).show();
            } else {
                /*datosUsuario.setNombre(nombre);
                datosUsuario.setCorreo(correo);
                usuario.putParcelable("usuario", datosUsuario);
                actividadPerfil.putExtras(usuario);
                startActivity(actividadPerfil);*/
                Query consulta= dbRef.equalTo(contr);
                consulta.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nom=textoNombre.getText().toString().trim();
                        String corr=textoCorreo.getText().toString().trim();
                        if(snapshot.exists()){
                            if(!nom.isEmpty()){
                                dbRef.child(nom).setValue(nom);
                            }
                            if(!corr.isEmpty()){
                                dbRef.child(nom).child("correo").setValue(corr);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
                actividadPerfil.putExtra("usuario",nom);
                startActivity(actividadPerfil);
            }
        } else {
            //actividadPerfil.putExtras(usuario);
            actividadPerfil.putExtra("usuario",nom);
            startActivity(actividadPerfil);
        }
    }
}