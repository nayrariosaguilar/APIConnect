package com.example.apiconnect.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.*;

/**
 * Main program to test WebService
 *
 * @author Albert Mallada
 */
public class MainTestApp {

    private static BufferedReader teclat = new BufferedReader(new InputStreamReader(System.in));
    // Private keys from Albert Mallada (Institut Provençana)
    private static String marvelPublicKey = "f0bd70e61a5585d39ee70be55d8181aa";
    private static String marvelPrivateKey = "71035e9c722cde4b7b1b6a7e156fe3f34a8e09c8";


    /**
     * Main program
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        CharacterMarvelRestClient restClient = new CharacterMarvelRestClient(marvelPublicKey, marvelPrivateKey);
        CharacterComic[] characterComics;
        int opcioMenu;
        do {
            opcioMenu = printMenu();
            switch (opcioMenu) {
                case 1:
                    characterComics = restClient.listCharacters();
                    for (int i = 0; i < characterComics.length; i++) {
                        System.out.println("id: " + characterComics[i].id + " Name: " + characterComics[i].name);
                        System.out.println("URL image: "
                                + characterComics[i].thumbnail.path + "." + characterComics[i].thumbnail.extension);
                    }
                    break;
                case 2:
                    System.out.println("Indica l'id que vols buscar: ");
                    int id = Integer.parseInt(teclat.readLine());
                    CharacterComic c = restClient.FindById(id);
                    System.out.println("id: " + c.id + " name: " + c.name);
                    break;
                case 3:
                    System.out.println("Indica el personatge que vols buscar: ");
                    String cerca = teclat.readLine();
                    characterComics = restClient.FindByName(cerca);
                    for (int i = 0; i < characterComics.length; i++)
                        System.out.println("id: " + characterComics[i].id + " Name: " + characterComics[i].name);
                    break;
                case 4:
                    restClient.ParseToObject();
                    break;
                default:
                    break;
            }

        } while (opcioMenu != 0);
    }

    /**
     * Menu to test Marvel API
     *
     * @return -1 if there are and error getting int value in options menú
     * @throws IOException
     */
    private static int printMenu() throws IOException {
        int result;
        do {
            System.out.println("========= Menu =========");
            System.out.println("1. List characters");
            System.out.println("2. Find character by id");
            System.out.println("3. Find character by name");
            System.out.println("0. Exit");
            System.out.println("========================");
            System.out.print("Choose an option: ");
            try {
                result = Integer.parseInt(teclat.readLine());
            } catch (NumberFormatException e) {
                result = -1;
            }
        } while (result == -1);

        return result;
    }

}