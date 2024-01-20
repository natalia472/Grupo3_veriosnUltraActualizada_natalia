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
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TareasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TareasFragment extends Fragment {
    DatabaseReference dbRef;
    Tarea tar;
    ArrayList<Tarea> listaTareas;
    AdaptadorTareas miAdaptador;
    String mod,tarea;
    int idTarea;
    String fecha;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView contenedorVista;
    public TareasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TareasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TareasFragment newInstance(String param1, String param2) {
        TareasFragment fragment = new TareasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tareas, container, false);
        contenedorVista = view.findViewById(R.id.listaTareas);
        dbRef= FirebaseDatabase.getInstance().getReference().child("tarea");
        tar=new Tarea();

        contenedorVista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//para que puedan seleccionarse de forma individual
        listaTareas=new ArrayList<>();
        /*fecha=LocalDate.of(2022,12,12);
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        String fechita=sdf.format(fecha);*/

        //listaTareas.add(new Tarea("Juana","paula","12/12/2022"));


        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    fecha=ds.child("fechaEntrega").getValue(String.class);
                    idTarea=ds.child("id").getValue(Integer.class);
                    mod=ds.child("modulo").getValue(String.class);
                    tarea=ds.child("tarea").getValue(String.class);
                    listaTareas.add(new Tarea(idTarea,mod,tarea,fecha));
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
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Utiliza tu lista (listaModulos) y el método remove() para eliminar el elemento
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

        FloatingActionButton botonNuevaTarea = (FloatingActionButton) view.findViewById(R.id.floatingABtareas);
        botonNuevaTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaNuevaTarea = new Intent(getContext(), ActivityNuevaTarea.class);
                startActivity(pantallaNuevaTarea);
            }
        });

        return view;
    }

}