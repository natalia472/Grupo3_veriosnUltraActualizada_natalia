package com.example.grupo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

public class AcercaDe extends AppCompatActivity implements View.OnClickListener{
    private Bundle usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        usuario = getIntent().getExtras();

        MaterialToolbar encabezado = findViewById(R.id.encabezadoAcercaDe);

        encabezado.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent actividadMenuPrincipal = new Intent(AcercaDe.this, MenuPrincipal.class);
        actividadMenuPrincipal.putExtras(usuario);
        startActivity(actividadMenuPrincipal);
    }
}