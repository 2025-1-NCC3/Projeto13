package com.example.ubersecurity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class TelaCadastrarPassageiro extends AppCompatActivity {

    private EditText emailField, nameField, passwordField;
    private Button registerButton;
    private RequestQueue requestQueue;  // Definindo o requestQueue como variável de instância

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastrar_passageiro);

        // Inicializando os campos de entrada
        emailField = findViewById(R.id.editTextEmail);
        nameField = findViewById(R.id.editTextText2);
        passwordField = findViewById(R.id.editTextTextPassword2);
        registerButton = findViewById(R.id.btn_entrar2);

        // Inicializando o RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Configurando o botão de cadastro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pegando os dados inseridos no formulário
                String email = emailField.getText().toString();
                String name = nameField.getText().toString();
                String password = passwordField.getText().toString();

                // Verificação de campos vazios
                if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(TelaCadastrarPassageiro.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chamando o método para registrar o usuário
                registerUser(name, email, password);
            }
        });
    }

    // Método para registrar o usuário
    private void registerUser(String name, String email, String password) {
        String url = "https://api-uberap-novo.azurewebsites.net/register"; // URL do seu backend na Azure

        // Criando o objeto JSON com os dados do usuário
        JSONObject registerData = new JSONObject();
        try {
            registerData.put("nome", name);  // Alterado para "nome" conforme o seu servidor
            registerData.put("email", email);
            registerData.put("senha", password);

            Log.d("API", "Dados enviados: " + registerData.toString());  // Log para ver os dados

            // Faça a requisição
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, registerData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("API", "Resposta: " + response.toString());  // Log para ver a resposta
                            try {
                                String status = response.getString("status");
                                if ("success".equals(status)) {
                                    Toast.makeText(TelaCadastrarPassageiro.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                                    finish(); // Volta para a tela de login
                                } else {
                                    Toast.makeText(TelaCadastrarPassageiro.this, "Erro ao cadastrar. Tente novamente.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("API", "Erro na requisição: " + error.toString());  // Log para mostrar o erro
                            Toast.makeText(TelaCadastrarPassageiro.this, "Erro na conexão com o servidor", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    // Adicionando o cabeçalho Content-Type: application/json
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            // Adicionando a requisição à fila
            requestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("API", "Erro ao construir o JSON: " + e.getMessage());
        }
    }
}
