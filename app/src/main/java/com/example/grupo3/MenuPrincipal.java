package com.example.grupo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MenuPrincipal extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, Toolbar.OnMenuItemClickListener {
    private Bundle usuario;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        usuario = getIntent().getExtras();
        navController = Navigation.findNavController(this, R.id.navContainer);

        MaterialToolbar encabezado = findViewById(R.id.encabezadoMenuPrincipal);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationMenuPrincipal);

        encabezado.setOnMenuItemClickListener(this);
        bottomNavigation.setOnItemSelectedListener(this);





    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        boolean realizado = false;
        if (item.getItemId() == R.id.itemPerfil) {
            realizado = true;
            Intent actividadPerfil = new Intent(MenuPrincipal.this, Perfil.class);
            actividadPerfil.putExtras(usuario);
            startActivity(actividadPerfil);
        } else if (item.getItemId() == R.id.itemAceraDe) {
            realizado = true;
            Intent actividadAcercaDe = new Intent(MenuPrincipal.this, AcercaDe.class);
            actividadAcercaDe.putExtras(usuario);
            startActivity(actividadAcercaDe);
        } else if (item.getItemId() == R.id.itemCerrarSesion) {
            realizado = true;
            Intent actividadLogin = new Intent(MenuPrincipal.this, Login.class);
            startActivity(actividadLogin);
        }
        return realizado;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean realizado = false;
        if (item.getItemId() == R.id.itemAsignaturas) {
            realizado = true;
            navController.navigate(R.id.fragmentAsignaturas, usuario);
        } else if (item.getItemId() == R.id.itemTareas) {
            realizado = true;
            navController.navigate(R.id.fragmentTareas, usuario);
        }
        return realizado;
    }
}