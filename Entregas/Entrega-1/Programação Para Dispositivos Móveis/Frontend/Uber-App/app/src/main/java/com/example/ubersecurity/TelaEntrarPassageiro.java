package com.example.ubersecurity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;


public class TelaEntrarPassageiro extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button loginButton, btnCadastrar;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_entrar_passageiro);

        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextTextPassword2);
        loginButton = findViewById(R.id.btn_entrar2);
        btnCadastrar = findViewById(R.id.cadastrar);
        requestQueue = Volley.newRequestQueue(this);

        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                // Validação Simples
                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(TelaEntrarPassageiro.this,"Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(email, password);

            }
        });
        // Configuração do botão "Cadastrar-se"
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaEntrarPassageiro.this, TelaCadastrarPassageiro.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password){
        String url = "https://api-uberap-novo.azurewebsites.net/login";

        JSONObject loginData = new JSONObject();
        try{
            loginData.put("email", email);
            loginData.put("senha", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST, url, loginData,
                new Response.Listener<JSONObject>(){
                    public void onResponse(JSONObject response){
                        try{
                            String status = response.getString("status");
                            if (status.equals("success")){
                                // Redireciona para a tela funcional
                                Intent intent = new Intent(TelaEntrarPassageiro.this, TelaCadastrarPassageiro.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(TelaEntrarPassageiro.this, "Login falhou. Verifique suas credenciais.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Toast.makeText(TelaEntrarPassageiro.this, "Erro na conexão com o servidor", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(loginRequest);
    }



}
