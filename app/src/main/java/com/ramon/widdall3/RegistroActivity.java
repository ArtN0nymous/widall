package com.ramon.widdall3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.CaseMap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ramon.widdall3.Entidades.Usuario;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistroActivity extends AppCompatActivity {

    private EditText txtNombre, txtCorreo, txtPass, txtPass2;
    private Button btnRegistrar;
    private ImageButton regresar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase Database;
    //private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private CircleImageView userperfil;
    private CircleImageView userperfil2;
    private static final int PHOTO_PERFIL=2;
    String currentPath;
    String  urlimagen;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtNombre=(EditText)findViewById(R.id.registronombre);
        txtCorreo=(EditText)findViewById(R.id.registroemail);
        txtPass=(EditText)findViewById(R.id.registropass);
        txtPass2=(EditText)findViewById(R.id.registropass2);
        btnRegistrar=(Button)findViewById(R.id.btnregistrar);
        regresar=(ImageButton)findViewById(R.id.back_login);
        userperfil = (CircleImageView) findViewById(R.id.registroperfil);
        userperfil2=(CircleImageView)findViewById(R.id.registroperfil2);
        mAuth = FirebaseAuth.getInstance();

        Database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        //databaseReference = Database.getReference("Usuarios");

        userperfil2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"Selecciona una imagen"),PHOTO_PERFIL);
            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = txtCorreo.getText().toString();
                final String nombre = txtNombre.getText().toString();
                //Ciclo for para identificar los espacios en la cadena y borrarlos.
                String name = null;
                for (int x = 0; x < nombre.length(); x++) {
                    name = nombre.replaceAll("\\s", "");}
                //se guarda en una nueva variable, las cadenas son inmutables
                final String namefinal = name;
                if(isValidEmail(mail) && validpass() && validnombre(namefinal)){
                    String pass = txtPass.getText().toString();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(RegistroActivity.this,"Registro completado", Toast.LENGTH_SHORT).show();
                                Usuario usuario = new Usuario();
                                usuario.setCorreo(mail);
                                usuario.setNombre(namefinal);
                                usuario.setUserperfil(urlimagen);
                                if(urlimagen != null){
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    DatabaseReference reference = Database.getReference("Usuarios/"+currentUser.getUid());
                                    reference.setValue(usuario);
                                    finish();
                                    //Bundle extras = new Bundle();
                                    //extras.putString("perfil",urlimagen);
                                    //Intent i = new Intent(RegistroActivity.this, MainActivity.class);
                                    //i.putExtras(extras);
                                    //startActivity(i);
                                }else {
                                    Toast.makeText(RegistroActivity.this,"AGREGUE UNA FOTO DE PERFIL",Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegistroActivity.this, "ERROR AL REGISTRARSE intente de nuevo mas tarde ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegistroActivity.this,"Error verifique sus datos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_PERFIL && resultCode == RESULT_OK) {
            final Uri u = data.getData();
            //final Uri u = Uri.parse(currentPhotoPath);
            storageReference=storage.getReference("Foto_perfil");//Imagenes perfil
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fotoReferencia.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        urlimagen = downloadUri.toString();
                        Glide.with(RegistroActivity.this).load(urlimagen).into(userperfil);
                        Glide.with(RegistroActivity.this).load(urlimagen).into(userperfil2);
                    } else {
                        Toast.makeText(RegistroActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public boolean validpass(){
        String pass1,pass2;
        pass1 = txtPass.getText().toString();
        pass2 = txtPass2.getText().toString();
        if (pass1.equals(pass2)){
            if (pass1.length()>=6 && pass1.length()<=16){
                return true;
            } else return false;
        } else return false;
    }
    public boolean validnombre(String nombre){
        return !nombre.isEmpty();
    }
}
