package com.example.retrofitdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class SignUpFragment extends Fragment {

    private SignUpViewModel mViewModel;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private Button signupButton;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        textInputEmail = v.findViewById(R.id.text_input_email);
        textInputUsername = v.findViewById(R.id.text_input_username);
        textInputPassword = v.findViewById(R.id.text_input_password);
        mViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        mViewModel.getUsernameTaken().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUsernameTaken) {
                if (isUsernameTaken) {
                    createDialog("Username already taken");
                    //Toast.makeText(requireActivity(), "Username taken",Toast.LENGTH_SHORT).show();
                } else {
                    createDialog("Signup Successful, Please login to continue.");
                    //Toast.makeText(requireActivity(),"Signup Successful",Toast.LENGTH_SHORT).show();
                }
            }
        });
        signupButton = v.findViewById(R.id.button_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail() && validateUsername() && validatePassword()) {
                    String email = textInputEmail.getEditText().getText().toString().trim();
                    String username = textInputUsername.getEditText().getText().toString().trim();
                    String password = textInputPassword.getEditText().getText().toString().trim();
                    mViewModel.insertUserData(username, password, email);
                }
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid email address");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            textInputUsername.setError("Username too long");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    public void createDialog(String displayMessage) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_alert, null);
        builder.setCancelable(true);
        builder.setView(v);
        TextView message, dismiss;
        message = v.findViewById(R.id.tv_message);
        dismiss = v.findViewById(R.id.tv_dismiss);
        AlertDialog alertDialog = builder.create();
        message.setText(displayMessage);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}