package com.example.grupo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText textoNombre;
    private EditText textoContrasena;
    private DatabaseReference dbRef;
    private String nombre, contrasena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Aquí se enlazan los elementos con sus respectivos en el xml
        textoNombre = findViewById(R.id.editTextNombreLogin);
        textoContrasena = findViewById(R.id.editTextContrasenaLogin);
        MaterialButton botonIniciarSesion = findViewById(R.id.botonIniciarSesionLogin);
        MaterialButton botonRegistrarse = findViewById(R.id.botonRegistrarseLogin);

        dbRef= FirebaseDatabase.getInstance().getReference().child("usu");

        //Se indica que al pulsar en cualquiera de los botones se ejecutará el método OnClick
        botonIniciarSesion.setOnClickListener(this);
        botonRegistrarse.setOnClickListener(this);
    }

    /**
     * Dependiendo del botón al que se pulse el método realizará diferentes acciones
     *
     * @param v The view that was clicked
     */
    @Override
    public void onClick(View v) {
        LinearLayout layout = findViewById(R.id.layoutLogin);

        //Si el botón pulsado en el de iniciar sesión realizará las siguientes acciones
        if (v.getId() == R.id.botonIniciarSesionLogin) {
            //Se obtienen el nombre y la contraseña escritos por el usuario para realizar las siguientes acciones
            nombre = textoNombre.getText().toString().trim();
            contrasena = textoContrasena.getText().toString().trim();

            /*Si alguno de los campos está vacío aparecerá un mensaje avisando al usuario de que todos los campos
            deben ser rellenados*/
            if (nombre.isEmpty() || contrasena.isEmpty()) {
                Snackbar.make(layout, R.string.errorTextosVacíos, Snackbar.LENGTH_SHORT).show();

            //En caso de que estén rellenados se buscará si los datos existen en la base de datos
            }else{
                //Mira en la base de datos si existe algún usuario con el nombre insertado
                dbRef.orderByChild("nombre").equalTo(nombre).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Si hay algún usuario con ese nombre se ejecutará esta condición
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String contrasBBDD = ds.child("contrasena").getValue(String.class);

                                /*Si la contraseña insertada es la misma que la de la base de datos se ejecutará
                                esta condición*/
                                if (contrasBBDD.equals(contrasena)) {
                                    Intent actividadMenuPrincipal = new Intent(Login.this, MenuPrincipal.class);
                                    actividadMenuPrincipal.putExtra("usuarioInicio", nombre);
                                    startActivity(actividadMenuPrincipal);

                                //Sino mostrará un mensaje de que la contraseña es incorrecta
                                } else {
                                    Snackbar.make(layout, R.string.errorContrasenaIncorrecta, Snackbar.LENGTH_SHORT).show();
                                }
                            }

                        //Sino mostrará un mensaje de que no existe ese usuario y que se registre
                        }else{
                            Snackbar.make(layout, R.string.errorInsertarNombreIniciarSesion, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }

        //Si el botón pulsado es el de registrarse se le llevará a la actividad SignIn
        } else if (v.getId() == R.id.botonRegistrarseLogin) {
            Intent actividadSignIn = new Intent(Login.this, SignIn.class);
            startActivity(actividadSignIn);
        }
    }
}