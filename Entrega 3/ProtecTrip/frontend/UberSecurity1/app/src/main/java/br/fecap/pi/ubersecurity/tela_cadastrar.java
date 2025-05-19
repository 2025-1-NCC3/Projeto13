package br.fecap.pi.ubersecurity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.fecap.pi.ubersecurity.R;

public class tela_cadastrar extends AppCompatActivity {

    private Button motorista, cadastrar, passageiro, btnCam;
    private EditText campoNome;
    private ImageView img;

    private Boolean verificaPassageiro;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_cadastrar);

        //procurando pelo id para "associar" um ao outro
        motorista = findViewById(R.id.btn_motorista);
        passageiro = findViewById(R.id.btn_passageiro);
        cadastrar = findViewById(R.id.btn_cadastrar);
        campoNome = findViewById(R.id.campoUsuario);
        btnCam = findViewById(R.id.btnCam);
        img = findViewById(R.id.imageViewAvatar);

        verificaPassageiro = true;

        motorista.setOnClickListener(view ->{
            //mudar tudo para o motorista (banco de dados em construção)
            verificaPassageiro = false;
            motorista.setTextColor(getResources().getColor(R.color.txt_btn));
            passageiro.setTextColor(getResources().getColor(R.color.txt_btn_fora));
            animarBotao(motorista);


        });

        passageiro.setOnClickListener(view ->{
            //mudar tudo para o passageiro (banco de dados em construção)
            verificaPassageiro = true;
            motorista.setTextColor(getResources().getColor(R.color.txt_btn_fora));
            passageiro.setTextColor(getResources().getColor(R.color.txt_btn));
            animarBotao(passageiro);


        });
        //funcionalidade do botão cadastrar
        cadastrar.setOnClickListener(view ->{
            String nome = campoNome.getText().toString();

            if (verificaPassageiro == true){
                Intent mudarTela = new Intent(this, home_screen.class);
                mudarTela.putExtra("nome", nome);
                startActivity(mudarTela);
            }

            else if (verificaPassageiro == false){
                Intent mudarTela = new Intent(this, home_motorista.class);
                mudarTela.putExtra("nome", nome);
                startActivity(mudarTela);
            }
        });

        //checando se tem a permissão para usar o código
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        }


        //funcionalidade do botão FOTO
        btnCam.setOnClickListener(new View.OnClickListener() {
            //abrir a camera caso clique no botão foto
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 101);
            }

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

    //selecionando a imagem para ser mostrada depois
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
        }
    }

}