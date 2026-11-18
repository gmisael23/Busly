package com.busly.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.busly.app.databinding.ActivityLoginBinding;
import com.busly.app.util.Sessao;

/** Tela de login (Figura 1 do banner). */
public class LoginActivity extends AppCompatActivity {

    private static final int TAMANHO_MINIMO_SENHA = 6;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Sessao.isLogado(this)) {
            abrirPrincipal();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEntrar.setOnClickListener(v -> tentarLogin());
        binding.cbMostrarSenha.setOnCheckedChangeListener((botao, marcado) -> {
            binding.etSenha.setTransformationMethod(marcado
                    ? HideReturnsTransformationMethod.getInstance()
                    : PasswordTransformationMethod.getInstance());
            binding.etSenha.setSelection(binding.etSenha.getText().length());
        });
        configurarLinkCadastro();
    }

    private void tentarLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String senha = binding.etSenha.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError(getString(R.string.erro_email));
            binding.etEmail.requestFocus();
            return;
        }
        if (senha.length() < TAMANHO_MINIMO_SENHA) {
            binding.etSenha.setError(getString(R.string.erro_senha));
            binding.etSenha.requestFocus();
            return;
        }

        // Autenticação simulada. Futuramente: chamada ao backend.
        Sessao.entrar(this, email);
        abrirPrincipal();
    }

    private void abrirPrincipal() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /** Deixa apenas "Cadastre-se" colorido, em negrito e clicável. */
    private void configurarLinkCadastro() {
        String texto = getString(R.string.sem_conta);
        String link = getString(R.string.cadastre_se);
        int inicio = texto.indexOf(link);

        SpannableString spannable = new SpannableString(texto);
        if (inicio >= 0) {
            spannable.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(LoginActivity.this, R.string.cadastro_em_breve,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(ContextCompat.getColor(LoginActivity.this, R.color.busly_link));
                    ds.setFakeBoldText(true);
                    ds.setUnderlineText(false);
                }
            }, inicio, inicio + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        binding.tvCadastro.setText(spannable);
        binding.tvCadastro.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvCadastro.setHighlightColor(Color.TRANSPARENT);
    }
}
