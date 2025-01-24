package com.example.apiconnect;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apiconnect.RestClient.CharacterComic;
import com.example.apiconnect.RestClient.CharacterMarvelRestClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FullCharacterDescription extends AppCompatActivity {
    ExecutorService executor;
    Handler handler;
    TextView textViewName;
    ImageView imageView;
    Bundle id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_character_description);

        id = getIntent().getExtras();
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
        pedirCharactersApiMarvel();
    }
    private void pedirCharactersApiMarvel() {
        //genero mi executor para que lo ejecute de forma asincrona
        executor.execute(new Runnable() {
            @Override
            public void run() {
                inicioElementos();
                //quiero crear un caracter Marvel REST CLIENT
                String marvelPublicKey = "f0bd70e61a5585d39ee70be55d8181aa";
                String marvelPrivateKey = "71035e9c722cde4b7b1b6a7e156fe3f34a8e09c8";
                //al constructor le paso publi/private key
                CharacterMarvelRestClient restClient =  new CharacterMarvelRestClient(marvelPublicKey,marvelPrivateKey);
                String outputFileName = null;
                try {

                    // Obtener la lista de personajes desde la API
                    int idMarve = id.getInt("id");
                    CharacterComic c = restClient.FindById(idMarve);
                    if (c == null || c.getId() == 0)
                        throw new IOException("No se encontraron personajes con el ID");
                    String description = c.getDescription();
                    textViewName.setText(description);
                    String Path = c.getThumbnail().getPath();
                    String Extension = c.getThumbnail().getExtension();
                    String url= Path+"."+Extension;
                    outputFileName =  c.getId() + ".jpg";
                    readHttpWriteFile(url, outputFileName);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                String finalOutputFileName = outputFileName;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        carregaImatge(finalOutputFileName);
                    }
                });
            }
        });
    }
    private void inicioElementos() {
        textViewName = findViewById(R.id.description);
        imageView = (ImageView) findViewById(R.id.foto);

    }private void readHttpWriteFile(String urlString, String nameFileOutput) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Error en la conexión: " + connection.getResponseCode());
            }
            InputStream inputStream = connection.getInputStream();
            FileOutputStream fos = openFileOutput(nameFileOutput, Context.MODE_PRIVATE);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
            inputStream.close();
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void carregaImatge(String fileName) {
        File directoryInternalStorage = getFilesDir();
        File file = new File(directoryInternalStorage, fileName);
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            imageView.setImageURI(uri);
        } else {
            System.out.println("El archivo no existe en la ruta: " + file.getAbsolutePath());
        }
    }

}