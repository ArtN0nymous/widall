package com.ramon.widdall3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ramon.widdall3.Entidades.Usuario;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileStore;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    private CircleImageView fotoperfil;
    private TextView txtmensaje;
    private RecyclerView rvmensaje;
    private Button btenviar;
    private TextView nombre;
    private ImageButton btenviarfoto;
    private ImageButton camara;
    private ImageView reaccion;

    private Window window;
    private ProgressDialog dialog;
    String currentPhotoPath;
    String path;
    MediaPlayer md;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    final int PHOTO_CONST = 3;
    private static final int PHOTO_SEND=1;
    private static final int PHOTO_PERFIL=2;
    private static final int PHOTO_PAPER=4;
    private String NOMBRE_LOG;
    private String fotoperfilCadena;
    private FirebaseAuth mAuth;

    private String code;

    AlertDialog sentfotoDialog;
    AlertDialog actualizarfotoDialog;
    AlertDialog cargando;
    String urlimagen;

    Toolbar toolbarr;

    private Adaptermensaje adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fotoperfil = (CircleImageView) findViewById(R.id.fotoperfil);
        txtmensaje = (TextView) findViewById(R.id.txtmensaje);
        rvmensaje = (RecyclerView) findViewById(R.id.rvmensajes);
        btenviar = (Button) findViewById(R.id.btenviar);
        nombre= (TextView) findViewById(R.id.nombre);
        reaccion=(ImageView)findViewById(R.id.reaccion);
        md = MediaPlayer.create(this,R.raw.clic);

        toolbarr = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarr);
        mAuth = FirebaseAuth.getInstance();

        sentfotoDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Cargando imagen...")
                .setCancelable(false).build();
        actualizarfotoDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Actualizando foto de perfil...")
                .setCancelable(false)
                .build();
        cargando = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Cargando Mensajes")
                .setCancelable(false)
                .build();



        btenviarfoto=(ImageButton) findViewById(R.id.enviarfoto);
        camara=(ImageButton)findViewById(R.id.camara);
        fotoperfilCadena="";



        adapter = new Adaptermensaje(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvmensaje.setLayoutManager(l);
        rvmensaje.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        code = getIntent().getStringExtra("codigo");
        databaseReference = database.getReference(code.toString()); //ultima version
        storage = FirebaseStorage.getInstance();

        //recibirdatos();
        //String primary = "#f58024";
        //String background = "#f79c55";
        //String primaryDark = "#f49a11";
        //cambiarColor(primaryDark,primary,background);
        //enviardatos();

        btenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtmensaje.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Escriba un mensaje", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.push().setValue(new MensajeEnviar(txtmensaje.getText().toString(),NOMBRE_LOG,urlimagen,"1", ServerValue.TIMESTAMP));
                    md.start();
                }
                txtmensaje.setText(null);

            }
        });

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        btenviarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"Selecciona una imagen"),PHOTO_SEND);
            }
        });
        fotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"Selecciona una imagen"),PHOTO_PERFIL);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MensajeRecibir m= snapshot.getValue(MensajeRecibir.class);
                adapter.addmensaje(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = crearFoto();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.ramon.widdall3",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PHOTO_CONST);
            }
        }
    }
    private File crearFoto() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();


        return image;
    }
    private void galleryAddPic() {
        //Devuelve el archivo creado de la camara a la galeria de android
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setScrollbar(){
        rvmensaje.scrollToPosition(adapter.getItemCount()-1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_CONST && resultCode == RESULT_OK){
            //CARAMA
            sentfotoDialog.show();
            File temp = new File(currentPhotoPath);
            final Uri u = Uri.fromFile(temp);
            //final Uri o = Uri.parse(path);
            storageReference=storage.getReference("Pictures");//Imagenes del chat
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
                        String urlimagen1 = downloadUri.toString();
                        MensajeEnviar m = new MensajeEnviar(NOMBRE_LOG+" ha enviado una foto",NOMBRE_LOG,urlimagen,"2",urlimagen1,ServerValue.TIMESTAMP);
                        databaseReference.push().setValue(m);
                        sentfotoDialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else if(requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            sentfotoDialog.show();
            final Uri u = data.getData();
            storageReference=storage.getReference("Pictures");//Imagenes del chat
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
                        String urlimagen1 = downloadUri.toString();
                        MensajeEnviar m = new MensajeEnviar(NOMBRE_LOG+" ha enviado una foto",NOMBRE_LOG,urlimagen,"2",urlimagen1,ServerValue.TIMESTAMP);
                        databaseReference.push().setValue(m);
                        sentfotoDialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if(requestCode == PHOTO_PERFIL && resultCode == RESULT_OK){
            actualizarfotoDialog.show();
            final Uri u = data.getData();
            storageReference=storage.getReference("Foto_perfil");//Imagenes de perfil
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
                        MensajeEnviar m = new MensajeEnviar(NOMBRE_LOG+" ha actualizado su foto de perfil",NOMBRE_LOG,urlimagen,"2",downloadUri.toString(),ServerValue.TIMESTAMP);
                        databaseReference.push().setValue(m);
                        actualizar();
                        onResume();
                        //Glide.with(MainActivity.this).load(urlimagen).into(fotoperfil);
                        actualizarfotoDialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            returnlogin();
        } else if(id == R.id.buscar){
            Toast.makeText(MainActivity.this, "BUSCANDO", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            cargando.show();

            //Recupera la informacion de usuario de la base de datos

            btenviar.setEnabled(false);

            DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    urlimagen = usuario.getUserperfil();
                    NOMBRE_LOG = usuario.getNombre();
                    nombre.setText(NOMBRE_LOG);
                    Glide.with(MainActivity.this).load(urlimagen).into(fotoperfil);
                    cargando.dismiss();
                    btenviar.setEnabled(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else {
            returnlogin();
        }
    }
    private void returnlogin(){
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }
    private void actualizar(){
        try {
            //Actualiza la url de la foto de perfil del usuario
            Map<String,Object>map = new HashMap<>();
            map.put("userperfil",urlimagen);
            FirebaseUser currentUser = mAuth.getCurrentUser();
            DatabaseReference reference =  database.getReference("Usuarios/"+currentUser.getUid());
            reference.updateChildren(map);
        }catch (Exception e){
            Toast.makeText(MainActivity.this,"Error al actualizar",Toast.LENGTH_LONG).show();
        }
    }



}