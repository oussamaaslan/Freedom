package com.azlan.freedom;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.azlan.freedom.databinding.FragmentLoginBinding;
import com.azlan.freedom.databinding.FragmentRegistrationBinding;
import com.azlan.freedom.tools.UserValidator;

import java.util.Objects;


public class registrationFragment extends Fragment {
    private registrationFragment.Listener listener;
    private FragmentRegistrationBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.Listener) {
            listener = (registrationFragment.Listener) context;
        } else {
            throw new ClassCastException(context.toString() + " You need to implement MyFragment.Listener");
        }
    }

    public interface Listener {
        void onSomethingHappened(String username,String email,String password);
    }

    public registrationFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSingin.setOnClickListener(v -> {
            String username=binding.txtUsername.getEditText().getText().toString();
            String email=binding.txtEmail.getEditText().getText().toString();
            String password=binding.txtPassword.getEditText().getText().toString();
            if(!checkvalidation("Both")){
                listener.onSomethingHappened(username,email,password);
            }
            else Toast.makeText(getContext(), "Invalid Credential Format.", Toast.LENGTH_SHORT).show();


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
            else binding.txtConfirmPassword.setError(null);
        }

        return isvalidate;
    }
}