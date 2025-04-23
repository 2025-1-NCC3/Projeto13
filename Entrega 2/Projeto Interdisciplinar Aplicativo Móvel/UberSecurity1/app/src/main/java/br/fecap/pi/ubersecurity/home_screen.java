package br.fecap.pi.ubersecurity;

import android.annotation.SuppressLint;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.fecap.pi.ubersecurity.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class home_screen extends AppCompatActivity {
    //criando as variáveis que estão no xml
    private TextView textoPadrao;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private Button btnCam, btnCall, btnProcurar;
    private EditText editTextDestino;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);

        //procurando pelo id para "associar" um ao outro
        textoPadrao = findViewById(R.id.textPadrao);
        mapView = findViewById(R.id.mapView);
        btnCam = findViewById(R.id.btnCamera);
        btnCall = findViewById(R.id.btnCall);
        editTextDestino = findViewById(R.id.editTextDestino);
        btnProcurar = findViewById(R.id.btnProcurar);


        // Inicializar o FusedLocationProviderClient para o mapa
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
/*
        btnProcurar.setOnClickListener(v -> {
            String destino = editTextDestino.getText().toString();
            if (!destino.isEmpty()) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(destino));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

*/

        //coloca o textoPadrao na frente
        textoPadrao.bringToFront();
        btnCam.bringToFront();

        //funcionalidade da camera (nao funcionando ainda)
        btnCam.setOnClickListener(v -> {
            Intent mudarTela = new Intent(this, Camera.class);
            startActivity(mudarTela);
        });


        //botão de ligar para a polícia
        btnCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:190"));

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                startActivity(callIntent);
            }
        });



        //pega o Extra deixado na tela de cadastrar e na de entrar
        String nome = getIntent().getStringExtra("nome");
        textoPadrao.setText("Olá, " + nome + "! Para onde vamos hoje?");

        //Esse código é do mapa
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(map -> {
            googleMap = map;

            // Verificar permissões de localização
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);

                // Obter a última localização
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        });

        //ajustar a tela para não sobrepor as barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
