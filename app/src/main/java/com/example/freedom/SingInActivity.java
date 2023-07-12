package com.example.freedom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.freedom.databinding.ActivitySingInBinding;
import com.example.freedom.tools.Connection;
import com.example.freedom.tools.UserValidator;

import java.util.Objects;

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
        Runnable  runnableStartLogin = new Runnable() {
            public void run() {
                binding.progressCircular.setVisibility(View.VISIBLE);
                binding.btnSingin.setVisibility(View.INVISIBLE);
            }
        };
        Runnable  runnableEndLogin = new Runnable() {
            public void run() {
                binding.progressCircular.setVisibility(View.INVISIBLE);
                binding.btnSingin.setVisibility(View.VISIBLE);
            }
        };
        binding.btnSingin.setOnClickListener(v -> {
            if( !checkvalidation("All")){
                runnableStartLogin.run();
                String username=binding.txtUsername.getEditText().getText().toString();
                String email=binding.txtEmail.getEditText().getText().toString();
                String password=binding.txtPassword.getEditText().getText().toString();
                String passwordConfirm=binding.txtConfirmPassword.getEditText().getText().toString();
                Connection.getInstance().createUser(email,password,this,runnableEndLogin);
                Toast.makeText(this,"login successfully",Toast.LENGTH_SHORT).show();
            }
            else
                runnableEndLogin.run();
        });


        binding.txtUsername.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    checkvalidation("Username");
            }
        });

        binding.txtEmail.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        binding.txtConfirmPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    checkvalidation("PasswordConfirm");
            }
        });
    }


    private boolean checkvalidation(String checked){
        boolean isvalidate=false;

        if(Objects.equals(checked, "Username")|| Objects.equals(checked, "All")){
            UserValidator.ValidationResult userNameValidationResult = UserValidator.validateUsername(binding.txtUsername.getEditText().getText().toString());
            if (!userNameValidationResult.isValid()) {
                binding.txtUsername.setError(userNameValidationResult.getErrorMessage());
                isvalidate=true;
            }
            else binding.txtUsername.setError(null);
        }
        if (Objects.equals(checked, "Email") || Objects.equals(checked, "All")) {
            UserValidator.ValidationResult emailValidationResult = UserValidator.validateEmail(binding.txtEmail.getEditText().getText().toString());
            if (!emailValidationResult.isValid()) {
                binding.txtEmail.setError(emailValidationResult.getErrorMessage());
                isvalidate=true;
            }
            else binding.txtEmail.setError(null);
        }


        if(Objects.equals(checked, "Password") || Objects.equals(checked, "All")){
            UserValidator.ValidationResult passwordValidationResult = UserValidator.validatePassword(binding.txtPassword.getEditText().getText().toString());
            if (!passwordValidationResult.isValid()) {
                binding.txtPassword.setError( passwordValidationResult.getErrorMessage());
                isvalidate=true;
            }
            else binding.txtPassword.setError(null);
        }
        if(Objects.equals(checked, "PasswordConfirm") || Objects.equals(checked, "All")){
            UserValidator.ValidationResult passwordConfirmValidationResult = UserValidator.validateConfirmPassword(binding.txtPassword.getEditText().getText().toString(),
                    binding.txtConfirmPassword.getEditText().getText().toString());
            if (!passwordConfirmValidationResult.isValid()) {
                binding.txtConfirmPassword.setError( passwordConfirmValidationResult.getErrorMessage());
                isvalidate=true;
            }
            else binding.txtPassword.setError(null);
        }

        return isvalidate;
    }
}