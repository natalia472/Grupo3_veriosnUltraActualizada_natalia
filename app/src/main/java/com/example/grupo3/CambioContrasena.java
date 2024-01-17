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

public class CambioContrasena extends AppCompatActivity implements View.OnClickListener {
    private Bundle usuario;
    private Usuario datosUsuario;
    private EditText textoViejaContrasena;
    private EditText textoNuevaContrasena;
    private EditText textoConfirmarContrasena;
    DatabaseReference dbRef;
    String nomUsuario,nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_contrasena);

        usuario = getIntent().getExtras();
       // datosUsuario = usuario.getParcelable("usuario");

        MaterialToolbar encabezado = findViewById(R.id.encabezadoCambiarContrasena);
        textoViejaContrasena = findViewById(R.id.editTextContrasenaCambiarContrasena);
        textoNuevaContrasena = findViewById(R.id.editTextNuevaContrasenaCambiarContrasena);
        textoConfirmarContrasena = findViewById(R.id.editTextConfirmarContrasenaCambiarContrasena);
        FloatingActionButton botonCambios = findViewById(R.id.botonCambiosCambiarContrasena);

        dbRef= FirebaseDatabase.getInstance().getReference().child("datosUsuario");
        datosUsuario=new Usuario();
        nomUsuario=getIntent().getStringExtra("usuarioRegistro");

        encabezado.setNavigationOnClickListener(this);
        botonCambios.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent actividadPerfil = new Intent(CambioContrasena.this, Perfil.class);
        if (v.getId() == R.id.botonCambiosCambiarContrasena) {
            String viejaContrasena = textoViejaContrasena.getText().toString();
            String nuevaContrasena = textoNuevaContrasena.getText().toString();
            String confirmarContrasena = textoConfirmarContrasena.getText().toString();
            if (viejaContrasena.isEmpty() || nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                Toast.makeText(this, R.string.errorTextosVacíos, Toast.LENGTH_SHORT).show();
            } else if (nuevaContrasena.equals(confirmarContrasena)) {
                Query consulta= dbRef.equalTo(nomUsuario);
                consulta.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nom=textoNuevaContrasena.getText().toString().trim();
                        if(snapshot.exists()){
                            if(!nuevaContrasena.isEmpty()){
                                dbRef.child(nom).child("contrasena").setValue(nuevaContrasena);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

                //datosUsuario.setContrasena(nuevaContrasena);
                //usuario.putParcelable("usuario", datosUsuario);
                actividadPerfil.putExtras(usuario);
                startActivity(actividadPerfil);
            } else {
                Toast.makeText(this, R.string.errorContrasenaYConfirmacionErroneas, Toast.LENGTH_SHORT).show();
            }
        } else {
            actividadPerfil.putExtras(usuario);
            startActivity(actividadPerfil);
        }
    }
}