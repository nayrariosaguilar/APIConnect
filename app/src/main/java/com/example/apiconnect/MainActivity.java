package com.example.apiconnect;

import android.content.Context;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
        //inicioMovies();
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
                // Obtener la lista de personajes desde la API
                listCharacter = restClient.listCharacters();

            } catch (IOException e) {
                e.printStackTrace();
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
    private void readHttpWriteFile(String urlString, String nameFileOutput) {
        try (FileOutputStream fos = openFileOutput(nameFileOutput, Context.MODE_PRIVATE);
             InputStream inputStream = new URL(urlString).openStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            System.out.println("Image downloaded and saved as: " + nameFileOutput);

        } catch (IOException e) {
            System.err.println("Error downloading image: " + e.getMessage());
        }
    }

}