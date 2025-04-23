package br.fecap.pi.ubersecurity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.fecap.pi.ubersecurity.R;

public class tela_entrar extends AppCompatActivity {

    private Button passageiro;
    private Button motorista;
    private Button entrar;
    private EditText campoNome;


    @SuppressLint({"ResourceAsColor", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_entrar);

        motorista = findViewById(R.id.btn_motorista);
        passageiro = findViewById(R.id.btn_passageiro);
        entrar = findViewById(R.id.btn_sign_in);
        campoNome = findViewById(R.id.campoUsuario);


        motorista.setOnClickListener(view ->{
            //mudar tudo para o motorista (banco de dados em construção)
            motorista.setTextColor(getResources().getColor(R.color.txt_btn));
            passageiro.setTextColor(getResources().getColor(R.color.txt_btn_fora));
            animarBotao(motorista);


        });

        passageiro.setOnClickListener(view ->{
            //mudar tudo para o passageiro (banco de dados em construção)
            motorista.setTextColor(getResources().getColor(R.color.txt_btn_fora));
            passageiro.setTextColor(getResources().getColor(R.color.txt_btn));
            animarBotao(passageiro);


        });

        //funcionalidade do botão cadastrar
        entrar.setOnClickListener(view ->{
            String nome = campoNome.getText().toString();

            Intent mudarTela = new Intent(this, home_screen.class);
            mudarTela.putExtra("nome", nome);
            startActivity(mudarTela);
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //animação do botão do passageiro e do botão do motorista quando clica
    private void animarBotao(Button botao) {
        ObjectAnimator aumentar = ObjectAnimator.ofFloat(botao, "scaleX", 1f, 1.1f);
        aumentar.setDuration(150);
        ObjectAnimator diminuir = ObjectAnimator.ofFloat(botao, "scaleX", 1.1f, 1f);
        diminuir.setDuration(150);

        aumentar.start();
        aumentar.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                diminuir.start();
            }
        });
    }

}