package com.example.grupo3;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import tablas.Modulo;

public class ActivityNuevoModulo extends AppCompatActivity implements View.OnClickListener {
    Bundle usuario;
    DatabaseReference dbRef;
    Modulo mod;
    EditText  textoNombre,textoCiclo,textoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_modulo);
        usuario=getIntent().getExtras();

        MaterialToolbar encabezado = findViewById(R.id.encabezadoNuevoModulo);
        textoNombre = findViewById(R.id.nombreModulo);
        textoCiclo = findViewById(R.id.ciclo);
        textoUsuario = findViewById(R.id.usuario);
        MaterialButton botonRegistrar = findViewById(R.id.botonRegistrarModulo);
        dbRef= FirebaseDatabase.getInstance().getReference().child("mod"); //SIN ESTA LINEA NO SE INSERTA NADA

        encabezado.setNavigationOnClickListener(this);
        botonRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.botonRegistrarModulo) {
            LinearLayout layout = findViewById(R.id.layoutNuevoModulo);

            String nombre = textoNombre.getText().toString().trim();
            String ciclo = textoCiclo.getText().toString().trim();
            String usuarioT = textoUsuario.getText().toString().trim();

            if (nombre.isEmpty() || ciclo.isEmpty() || usuarioT.isEmpty()) {
                Snackbar.make(layout, R.string.errorTextosVacíos, Snackbar.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Mensaje Informativo");
                builder.setMessage("Estás a punto de guardar un nuevo módulo, si estás seguro haz clic en 'aceptar'");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mod = new Modulo();
                        mod.setModulo(nombre);
                        mod.setCiclo(ciclo);
                        mod.setUsuario(usuarioT);
                        dbRef.child(nombre).setValue(mod);
                        Intent actividadMenuPrincipal = new Intent(ActivityNuevoModulo.this, MenuPrincipal.class);
                        actividadMenuPrincipal.putExtras(usuario);
                        startActivity(actividadMenuPrincipal);
                    }
                });
                builder.setNegativeButton("No aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(layout, "Si no aceptas modifica algún campo", Snackbar.LENGTH_SHORT).show();
                    }
                });
                builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(layout, "Has cancelado el registro", Snackbar.LENGTH_SHORT).show();
                    }
                });
                AlertDialog cuadroDialogo = builder.create();
                cuadroDialogo.show();
            }
        } else {
            Intent actividadMenuPrincipal=new Intent(ActivityNuevoModulo.this, MenuPrincipal.class);
            actividadMenuPrincipal.putExtras(usuario);
            startActivity(actividadMenuPrincipal);
        }
    }
}