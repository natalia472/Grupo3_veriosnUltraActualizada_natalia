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

import tablas.Modulo;
import tablas.Usuario;

public class Login extends AppCompatActivity implements View.OnClickListener {
    //Atributos.
    private Usuario usuarioPrueba;
    private Bundle usuario;
    private EditText textoNombre;
    private EditText textoContrasena;
    DatabaseReference dbRef;
    Usuario usu;
    private String nombreUsuario;
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Crea un usuario de prueba.
        usuarioPrueba = new Usuario("Jose", "jose@gmail.com", "123456");

        //Se crea un Bundle con el usuario anteriormente creado.
        usuario = new Bundle();
        usuario.putParcelable("usuario", usuarioPrueba);

        //Aquí se enlazan los elementos con sus respectivos en el xml.
        textoNombre = findViewById(R.id.editTextNombreLogin);
        textoContrasena = findViewById(R.id.editTextContrasenaLogin);
        MaterialButton botonIniciarSesion = findViewById(R.id.botonIniciarSesionLogin);
        MaterialButton botonRegistrarse = findViewById(R.id.botonRegistrarseLogin);

        //Se indica que al pulsar en cualquiera de los botones se ejecutará el método OnClick.
        botonIniciarSesion.setOnClickListener(this);
        botonRegistrarse.setOnClickListener(this);
        dbRef= FirebaseDatabase.getInstance().getReference().child("usu");
    }

    /**
     * Dependiendo del botón al que se pulse el método realizará ddiferentes acciones.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        layout = findViewById(R.id.layoutLogin);
        //Si el botón pulsado en el de iniciar sesión realizará las siguientes acciones.
        if (v.getId() == R.id.botonIniciarSesionLogin) {
            //Se obtienen el correo y la contraseña escritos por el usuario para realizar las siguientes acciones.
            String correo = textoNombre.getText().toString();
            String contrasena = textoContrasena.getText().toString();

            /*Si alguno de los campos está vacío aparecerá un mensaje avisando al usuario de que todos los campos
            deben ser rellenados.*/
            if (correo.isEmpty() || contrasena.isEmpty()) {

                Snackbar.make(layout, R.string.errorTextosVacíos, Snackbar.LENGTH_SHORT).show();

            /*Sino si el correo y contraseña corresponden a los del usuario de prueba se le llevará a la actividad
            MenuPrincipal junto con el Bundle con el Usuario correspondiente.*/
            }else{
                nombreUsuario=textoNombre.getText().toString().trim();
                String contras=textoContrasena.getText().toString().trim();
                dbRef.orderByChild("nombre").equalTo(nombreUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (snapshot.exists()) { //si el dato existe en la bbdd
                               String contrasBBDD=ds.child("contrasena").getValue(String.class);
                               if(contrasBBDD.equals(contras)){
                                   Intent actividadMenuPrincipal = new Intent(Login.this, MenuPrincipal.class);
                                   actividadMenuPrincipal.putExtra("usuarioInicio",textoNombre.getText().toString().trim());
                                   startActivity(actividadMenuPrincipal);
                               }else{
                                   Snackbar.make(layout, R.string.errorInsertarDatosIniciarSesion, Snackbar.LENGTH_SHORT).show();
                               }
                            }else{
                                Snackbar.make(layout, R.string.errorInsertarDatosIniciarSesion, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }

        /*Si el botón pulsado es el de registrarse se le llevará a la actividad SignIn junto con el Bundle con
        el usuario de prueba.*/
        } else if (v.getId() == R.id.botonRegistrarseLogin) {
            Intent actividadSignIn = new Intent(Login.this, SignIn.class);
            actividadSignIn.putExtras(usuario);
            startActivity(actividadSignIn);
        }
    }
}