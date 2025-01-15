package com.example.apiconnect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apiconnect.RestClient.CharacterComic;
import com.example.apiconnect.RestClient.CharacterMarvelRestClient;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ExecutorService executor;
    Handler handler;
    CustomAdapter customAdapter;
    CharacterComic[] listCharacter;

    private LinearLayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
        inicioMovies();
        inicioElementos();
        pedirCharactersApiMarvel();
    }

    private void pedirCharactersApiMarvel() {
        //genero mi executor para que lo ejecute de forma asincrona
    executor.execute(new Runnable() {
        @Override
        public void run() {
            //quiero crear un caracter Marvel REST CLIENT
             String marvelPublicKey = "f0bd70e61a5585d39ee70be55d8181aa";
             String marvelPrivateKey = "71035e9c722cde4b7b1b6a7e156fe3f34a8e09c8";
             //al constructor le paso publi/private key
            CharacterMarvelRestClient restClient =  new CharacterMarvelRestClient(marvelPublicKey,marvelPrivateKey);
            try {
                listCharacter = restClient.listCharacters();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    inicioElementos();
                }
            });
        }
    });
    }

    private void inicioElementos() {
        RecyclerView recycler = findViewById(R.id.recyclerView);
        lManager = new LinearLayoutManager(this );
        recycler.setLayoutManager(lManager);
        customAdapter = new CustomAdapter(listCharacter);
        recycler.setAdapter(customAdapter);
    }

    private void inicioMovies() {
        listCharacter = new CharacterComic[3];
       listCharacter[0] = new CharacterComic("naysita");
        listCharacter[1] = new CharacterComic("naysita2");
        listCharacter[2] = new CharacterComic("naysita3");
    }
}