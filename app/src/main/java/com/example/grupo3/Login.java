package com.example.grupo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tablas.Usuario;

public class Login extends AppCompatActivity implements View.OnClickListener {
    //Atributos.
    private Usuario usuarioPrueba;
    private Bundle usuario;
    private EditText textoNombre;
    private EditText textoContrasena;
    DatabaseReference dbRef;
    Usuario usu;


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
        //Si el botón pulsado en el de iniciar sesión realizará las siguientes acciones.
        if (v.getId() == R.id.botonIniciarSesionLogin) {
            //Se obtienen el correo y la contraseña escritos por el usuario para realizar las siguientes acciones.
            String correo = textoNombre.getText().toString();
            String contrasena = textoContrasena.getText().toString();

            /*Si alguno de los campos está vacío aparecerá un mensaje avisando al usuario de que todos los campos
            deben ser rellenados.*/
            if (correo.isEmpty() || contrasena.isEmpty()) {
                LinearLayout layout = findViewById(R.id.layoutLogin);
                Snackbar.make(layout, R.string.errorTextosVacíos, Snackbar.LENGTH_SHORT).show();

            /*Sino si el correo y contraseña corresponden a los del usuario de prueba se le llevará a la actividad
            MenuPrincipal junto con el Bundle con el Usuario correspondiente.*/
            } else{
                Intent actividadMenuPrincipal = new Intent(Login.this, MenuPrincipal.class);
                actividadMenuPrincipal.putExtra("usuarioInicio",textoNombre.getText().toString().trim());
                startActivity(actividadMenuPrincipal);
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