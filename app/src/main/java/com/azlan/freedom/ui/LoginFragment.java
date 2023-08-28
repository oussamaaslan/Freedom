package com.azlan.freedom.ui;

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
import com.azlan.freedom.models.User;
import com.azlan.freedom.tools.UserValidator;

import java.util.Objects;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    public LoginFragment() {
        // Required empty public constructor
    }

    private Listener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else {
            throw new ClassCastException(context.toString() + " You need to implement MyFragment.Listener");
        }
    }

    public interface Listener {
        void onSomethingHappened(String email,String password);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLogin.setOnClickListener(v -> {
            if(!checkValidation("Both")){
                listener.onSomethingHappened(binding.txtemail.getEditText().getText().toString(),
                        binding.txtPassword.getEditText().getText().toString());
            }
            else Toast.makeText(getContext(), "Invalid Email or Password Format.", Toast.LENGTH_SHORT).show();

    });
        //Checking email Format
        binding.txtemail.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                checkValidation("Email");
            }
        });
        binding.txtPassword.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                checkValidation("Password");
            }
        });
    }

   private boolean checkValidation(String checked){
        boolean isvalidate=false;
        if(Objects.equals(checked, "Email") || Objects.equals(checked, "Both")) {
            UserValidator.ValidationResult emailValidationResult = UserValidator.validateEmail(binding.txtemail.getEditText().getText().toString());
            if (!emailValidationResult.isValid()) {
                binding.txtemail.setError(emailValidationResult.getErrorMessage());
                isvalidate = true;

            } else binding.txtemail.setError(null);

        }
        if(Objects.equals(checked, "Password") || Objects.equals(checked, "Both")) {
            UserValidator.ValidationResult passwordValidationResult = UserValidator.validatePassword(binding.txtPassword.getEditText().getText().toString());
            if (!passwordValidationResult.isValid()) {
                binding.txtPassword.setError(passwordValidationResult.getErrorMessage());
                isvalidate = true;

            } else binding.txtPassword.setError(null);


        }
          return isvalidate;
    }
}