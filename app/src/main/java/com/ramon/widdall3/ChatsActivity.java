package com.ramon.widdall3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.ramon.widdall3.Entidades.Chat;
import com.ramon.widdall3.Entidades.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsActivity extends AppCompatActivity {
    private CircleImageView fotoUserChat;
    private TextView nombreUserChat;
    private RecyclerView rvChats;
    private Button nuevochat;
    private AdapterChats adapter;
    private CardView cardChat;
    private ImageButton btnCancelar;
    private ImageButton btnGuardar;

    private String urlimagen;
    private String urlimagen_chat;
    private String NOMBRE_LOG;
    //private String USER_LOG;

    private String nameChat;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private String codigo;
    private TextView txtcodigochat;
    private ImageView image_codigo_chat;
    private ImageButton imageButton_codigo_chat;

    private static final int PHOTO_SEND=1;

    private EditText codigo_nombre_chat;
    ArrayList<Chat> listchats;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        fotoUserChat=(CircleImageView)findViewById(R.id.fotoperfil);
        nombreUserChat=(TextView)findViewById(R.id.nombre);
        rvChats=(RecyclerView)findViewById(R.id.rvchats);
        nuevochat=(Button)findViewById(R.id.nuevochat);
        btnCancelar=(ImageButton)findViewById(R.id.codigochat_cancelar);
        btnGuardar=(ImageButton)findViewById(R.id.codigochat_copiar);
        codigo_nombre_chat=(EditText)findViewById(R.id.codigo_nombre_chat);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            startActivity(new Intent(ChatsActivity.this,RegistroActivity.class));
            finish();
        }
        reference = database.getReference("Chats/"+currentUser.getUid());
        txtcodigochat =(TextView)findViewById(R.id.txtcodigochat);
        image_codigo_chat=(ImageView)findViewById(R.id.image_codigo_chat);
        imageButton_codigo_chat=(ImageButton)findViewById(R.id.imagebutton_image_chat);
        cardChat= findViewById(R.id.codigochat);
        adapter = new AdapterChats(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvChats.setLayoutManager(l);
        listchats = new ArrayList<>();
        //llenarlista();
        rvChats.setAdapter(adapter);

        imageButton_codigo_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"Selecciona una imagen"),PHOTO_SEND);
            }
        });

        nuevochat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adapter.addChat(new Chat("Chat de prueba","","Mensaje del chat",""));
                char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
                StringBuilder sb = new StringBuilder(7);
                Random random = new Random();
                for (int i = 0; i < 7; i++) {
                    char c = chars[random.nextInt(chars.length)];
                    sb.append(c);
                }
                codigo = sb.toString();
                txtcodigochat.setText(codigo);
                cardChat.setVisibility(View.VISIBLE);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigo_nombre_chat.setText("");
                cardChat.setVisibility(View.GONE);
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = codigo_nombre_chat.getText().toString();
                //adapter.addChat(new Chat(nombre,"","Chat nuevo!",codigo));
                FirebaseUser currentUser = mAuth.getCurrentUser();
                DatabaseReference reference1 = database.getReference("Chats/"+currentUser.getUid());
                //Chat ch1 = new Chat(name,urlimagen_chat,"Nuevo chat",codigo);
                Chat ch = new Chat();
                ch.setNombre(name);
                ch.setUrlperfil(urlimagen_chat);
                ch.setMensaje("Codigo: "+codigo);
                ch.setCodigo_del_chat(codigo);
                if(name.toString().isEmpty()){
                    Toast.makeText(ChatsActivity.this, "Ingrese un nombre para el chat", Toast.LENGTH_SHORT).show();
                } else if(urlimagen_chat!=null){
                    reference1.push().setValue(ch);
                    codigo_nombre_chat.setText("");
                    image_codigo_chat.setVisibility(View.INVISIBLE);
                    cardChat.setVisibility(View.GONE);
                    Toast.makeText(ChatsActivity.this, "Chat creado CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(ChatsActivity.this, "Agregue una foto para el chat", Toast.LENGTH_SHORT).show();
                }

            }
        });
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chat ch= snapshot.getValue(Chat.class);
                adapter.addChat(ch);
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
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });
    }
    private void setScrollbar(){
        rvChats.scrollToPosition(adapter.getItemCount()-1);
    }
    protected void onResume() {
        super.onResume();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //Recupera la informacion de usuario de la base de datos
            DatabaseReference reference = database.getReference("Usuarios/" + currentUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    urlimagen = usuario.getUserperfil();
                    NOMBRE_LOG = usuario.getNombre();
                    nombreUserChat.setText(NOMBRE_LOG);
                    Glide.with(ChatsActivity.this).load(urlimagen).into(fotoUserChat);
                    //USER_LOG = currentUser.getUid();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            startActivity(new Intent(ChatsActivity.this,RegistroActivity.class));
            Toast.makeText(ChatsActivity.this, "Necesita registrarse", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            final Uri u = data.getData();
            storageReference = storage.getReference("Pictures");//Imagenes del chat
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
                        urlimagen_chat = downloadUri.toString();
                        Glide.with(ChatsActivity.this).load(urlimagen_chat).into(image_codigo_chat);
                        image_codigo_chat.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ChatsActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void llenarlista(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        reference.child("Chats/"+currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //final List<String> chats = new ArrayList<String>();
                Chat chat = null;
                try {
                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        //String chat = areaSnapshot.child("codigo_del_chat").getValue(String.class);
                        chat = new Chat();
                        chat.setNombre(areaSnapshot.child("codigo_del_chat").getValue(String.class));
                        chat.setMensaje(areaSnapshot.child("mensaje").getValue(String.class));
                        chat.setNombre(areaSnapshot.child("nombre").getValue(String.class));
                        chat.setUrlperfil(areaSnapshot.child("urlperfil").getValue(String.class));
                        listchats.add(chat);
                        //carga los elementos en una lista
                    }
                    Toast.makeText(ChatsActivity.this, "Lista llena", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(ChatsActivity.this, "Error al cargar la lista de chats", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
