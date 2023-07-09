package com.example.freedom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.freedom.databinding.ActivityLoginBinding;
import com.example.freedom.databinding.ActivityMainBinding;
import com.example.freedom.tools.Connection;

public class LoginActivity extends AppCompatActivity {
private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right );
        binding.txtsingin.setOnClickListener(v -> {
            Intent intent = new Intent(this,SingInActivity.class);
            startActivity(intent);
            finish();
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email=binding.txtUsername.getEditText().getText().toString();
            String password=binding.txtPassword.getEditText().getText().toString();
            Connection.getInstance().login(email,password,this);
        });
    }
}