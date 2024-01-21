package com.example.grupo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tablas.Usuario;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference dbRef;
    private EditText textoNombre, textoCorreo, textoContrasena, textoConfirmarContrasena;
    private String nombre, correo, contrasena, confirmarContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Aquí se enlazan los elementos con sus respectivos en el xml
        MaterialToolbar encabezado = findViewById(R.id.encabezadoSignIn);
        textoNombre = findViewById(R.id.editTextNombreSignIn);
        textoCorreo = findViewById(R.id.editTextCorreoSignIn);
        textoContrasena = findViewById(R.id.editTextContrasenaSignIn);
        textoConfirmarContrasena = findViewById(R.id.editTextConfirmarContrasenaSignIn);
        MaterialButton botonRegistrarse = findViewById(R.id.botonRegistrarseSignIn);

        dbRef= FirebaseDatabase.getInstance().getReference().child("usu");

        //Se indica que al pulsar en cualquiera de los botones se ejecutará el método OnClick
        encabezado.setNavigationOnClickListener(this);
        botonRegistrarse.setOnClickListener(this);
    }

    /**
     * Dependiendo del botón al que se pulse el método realizará ddiferentes acciones
     *
     * @param v The view that was clicked
     */
    @Override
    public void onClick(View v) {
        LinearLayout layout = findViewById(R.id.layoutSignIn);

        //Si el botón pulsado en el de registrarse realizará las siguientes acciones
        if (v.getId()==R.id.botonRegistrarseSignIn) {
            //Se obtienen los datos escritos por el usuario y se guardan en Strings
            nombre = textoNombre.getText().toString();
            correo = textoCorreo.getText().toString();
            contrasena = textoContrasena.getText().toString();
            confirmarContrasena = textoConfirmarContrasena.getText().toString();

            //Se verifica si están rellenados todos los datos
            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                Snackbar.make(layout, R.string.errorTextosVacíos, Snackbar.LENGTH_SHORT).show();

            //Si están rellenados se verificará si la contraseña y su confirmación son la misma
            } else if (!contrasena.equals(confirmarContrasena)) {
                Snackbar.make(layout, R.string.errorContrasenaYConfirmacionErroneas, Snackbar.LENGTH_SHORT).show();

            //Si son la misma por último se verificará si el usuario existe en la base de datos
            } else {
                dbRef.orderByChild("nombre").equalTo(nombre).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Si hay algún usuario con ese nombre se ejecutará esta condición
                        if (snapshot.exists()) {
                            Snackbar.make(layout, R.string.errorNombreYaExistente, Snackbar.LENGTH_SHORT).show();

                        //Sino guardará el usuario en una base de datos y ejecutará la actividad Login
                        }else{
                            Usuario usu = new Usuario(nombre, correo, contrasena);
                            dbRef.child(nombre).setValue(usu);
                            Intent actividadLogin = new Intent(SignIn.this, Login.class);
                            startActivity(actividadLogin);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }

        //Si el botón pulsado es el de volver al inicio de sesión se le llevará a la actividad Login.
        } else {
            Intent actividadLogin = new Intent(SignIn.this, Login.class);
            startActivity(actividadLogin);
        }
    }
}