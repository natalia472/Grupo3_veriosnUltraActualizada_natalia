package com.example.grupo3;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import tablas.Tarea;

public class AdaptadorTareas extends ArrayAdapter<Tarea> {
    private ArrayList<Tarea> lista;
    private SparseBooleanArray selectedItems; // Para almacenar la información de selección
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public AdaptadorTareas(Context contexto, ArrayList<Tarea> lista) {
        super(contexto, R.layout.tareas, lista);
        this.lista=lista;
        selectedItems = new SparseBooleanArray();
    }

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
        LayoutInflater mostrado = LayoutInflater.from(getContext());
        View elemento = mostrado.inflate(R.layout.tareas, parent, false);

        TextView nombreModulo = elemento.findViewById(R.id.moduloTareas);
        TextView nombreTarea = elemento.findViewById(R.id.nombreTarea);
        TextView fechaSeleccionada = elemento.findViewById(R.id.fechaEntregaTarea);
        MaterialButton botonEliminarTarea = elemento.findViewById(R.id.botonEliminarTarea);

        //Asociamos elementos
        nombreModulo.setText(lista.get(position).getModulo());
        nombreTarea.setText(lista.get(position).getTarea());
        fechaSeleccionada.setText(lista.get(position).getFechaEntrega());

        // Verifica si el elemento está seleccionado y cambia su apariencia
        if (selectedItems.get(position)) {
            elemento.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_light));
        } else {
            elemento.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
        }

        botonEliminarTarea.setOnClickListener(new View.OnClickListener() {
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
