package com.example.freedom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.freedom.databinding.ActivitySingInBinding;
import com.example.freedom.tools.Connection;

public class SingInActivity extends AppCompatActivity {
private ActivitySingInBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivitySingInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left );
        binding.txtsingin.setOnClickListener(v -> {
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        });

        binding.btnSingin.setOnClickListener(v -> {
            String email=binding.txtEmail.getEditText().getText().toString();
            String password=binding.txtPassword.getEditText().getText().toString();
            Connection.getInstance().createUser(email,password,this);
        });
    }
}