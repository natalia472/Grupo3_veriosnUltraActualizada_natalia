package com.example.grupo3;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
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
import Tablas.Modulo;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModuloFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModuloFragment extends Fragment {
    DatabaseReference dbRef;
    Modulo mod;
    ArrayList<Modulo> listaModulos;
    AdaptadorCards miAdaptador;
    String nom,ciclo,usu;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView contenedorVista;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ModuloFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AsignaturasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModuloFragment newInstance(String param1, String param2) {
        ModuloFragment fragment = new ModuloFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asignaturas, container, false);
        contenedorVista = view.findViewById(R.id.listaCards);
        dbRef= FirebaseDatabase.getInstance().getReference().child("mod");
        mod=new Modulo();

        contenedorVista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//para que puedan seleccionarse de forma individual
        listaModulos=new ArrayList<>();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //Modulo m=ds.getValue(Modulo.class);
                    nom = ds.child("modulo").getValue(String.class);
                    ciclo = ds.child("ciclo").getValue(String.class);
                    usu = ds.child("usuario").getValue(String.class);
                    listaModulos.add(new Modulo(nom, ciclo, usu));
                }
                miAdaptador.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        miAdaptador = new AdaptadorCards(getContext(),listaModulos);
        contenedorVista.setAdapter(miAdaptador);
        // Configurar el listener para eliminar la tarjeta al hacer clic en el botón
        miAdaptador.setOnItemClickListener(new AdaptadorCards.OnItemClickListener() {
            @Override
            public void onDeleteButtonClick(int position) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Mensaje Informativo");
                builder.setMessage("Vas a eliminar un módulo, si estás seguro haz clic en 'ELIMINAR'");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //borrar la card con firebase
                        //String nombreM=nombreModulo.getText().toString().trim();
                        //dbRef.child(position).removeValue();

                        // Utiliza tu lista (listaModulos) y el método remove() para eliminar el elemento
                        String clave=listaModulos.get(position).getModulo(); //borra el modulo que este en la posicion que se ha seleccionado para borrar
                        dbRef.child(clave).removeValue();
                        listaModulos.remove(position);
                        miAdaptador.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("NO ELIMINAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        View padre=(View) view.getParent();
                        Snackbar barra= Snackbar.make(padre,"Has seleccionado no eliminar el módulo",Snackbar.LENGTH_SHORT);
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
        FloatingActionButton botonNuevoModulo=(FloatingActionButton) view.findViewById(R.id.floatingABmodulo);
        botonNuevoModulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaNuevoModulo=new Intent(getContext(), ActivityNuevoModulo.class);
                startActivity(pantallaNuevoModulo);
            }
        });
        return view;
    }
}
