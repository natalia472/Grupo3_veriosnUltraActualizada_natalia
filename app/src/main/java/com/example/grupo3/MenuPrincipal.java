package com.example.grupo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MenuPrincipal extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, Toolbar.OnMenuItemClickListener {
    private Bundle usuario;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        usuario = getIntent().getExtras(); //coge los datos del bundle anterior
        navController = Navigation.findNavController(this, R.id.navContainer);

        MaterialToolbar encabezado = findViewById(R.id.encabezadoMenuPrincipal);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationMenuPrincipal);

        encabezado.setOnMenuItemClickListener(this);
        bottomNavigation.setOnItemSelectedListener(this);

        navController.navigate(R.id.fragmentModulos, usuario);
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
            /*Esta linea de codigo cierra sesion con el usuario actual para que
            una vez seleccionada la opcion de cerrar sesion, se desvincule y pueda
            dar paso a un nuevo usuario registrado para luego mostrar sus datos en
            el perfil de usuario */
            FirebaseAuth.getInstance().signOut();
            Intent actividadLogin = new Intent(MenuPrincipal.this, Login.class);
            startActivity(actividadLogin);
            finish(); //para asegurarse de que el usuario no pueda volver atras
        }
        return realizado;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean realizado = false;
        if (item.getItemId() == R.id.itemModulos) {
            realizado = true;
            navController.navigate(R.id.fragmentModulos, usuario);
        } else if (item.getItemId() == R.id.itemTareas) {
            realizado = true;
            navController.navigate(R.id.fragmentTareas, usuario);
        }
        return realizado;
    }
}