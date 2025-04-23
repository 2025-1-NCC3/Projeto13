package br.fecap.pi.ubersecurity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.fecap.pi.ubersecurity.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

    }
    //métodos que fazem mudar a tela
    public void mudar_tela_entrar(View view){
        Intent entrar = new Intent (MainActivity.this, tela_entrar.class);
        startActivity(entrar);
    }
    public void mudar_tela_cadastrar(View view){
        Intent cadastrar = new Intent (MainActivity.this, tela_cadastrar.class);
        startActivity(cadastrar);
    }

}