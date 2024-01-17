package com.example.grupo3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Tablas.Modulo;

public class ActivityNuevoModulo extends AppCompatActivity {
    DatabaseReference dbRef;
    Modulo mod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_modulo);

        MaterialButton botonRegistrar = findViewById(R.id.botonRegistrarModulo);
        dbRef= FirebaseDatabase.getInstance().getReference().child("mod"); //SIN ESTA LINEA NO SE INSERTA NADA
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());

                builder.setTitle("Mensaje Informativo");
                builder.setMessage("Estás a punto de guardar un nuevo módulo, si estás seguro haz clic en 'aceptar'");
                builder.setIcon(android.R.drawable.ic_dialog_info);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mod=new Modulo();
                        EditText  textoNombre = findViewById(R.id.nombreModulo);
                        EditText textoCiclo= findViewById(R.id.ciclo);
                        EditText textoUsuario= findViewById(R.id.usuario);

                        String nombre = textoNombre.getText().toString().trim();
                        String ciclo= textoCiclo.getText().toString().trim();
                        String usuario= textoUsuario.getText().toString().trim();

                        mod.setModulo(nombre);
                        mod.setCiclo(ciclo);
                        mod.setUsuario(usuario);
                        dbRef.child(nombre).setValue(mod);

                        textoNombre.setText("");
                        textoCiclo.setText("");
                        textoUsuario.setText("");
                        Intent pantallaPrincipal=new Intent(ActivityNuevoModulo.this, MenuPrincipal.class);
                        //mandar el nombre del modulo a la siguiente pantalla para poder hacer la consulta
                        pantallaPrincipal.putExtra("nombreM",textoNombre.getText().toString());
                        startActivity(pantallaPrincipal);
            }
        });
                builder.setNegativeButton("No aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        View padre=(View) v.getParent();
                        Snackbar barra= Snackbar.make(padre,"Si no aceptas modifica algún campo",Snackbar.LENGTH_SHORT);
                        barra.show();
                    }
                });

                builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        View padre=(View) v.getParent();
                        Snackbar barra= Snackbar.make(padre,"Has cancelado el registro",Snackbar.LENGTH_SHORT);
                        barra.show();
                    }
                });
                AlertDialog cuadroDialogo = builder.create();
                cuadroDialogo.show();
            }
        });

    }
}