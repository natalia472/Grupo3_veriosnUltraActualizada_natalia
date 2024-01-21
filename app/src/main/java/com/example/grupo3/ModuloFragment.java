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
import tablas.Modulo;

public class ModuloFragment extends Fragment {
    private Bundle usuario = new Bundle();
    private DatabaseReference dbRef;
    private ArrayList<Modulo> listaModulos;
    private AdaptadorCards miAdaptador;
    private String nom,ciclo,usu;
    private ListView contenedorVista;

    public ModuloFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usuario=getArguments();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modulo, container, false);

        dbRef= FirebaseDatabase.getInstance().getReference().child("mod");

        contenedorVista = view.findViewById(R.id.listaCards);
        FloatingActionButton botonNuevoModulo=(FloatingActionButton) view.findViewById(R.id.floatingABmodulo);

        contenedorVista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//para que puedan seleccionarse de forma individual
        listaModulos=new ArrayList<>();

        if(!usuario.isEmpty()) {
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        nom = ds.child("modulo").getValue(String.class);
                        ciclo = ds.child("ciclo").getValue(String.class);
                        usu = ds.child("usuario").getValue(String.class);

                        if(usuario.getString("usuarioInicio").equals(usu)){
                            listaModulos.add(new Modulo(nom, ciclo, usu));
                        }
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
                    builder.setIcon(R.drawable.info);
                    builder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
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

            botonNuevoModulo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pantallaNuevoModulo=new Intent(getContext(), ActivityNuevoModulo.class);
                    pantallaNuevoModulo.putExtras(usuario);
                    startActivity(pantallaNuevoModulo);
                }
            });
        }

        return view;
    }
}