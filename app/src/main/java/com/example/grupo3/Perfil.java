package com.example.grupo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import Tablas.Usuario;

public class Perfil extends AppCompatActivity implements View.OnClickListener {
    private Bundle usuario;
    DatabaseReference dbRef;
    Usuario usu;
    String nomUsuario;
    TextView textoNombre,textoCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        usuario = getIntent().getExtras();
        Usuario datosUsuario = usuario.getParcelable("usuario");

        MaterialToolbar encabezado = findViewById(R.id.encabezadoPerfil);
        textoNombre = findViewById(R.id.textViewNombrePerfil);
        textoCorreo = findViewById(R.id.textViewCorreoPerfil);
        FloatingActionButton botonEditarPerfil = findViewById(R.id.botonEditarPerfil);
        ExtendedFloatingActionButton botonCambiarContrasena = findViewById(R.id.botonCambiarContrasena);


        dbRef= FirebaseDatabase.getInstance().getReference().child("usu");
        usu=new Usuario();
        nomUsuario=getIntent().getStringExtra("usuarioRegistro");
        Query consulta=dbRef.equalTo(nomUsuario);
        consulta.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    Usuario u=ds.getValue(Usuario.class);
                    textoNombre.setText(u.getNombre());
                    textoCorreo.setText(u.getCorreo());
                    /**
                     * muestra el ultimo usuario añadido, si se inserta uno nuevo, seguira
                     * mostrando el añadido anterior al nuevo
                     * ej: ya hay usuario x, añado usuario y, pero muestra el x
                     * si borro el usuario x, muestra el usuario y
                     * Si se edita el usuario, crea uno nuevo con los datos editados,
                     * ej, edito el usuario y para que de nombre tenga t y correo t2,
                     * al volver, solo mostrara los datos de y, pero en firebase crea un
                     * nuevo usuario con los datos de t
                     * para cambiar el nombre y correo de un usuario haria falta otra clave
                     * 'primaria' ya que no se podria cambiar el nombre y correo del usuario cuyo
                     * nombre sea 'x', deberia tener otra clave para identificarlo y comprobar si asi funciona
                     * Cambiar la contraseña no funciona
                     */
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

      /*  textoNombre.setText(datosUsuario.getNombre());
        textoCorreo.setText(datosUsuario.getCorreo());*/


        encabezado.setNavigationOnClickListener(this);
        botonEditarPerfil.setOnClickListener(this);
        botonCambiarContrasena.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.botonEditarPerfil) {
            Intent actividadEditarPerfil = new Intent(Perfil.this, EditarPerfil.class);
            //actividadEditarPerfil.putExtras(usuario);
            startActivity(actividadEditarPerfil);
        } else if (v.getId() == R.id.botonCambiarContrasena) {
            Intent actividadCambiarContrasena = new Intent(Perfil.this, CambioContrasena.class);
            //actividadCambiarContrasena.putExtras(usuario);
            startActivity(actividadCambiarContrasena);
        } else {
            Intent actividadMenuPrincipal = new Intent(Perfil.this, MenuPrincipal.class);
            //actividadMenuPrincipal.putExtras(usuario);
            startActivity(actividadMenuPrincipal);
        }
    }
}