package com.example.grupo3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import tablas.Tarea;

public class ActivityNuevaTarea extends AppCompatActivity implements View.OnClickListener {
    EditText idT,nomMod,nomTarea, fechaSeleccionada;
    DatabaseReference dbRef;
    Tarea tarea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);

        dbRef= FirebaseDatabase.getInstance().getReference().child("tarea");

        LinearLayout layout= findViewById(R.id.layoutNuevaTarea);

        MaterialToolbar encabezado = findViewById(R.id.encabezadoNuevaTarea);
        idT=findViewById(R.id.idTarea);
        nomMod=findViewById(R.id.moduloTarea);
        nomTarea=findViewById(R.id.nombreTarea);
        fechaSeleccionada = findViewById(R.id.fechaEntrega);
        MaterialButton eligeFecha= findViewById(R.id.botonEligeFecha);
        MaterialButton botonRegistrar = findViewById(R.id.botonRegistrarTarea);

        encabezado.setNavigationOnClickListener(this);
        eligeFecha.setOnClickListener(this);
        botonRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        LinearLayout layout= findViewById(R.id.layoutNuevaTarea);
        if (v.getId() == R.id.botonEligeFecha) {
            Calendar calendario = Calendar.getInstance();
            int year = calendario.get(Calendar.YEAR);
            int month = calendario.get(Calendar.MONTH);
            int day = calendario.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog selectorFecha = new DatePickerDialog(ActivityNuevaTarea.this, new DatePickerDialog.OnDateSetListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    month++;
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fecha = LocalDate.of(year, month, dayOfMonth);

                    fechaSeleccionada.setText(fecha.format(formato));
                }
            },year,month,day);
            selectorFecha.show();
        } else if (v.getId() == R.id.botonRegistrarTarea) {
            String textoID=idT.getText().toString().trim();
            String nomM=nomMod.getText().toString().trim();
            String nomT=nomTarea.getText().toString().trim();
            String fecha=fechaSeleccionada.getText().toString().trim();

            if (textoID.isEmpty() || nomM.isEmpty() || nomT.isEmpty() || fecha.isEmpty()) {
                Snackbar.make(layout, R.string.errorTextosVacíos, Snackbar.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Mensaje Informativo");
                builder.setMessage("Estás a punto de guardar una nueva tarea, si estás seguro haz clic en 'aceptar'");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*CODIGO DB*/
                        tarea = new Tarea();
                        int id = Integer.parseInt(textoID);
                            /*SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                            Date f= null;
                            */
                           /* try {
                                f = sdf.parse(fecha);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }*/
                        tarea.setId(id);
                        tarea.setModulo(nomM);
                        tarea.setTarea(nomT);
                        tarea.setFechaEntrega(fecha);
                        dbRef.child(String.valueOf(id)).setValue(tarea);

                        idT.setText("");
                        nomMod.setText("");
                        nomTarea.setText("");

                        Intent actividadMenuPrincipal = new Intent(ActivityNuevaTarea.this, MenuPrincipal.class);
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
            Intent actividadMenuPrincipal = new Intent(ActivityNuevaTarea.this, MenuPrincipal.class);
            startActivity(actividadMenuPrincipal);
        }
    }
}