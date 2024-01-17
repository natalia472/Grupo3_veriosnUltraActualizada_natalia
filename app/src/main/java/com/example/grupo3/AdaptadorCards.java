package com.example.grupo3;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import Tablas.Modulo;

public class AdaptadorCards extends ArrayAdapter<Modulo> {

    private ArrayList<Modulo> lista;
    private SparseBooleanArray selectedItems; // Para almacenar la información de selección
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public AdaptadorCards(Context contexto, ArrayList<Modulo> lista) {
        super(contexto, R.layout.cards, lista);
        this.lista=lista;
        selectedItems = new SparseBooleanArray();
    }

    // Método para actualizar la selección del elemento
    public void setItemSelected(int position, boolean isSelected) {
        selectedItems.put(position, isSelected);
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onDeleteButtonClick(int position);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater mostrado= LayoutInflater.from(getContext());
        View elemento= mostrado.inflate(R.layout.cards, parent, false);

        TextView nombreModulo= (TextView) elemento.findViewById(R.id.nombreModulo);
        TextView cicloModulo= (TextView) elemento.findViewById(R.id.ciclo);

        nombreModulo.setText(lista.get(position).getModulo());
        cicloModulo.setText(lista.get(position).getCiclo());



        // Verifica si el elemento está seleccionado y cambia su apariencia
        if (selectedItems.get(position)) {
            elemento.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_light));
        } else {
            elemento.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
        }

        MaterialButton botonEliminarModulo = elemento.findViewById(R.id.botonEliminarModulo);
        botonEliminarModulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notificar al fragmento para eliminar la tarjeta en la posición "position"
                if (onItemClickListener != null) {
                    onItemClickListener.onDeleteButtonClick(position);
                }
            }
        });

        return elemento;
    }
}
