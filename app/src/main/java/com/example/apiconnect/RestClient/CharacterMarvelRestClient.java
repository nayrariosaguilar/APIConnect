package com.example.apiconnect.RestClient;
import com.cedarsoftware.util.io.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * get Data from Marvel WebService
 * @author Albert Mallada
 */
public class CharacterMarvelRestClient {

    //URL base de la API de Marvel. Totes les peticions parteixen d'aquí.
    private String baseUrl = "http://gateway.marvel.com/v1/public";
    //Clau pública (obtinguda mitjançant registre a la web de Marvel)
    private String publicKey;
    //Clau privada (obtinguda mitjançant registre a la web de Marvel)
    private String privateKey;

    //URL de la API que permet obtenir la llista de personatges.
    //Es forma partint de la base donat que la URL és http://gateway.marvel.com/v1/public/characters
    private String listCharactersEndpoint = baseUrl + "/characters";

    public CharacterMarvelRestClient(String publicKey, String privateKey){
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }


    public CharacterComic[] ListCharacters() throws IOException {
        //Generem l'objecte URL que fa servir HttpURLConnection
        URL url = new URL(listCharactersEndpoint + "?" + getRequestParameters());

        //L'objecte HttpUrlConnection ens permet manipular una connexió HTTP.
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        //El mètode (o verb) que utilitzarem per fer la petició és GET.
        //Recordeu que en tenim d'altres: GET, POST, PUT, DELETE, OPTIONS, HEAD
        con.setRequestMethod("GET");

        //Connectem amb el servidor
        con.connect();

        System.out.println("Sending 'GET' request to URL : " + url.toString());
        System.out.println("Response status: " + con.getResponseCode() + " " + con.getResponseMessage());
        System.out.println("Response Body : ");
        String response = getResponseBody(con);
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        Data d = new Gson().fromJson(jsonObject.get("data"), Data.class);
        return d.results;
    }

    public CharacterComic FindById(int id) throws IOException {
        URL url = new URL(listCharactersEndpoint +
                "/" + id + "?" + getRequestParameters());
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        System.out.println("Sending 'GET' request to URL : " + url.toString());
        System.out.println("Response status: " + con.getResponseCode() + " " + con.getResponseMessage());
        //System.out.println("Response Body : ");
        //System.out.println(JsonWriter.formatJson(getResponseBody(con)));
        String response = getResponseBody(con);

        if(con.getResponseCode() == 200) {
            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            Data d = new Gson().fromJson(jsonObject.get("data"), Data.class);
            return d.results[0];
        }else{
            return null;
        }

    }


    public CharacterComic[] FindByName(String cerca) throws IOException{
        //Generem l'objecte URL que fa servir HttpURLConnection
        URL url = new URL(listCharactersEndpoint +
                "?" + getRequestParameters() +
                "&nameStartsWith=" + cerca);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        System.out.println("Sending 'GET' request to URL : " + url.toString());
        System.out.println("Response status: " + con.getResponseCode() + " " + con.getResponseMessage());
        System.out.println("Response Body : ");
        String response = getResponseBody(con);
        System.out.println(JsonWriter.formatJson(response));
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        Data d = new Gson().fromJson(jsonObject.get("data"), Data.class);
        return d.results;
        //return "";

    }

    public void ParseToObject() throws IOException {
        //Generem l'objecte URL que fa servir HttpURLConnection
        URL url = new URL(listCharactersEndpoint + "?" + getRequestParameters());

        //L'objecte HttpUrlConnection ens permet manipular una connexió HTTP.
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        //El mètode (o verb) que utilitzarem per fer la petició és GET.
        //Recordeu que en tenim d'altres: GET, POST, PUT, DELETE, OPTIONS, HEAD
        con.setRequestMethod("GET");

        //Connectem amb el servidor
        con.connect();

        System.out.println("Sending 'GET' request to URL : " + url.toString());
        System.out.println("Response status: " + con.getResponseCode() + " " + con.getResponseMessage());
        System.out.println("Response Body : ");
        String response = getResponseBody(con);
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        Data d = new Gson().fromJson(jsonObject.get("data"), Data.class);
        CharacterComic arr[] = d.results;

        for(int i=0; i<arr.length; i++)
            System.out.println("id: " + arr[i].id +  " Name: " + arr[i].name);


    }


    /*
     * Funció auxiliar que retorna els paràmetres auxiliars de la petició HTTP
     *
     */
    private String getRequestParameters(){
        StringBuilder sb = new StringBuilder();
        sb.append("apikey=" + this.publicKey + "&");
        sb.append("ts=" + "1" + "&");
        sb.append("hash=" + MD5("1" + this.privateKey + this.publicKey));
        return sb.toString();
    }


    /*
     * Funció axiliar per calcular el MD5 del missatge donat
     *
     */
    public String MD5(String mes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(mes.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
    /*
     * Funció auxiliar per printar el resultat d'una petició
     *
     */
    private String getResponseBody(HttpURLConnection con) throws IOException {
        BufferedReader br;

        if (con.getResponseCode() >= 400) {
            //Si el codi de resposta és superior a 400 s'ha produit un error i cal llegir
            //el missatge d'ErrorStream().
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        } else {
            //Si el codi és inferior a 400 llavors obtenim una resposta correcte del servidor.
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }

}