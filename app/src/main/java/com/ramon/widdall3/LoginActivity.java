package com.ramon.widdall3;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bugsee.library.Bugsee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ramon.widdall3.Entidades.Chat;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {
    private EditText txtcorreo;
    private EditText txtpass;
    private Button btnlogin;
    private Button btnregistrar;
    private Switch aSwitch;
    private FirebaseAuth mAuth;
    private AlertDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtcorreo = (EditText)findViewById(R.id.loginmail);
        txtpass=(EditText)findViewById(R.id.loginpass);
        btnlogin=(Button)findViewById(R.id.loginlogin);
        aSwitch=(Switch)findViewById(R.id.show_pass2);
        btnregistrar=(Button)findViewById(R.id.loginregistrar);
        mAuth = FirebaseAuth.getInstance();
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Iniciando Sesión")
                .setCancelable(false).build();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    txtpass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    txtpass.setInputType(129);
                }
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = txtcorreo.getText().toString();
                if(isValidEmail(correo) && validpass()){
                    dialog.show();
                    String pass = txtpass.getText().toString();

                    mAuth.signInWithEmailAndPassword(correo, pass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        dialog.dismiss();
                                        startActivity(new Intent(LoginActivity.this,ChatsActivity.class));
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Error usuario o password incorrecta", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });

                }else {
                    Toast.makeText(LoginActivity.this, "Error de login verifique sus datos", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistroActivity.class));
            }
        });
    }
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public boolean validpass(){
        String pass;
        pass = txtpass.getText().toString();
        if (pass.length()>=6 && pass.length()<=16){
            return true;
        } else return false;
    }

    @Override

    //LLama los datos de la base de datos
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Toast.makeText(LoginActivity.this, "Logging in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, ChatsActivity.class));
            finish();
        }
    }
}
