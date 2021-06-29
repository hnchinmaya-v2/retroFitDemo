package com.example.retrofitdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class LoginFragment extends Fragment {

    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private Button loginButton;

    private LoginViewModel mViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        textInputUsername = v.findViewById(R.id.text_input_username);
        textInputPassword = v.findViewById(R.id.text_input_password);
        loginButton = v.findViewById(R.id.button_login);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mViewModel.getUserMutableLiveData().observe(requireActivity(), new Observer<List<UserData>>() {
            @Override
            public void onChanged(List<UserData> userData) {
                if (userData.isEmpty()) {
                    //Toast.makeText(requireActivity(),"Username or Password is incorrect",Toast.LENGTH_SHORT).show();
                    createDialog("Username or Password is incorrect");
                } else {
                    Toast.makeText(requireActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("login", true);
                    editor.apply();
                    requireActivity().finish();
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateUsername() && validatePassword()) {
                    String username = textInputUsername.getEditText().getText().toString().trim();
                    String password = textInputPassword.getEditText().getText().toString().trim();
                    mViewModel.getUser(username, password);
                }
            }
        });
        return v;
    }

    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Field can't be empty");
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public void createDialog(String displayMessage) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_alert, null);
        builder.setCancelable(true);
        builder.setView(v);
        TextView message, dismiss;
        message = (TextView) v.findViewById(R.id.tv_message);
        dismiss = (TextView) v.findViewById(R.id.tv_dismiss);
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