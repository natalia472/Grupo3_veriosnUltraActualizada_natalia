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
import android.widget.RelativeLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import Tablas.Modulo;
import Tablas.Tarea;

public class ActivityNuevaTarea extends AppCompatActivity {
    EditText idT,nomMod,nomTarea;
    DatabaseReference dbRef;
    Tarea tarea;
    Date utilDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);
        LinearLayout linearPadre=(LinearLayout) findViewById(R.id.pantallaNuevaTarea);

        MaterialButton botonRegistrar = findViewById(R.id.botonRegistrarTarea);
        MaterialButton eligeFecha= findViewById(R.id.botonEligeFecha);
        EditText fechaSeleccionada = findViewById(R.id.fechaEntrega);

        idT=findViewById(R.id.idTarea);
        nomMod=findViewById(R.id.moduloTarea);
        nomTarea=findViewById(R.id.nombreTarea);
        dbRef= FirebaseDatabase.getInstance().getReference().child("tarea");

        eligeFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendario=Calendar.getInstance();
                int year= calendario.get(Calendar.YEAR);
                int month=calendario.get(Calendar.MONTH);
                int day=calendario.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog selectorFecha = new DatePickerDialog(ActivityNuevaTarea.this, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month++;
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        Log.i("fecha",dayOfMonth+" "+(month+1)+" "+ year);
                        LocalDate fecha = LocalDate.of(year, month, dayOfMonth);

                        fechaSeleccionada.setText(fecha.format(formato));
                        Snackbar barra=Snackbar.make(linearPadre,fecha.format(formato),Snackbar.LENGTH_SHORT);
                        barra.show();
                    }
                },year,month,day);
                selectorFecha.show();
            }
        });


        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setTitle("Mensaje Informativo");
                builder.setMessage("Estás a punto de guardar una nueva tarea, si estás seguro haz clic en 'aceptar'");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      /*CODIGO DB*/
                        tarea=new Tarea();
                        int id=Integer.parseInt(idT.getText().toString().trim());
                        String nomM=nomMod.getText().toString().trim();
                        String nomT=nomTarea.getText().toString().trim();
                        String fecha=fechaSeleccionada.getText().toString().trim();
                        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                        Date f= null;
                        try {
                            f = sdf.parse(fecha);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        tarea.setId(id);
                        tarea.setModulo(nomM);
                        tarea.setTarea(nomT);
                        tarea.setFechaEntrega(f);
                        dbRef.child(String.valueOf(id)).setValue(tarea);

                        idT.setText("");
                        nomMod.setText("");
                        nomTarea.setText("");

                        Intent pantallaPrincipal=new Intent(ActivityNuevaTarea.this, MenuPrincipal.class);
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