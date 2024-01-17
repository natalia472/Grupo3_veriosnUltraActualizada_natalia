package com.example.grupo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Tablas.Usuario;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    //Atributos.
    private Bundle usuario;
    private Usuario usuarioPrueba;
    private EditText textoNombre;
    private EditText textoCorreo;
    private EditText textoContrasena;
    private EditText textoConfirmarContrasena;
    DatabaseReference dbRef;
    Usuario usu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Se obtiene el Bundle de la actividad anterior y se saca de este el usuario de prueba
        usuario = getIntent().getExtras();
        usuarioPrueba = usuario.getParcelable("usuario");

        //Aquí se enlazan los elementos con sus respectivos en el xml.
        MaterialToolbar encabezado = findViewById(R.id.encabezadoSignIn);
        textoNombre = findViewById(R.id.editTextNombreSignIn);
        textoCorreo = findViewById(R.id.editTextCorreoSignIn);
        textoContrasena = findViewById(R.id.editTextContrasenaSignIn);
        textoConfirmarContrasena = findViewById(R.id.editTextConfirmarContrasenaSignIn);
        MaterialButton botonRegistrarse = findViewById(R.id.botonRegistrarseSignIn);

        //Se indica que al pulsar en cualquiera de los botones se ejecutará el método OnClick.
        encabezado.setNavigationOnClickListener(this);
        botonRegistrarse.setOnClickListener(this);

        dbRef= FirebaseDatabase.getInstance().getReference().child("usu");
        usu=new Usuario();
    }

    /**
     * Dependiendo del botón al que se pulse el método realizará ddiferentes acciones.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        //Si el botón pulsado en el de registrarse realizará las siguientes acciones.
        if (v.getId()==R.id.botonRegistrarseSignIn) {
            //Se obtienen los datos escritos por el usuario para realizar las siguientes acciones.
            String nombre = textoNombre.getText().toString();
            String correo = textoCorreo.getText().toString();
            String contrasena = textoContrasena.getText().toString();
            String confirmarContrasena = textoConfirmarContrasena.getText().toString();

            /*Si alguno de los campos está vacío aparecerá un mensaje avisando al usuario de que todos los campos
            deben ser rellenados.*/
            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                Toast.makeText(SignIn.this, R.string.errorTextosVacíos, Toast.LENGTH_SHORT).show();

            //Sino si el correo es igual al del usuario de prueba se avisará al usuario de que el mismo ya está en uso.
            } else if (usuarioPrueba.getNombre().equals(nombre)) {
                Toast.makeText(SignIn.this, R.string.errorNombreYaExistente, Toast.LENGTH_SHORT).show();

            /*Sino si la contraseña y su confirmación son iguales se le llevará a la actividad MenuPrincipal junto con
            el Bundle con el Usuario el cuál se crea con los datos insertados por el usuario.*/
            } else if (contrasena.equals(confirmarContrasena)) {
                /* antes de cambiar de pantalla, se insertan los datos en la bbdd y luego se
                ponen los campos en blanco */
                nombre=textoNombre.getText().toString().trim();
                correo=textoCorreo.getText().toString().trim();
                contrasena=textoContrasena.getText().toString().trim();
                usu.setNombre(nombre);
                usu.setCorreo(correo);
                usu.setContrasena(contrasena);
                dbRef.child(nombre).setValue(usu);

                textoNombre.setText("");
                textoCorreo.setText("");
                textoContrasena.setText("");
                textoConfirmarContrasena.setText("");
                usuario.putParcelable("usuario", new Usuario(nombre, correo, contrasena));
                Intent actividadMenuPrincipal = new Intent(SignIn.this, MenuPrincipal.class);
                //actividadMenuPrincipal.putExtras(usuario);
                actividadMenuPrincipal.putExtra("usuarioRegistro",nombre);
                actividadMenuPrincipal.putExtra("contras",contrasena);
                startActivityForResult(actividadMenuPrincipal,2);

                //startActivity(actividadMenuPrincipal);

            /*Si la contraseña y la confirmación de la misma no son iguales se le avisará mediante un mensaje al
            usuario.*/
            } else {
                Toast.makeText(this, R.string.errorContrasenaYConfirmacionErroneas, Toast.LENGTH_SHORT).show();
            }

        //Si el botón pulsado es el de volver al inicio de sesión se le llevará a la actividad Login.
        } else {
            Intent actividadLogin = new Intent(SignIn.this, Login.class);
            startActivity(actividadLogin);
        }
    }
}