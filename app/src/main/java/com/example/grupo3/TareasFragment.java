package com.example.grupo3;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tablas.Tarea;

public class TareasFragment extends Fragment {
    private Bundle usuario;
    private DatabaseReference dbRef;
    private ArrayList<Tarea> listaTareas;
    private AdaptadorTareas miAdaptador;
    private String mod,tarea,fecha,usu;
    int idTarea;
    private ListView contenedorVista;

    public TareasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario = getArguments(); //El getArguments, es lo mismo que hacer getIntent().getExtras pero en fragmentos :)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tareas, container, false);

        contenedorVista = view.findViewById(R.id.listaTareas);
        FloatingActionButton botonNuevaTarea = (FloatingActionButton) view.findViewById(R.id.floatingABtareas);

        dbRef= FirebaseDatabase.getInstance().getReference().child("tarea");

        contenedorVista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//para que puedan seleccionarse de forma individual
        listaTareas=new ArrayList<>();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    fecha=ds.child("fechaEntrega").getValue(String.class);
                    idTarea=ds.child("id").getValue(Integer.class);
                    mod=ds.child("modulo").getValue(String.class);
                    tarea=ds.child("tarea").getValue(String.class);
                    usu=ds.child("usuario").getValue(String.class);
                    if(usuario.getString("usuarioInicio").equals(usu)){
                        listaTareas.add(new Tarea(idTarea,mod,tarea,fecha,usuario.getString("usuarioInicio")));
                    }
                }
                miAdaptador.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        miAdaptador = new AdaptadorTareas(getContext(), listaTareas);
        contenedorVista.setAdapter(miAdaptador);
        miAdaptador.setOnItemClickListener(new AdaptadorTareas.OnItemClickListener() {
            @Override
            public void onDeleteButtonClick(int position) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Mensaje Informativo");
                builder.setMessage("Vas a eliminar una tarea, si estás seguro haz clic en 'ELIMINAR'");
                builder.setIcon(R.drawable.info);
                builder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String clave= String.valueOf(listaTareas.get(position).getId()); //borra el modulo que este en la posicion que se ha seleccionado para borrar
                        dbRef.child(clave).removeValue();

                        listaTareas.remove(position);
                        miAdaptador.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("NO ELIMINAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        View padre=(View) view.getParent();
                        Snackbar barra= Snackbar.make(padre,"Has seleccionado no eliminar la tarea",Snackbar.LENGTH_SHORT);
                        barra.show();
                    }
                });
                builder.setNeutralButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        View padre=(View) view.getParent();
                        Snackbar barra= Snackbar.make(padre,"Has cancelado el proceso",Snackbar.LENGTH_SHORT);
                        barra.show();
                    }
                });
                AlertDialog cuadroDialogo = builder.create();
                cuadroDialogo.show();
            }
        });

        botonNuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaNuevaTarea = new Intent(getContext(), ActivityNuevaTarea.class);
                pantallaNuevaTarea.putExtras(usuario);
                startActivity(pantallaNuevaTarea);
            }
        });

        return view;
    }
}