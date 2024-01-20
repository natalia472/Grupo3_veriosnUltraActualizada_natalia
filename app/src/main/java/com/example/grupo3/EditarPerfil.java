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
    private Bundle b;

    private Usuario datosUsuario;
    private EditText textoContrasena;
    private EditText textoCorreo;
    DatabaseReference dbRef;
    Usuario usu;
    String nomUsuario,nom,editarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);



        /*usuario = getIntent().getExtras();
        datosUsuario = usuario.getParcelable("usuario");*/

        MaterialToolbar encabezado = findViewById(R.id.encabezadoEditarPerfil);
        //textoNombre = findViewById(R.id.editTextNombreEditarPerfil);
        textoContrasena=findViewById(R.id.editTextContrasena);
        textoCorreo = findViewById(R.id.editTextCorreoEditarPerfil);
        FloatingActionButton botonCambios = findViewById(R.id.botonCambiosEditarPerfil);

        /*textoNombre.setText(datosUsuario.getNombre());
        textoCorreo.setText(datosUsuario.getCorreo());*/

        dbRef= FirebaseDatabase.getInstance().getReference();
        nomUsuario=getIntent().getStringExtra("nomUsu");
        editarUsuario=getIntent().getStringExtra("usuarioInicio");

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
                /*datosUsuario.setNombre(nombre);
                datosUsuario.setCorreo(correo);
                usuario.putParcelable("usuario", datosUsuario);
                actividadPerfil.putExtras(usuario);
                startActivity(actividadPerfil);*/
                //Query consulta= dbRef.equalTo(editarUsuario);
                dbRef.child("usu").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombreOriginal=getIntent().getStringExtra("nombreB");
                        if(snapshot.exists()){
                            if(!contrasena.isEmpty()){
                                dbRef.child("usu").child(nombreOriginal).child("contrasena").setValue(contrasena);
                            }
                            if(!correo.isEmpty()){
                                dbRef.child("usu").child(nombreOriginal).child("correo").setValue(correo);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
                //actividadPerfil.putExtra("usuario",nom);
                startActivity(actividadPerfil);
            }
        } else {
            //actividadPerfil.putExtras(usuario);
            //actividadPerfil.putExtra("usuario",nom);
            startActivity(actividadPerfil);
        }
    }
}