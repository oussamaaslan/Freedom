package com.example.freedom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.freedom.databinding.ActivityLoginBinding;
import com.example.freedom.databinding.ActivityMainBinding;
import com.example.freedom.tools.Connection;
import com.example.freedom.tools.UserValidator;

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


          Runnable  runnableStartLogin = new Runnable() {
            public void run() {
                binding.progressCircular.setVisibility(View.VISIBLE);
                binding.btnLogin.setVisibility(View.INVISIBLE);
            }
        };
        Runnable  runnableEndLogin = new Runnable() {
            public void run() {
                binding.progressCircular.setVisibility(View.INVISIBLE);
                binding.btnLogin.setVisibility(View.VISIBLE);
            }
        };
        binding.btnLogin.setOnClickListener(v -> {
            if( !checkvalidation("Both")){
                runnableStartLogin.run();
                String email=binding.txtemail.getEditText().getText().toString();
                String password=binding.txtPassword.getEditText().getText().toString();
                Connection.getInstance().login(email,password,this,runnableEndLogin);
                Toast.makeText(this,"login successfully",Toast.LENGTH_SHORT).show();
            }
            else
                runnableEndLogin.run();
        });


        binding.txtemail.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    checkvalidation("Email");
            }
        });


        binding.txtPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    checkvalidation("Password");
            }
        });
    }

    private boolean checkvalidation(String checked){
        boolean isvalidate=false;
        if(checked=="Email"||checked=="Both") {
            UserValidator.ValidationResult emailValidationResult = UserValidator.validateEmail(binding.txtemail.getEditText().getText().toString());
            if (!emailValidationResult.isValid()) {
                binding.txtemail.setError(emailValidationResult.getErrorMessage());
                isvalidate = true;
            } else binding.txtemail.setError(null);
        }
        if(checked=="Password"||checked=="Both") {
            UserValidator.ValidationResult passwordValidationResult = UserValidator.validatePassword(binding.txtPassword.getEditText().getText().toString());
            if (!passwordValidationResult.isValid()) {
                binding.txtPassword.setError(passwordValidationResult.getErrorMessage());
                isvalidate = true;
            } else binding.txtPassword.setError(null);

        }
        return isvalidate;
    }
}