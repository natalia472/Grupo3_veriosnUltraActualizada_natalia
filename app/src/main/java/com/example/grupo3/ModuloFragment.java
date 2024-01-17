package com.example.grupo3;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    TextView nombreM, cicloM;
    View card;
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
        dbRef= FirebaseDatabase.getInstance().getReference().child("mod").child("mod1");
        mod=new Modulo();
        //card=inflater.inflate(R.layout.cards, container, false);
        //nombreM=card.findViewById(R.id.nombreModulo);
        //cicloM=card.findViewById(R.id.ciclo);
        contenedorVista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//para que puedan seleccionarse de forma individual

        listaModulos=new ArrayList<>();

        //Query consulta= dbRef.orderByChild("modulo").equalTo(nom);
        //listaModulos.add(new Modulo(nom,ciclo,usu));
        /*nom=mod.getModulo();
        ciclo=mod.getCiclo();
        usu=mod.getUsuario();
        listaModulos.add(new Modulo(mod.getModulo(),mod.getCiclo(),mod.getUsuario()));*/
        //listaModulos.add(new Modulo("manualidades","DAM","pepe"));
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //Modulo m=ds.getValue(Modulo.class);
                    /*nom=m.getModulo();
                    ciclo=m.getCiclo();
                    usu=m.getUsuario();
                    listaModulos.add(new Modulo(mod.getModulo(),mod.getCiclo(),mod.getUsuario()));*/
                    //listaModulos.add(m);
                    nom = ds.child("modulo").getValue(String.class);
                    ciclo = ds.child("ciclo").getValue(String.class);
                    usu = ds.child("usuario").getValue(String.class);
                    listaModulos.add(new Modulo(nom, ciclo, usu));
                }
                //listaModulos.add(new Modulo("Programacion","DAM","Joseeeeeee"));
                actualizarInterfazDeUsuario();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        /*consulta.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Modulo m=snapshot.getValue(Modulo.class);

                for(DataSnapshot ds:snapshot.getChildren()){
                    Modulo m=ds.getValue(Modulo.class);
                    nom=m.getModulo();
                    ciclo=m.getCiclo();
                    usu=m.getUsuario();
                   listaModulos.add(new Modulo(mod.getModulo(),mod.getCiclo(),mod.getUsuario()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        //listaModulos.add(new Modulo(nom,ciclo,usu));
        //listaModulos.add(mod);
        /*
        listaModulos.add(new Modulo("Programacion","DAM","Jose"));
        listaModulos.add(new Modulo("Acceso datos","DAW", "Jose"));
        listaModulos.add(new Modulo("PSP","DAM", "Jose"));
        listaModulos.add(new Modulo("Bases de datos","DAW", "Jose"));
        listaModulos.add(new Modulo("Desarrollo interfaces","DAM", "Jose"));
        listaModulos.add(new Modulo("Ingles","DAW", "Jose"));
        listaModulos.add(new Modulo("Sistemas gestion","DAM", "Jose"));
*/
        miAdaptador = new AdaptadorCards(getContext(),listaModulos);
        contenedorVista.setAdapter(miAdaptador);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView nombreModulo= (TextView) view.findViewById(R.id.nombreModulo);
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
                        /*String nombreM=nombreModulo.getText().toString().trim();
                        dbRef.child(nombreM).removeValue();*/

                        // Utiliza tu lista (listaModulos) y el método remove() para eliminar el elemento
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
    private void actualizarInterfazDeUsuario() {
        // Crea un adaptador para mostrar la lista de módulos en tu interfaz de usuario
        miAdaptador = new AdaptadorCards(getContext(), listaModulos);
        contenedorVista.setAdapter(miAdaptador);
    }

    /**
     * segun de donde venga el resultado se cogera el bundle que
     * corresponda. Si es 1, cogera el bundle de la actividad
     * 'actividadnuevomodulo' y llama a un metodo que actualiza el
     * arraylist de modulos para insertarlo en el mismo

    public void onActivityResult(int requestCode,int resultCode, Intent datos) {
        super.onActivityResult(requestCode, resultCode, datos);
        if (requestCode == 1) { //comprueba que el resultado proviene de 'activityNuevoModulo', declarado antes como 1
            if (resultCode == RESULT_OK) { //comprueba que la actividad anterior se ha realizado correctamente
                // Se ejecuta cuando se ha añadido un nuevo módulo
                actualizarListaModulos(); //llama al metodo para actualizar el arraylist
            }
        }
    }
    // Metodo para actualizar la lista de modulos desde la base de datos
    private void actualizarListaModulos() {
        //limpiar la lista actual antes de hacer una nueva consulta
        listaModulos.clear();

        //hacer la consulta a la base de datos
        Query consulta = dbRef.orderByChild("ciclo");
        consulta.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Modulo mod = ds.getValue(Modulo.class);
                    listaModulos.add(mod); //añadir el modulo a la lista
                }

                //notificar al adaptador que los datos han cambiado
                miAdaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }*/

}
