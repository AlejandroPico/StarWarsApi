package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Swapi {
	
	public static enum Request {
		
		GET		("GET"),
		POST	("POST"),
		;
		
		private String value;
		
		private Request(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
	
    @SuppressWarnings("deprecation")
	public static void main(String[] args) {
    	
    	Scanner sc = new Scanner(System.in);
        int election = 0;
        String apiUrl = "https://swapi.dev/api";

        // Bucle para seguir mostrando el menú hasta que el usuario decida salir
        while (true) {
            // Mostrar el menú
            boolean validInput = false;

            while (!validInput) {
                try {
                    System.out.println("Elige qué quieres consultar:");
                    System.out.println("1- Planetas");
                    System.out.println("2- Personajes");
                    System.out.println("3- Películas");
                    System.out.println("4- Naves");
                    System.out.println("5- Vehículos");
                    System.out.println("6- Especies");
                    System.out.println("7- Salir");
                    System.out.print("Introduce tu opción: ");

                    election = sc.nextInt();

                    // Verificar si la opción es válida (1-7)
                    if (election >= 1 && election <= 7) {
                        validInput = true;
                    } else {
                        System.out.println("Por favor, ingrese una opción válida (1-7).");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Por favor, ingrese un número entero.");
                    sc.next(); // Limpiar el buffer para evitar bucle infinito
                }
            }

            // Si elige 7, terminamos el programa
            if (election == 7) {
                System.out.println("Saliendo del programa. ¡Hasta pronto!");
                break;
            }

            // Selección de la API según la elección
            switch (election) {
                case 1:
                    apiUrl = apiUrl + "/planets/";
                    break;
                case 2:
                    apiUrl = apiUrl + "/people/";
                    break;
                case 3:
                    apiUrl = apiUrl + "/films/";
                    break;
                case 4:
                    apiUrl = apiUrl + "/starships/";
                    break;
                case 5:
                    apiUrl = apiUrl + "/vehicles/";
                    break;
                case 6:
                    apiUrl = apiUrl + "/species/";
                    break;
            }

            // Hacer la consulta a la API
            try {
                // Crear un objeto URL
                URL url = new URL(apiUrl);

                // Abrir una conexión a la URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Establecer el método de solicitud GET
                connection.setRequestMethod(Request.GET.getValue());

                // Obtener el código de respuesta
                int responseCode = connection.getResponseCode();
                System.out.println("Código de respuesta: " + responseCode);

                // Leer la respuesta si el código es 200 (OK)
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    // Leer cada línea de la respuesta y añadirla al StringBuilder
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // Cerrar el BufferedReader
                    in.close();

                    // Convertir la respuesta a un objeto JSON usando Gson
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);

                    // Obtener el array de resultados según la opción seleccionada
                    JsonArray results = jsonResponse.getAsJsonArray("results");

                    // Mostrar el tipo de resultado de acuerdo a la selección
                    switch (election) {
                        case 1:
                            System.out.println("Listado de planetas:");
                            break;
                        case 2:
                            System.out.println("Listado de personajes:");
                            break;
                        case 3:
                            System.out.println("Listado de películas:");
                            break;
                        case 4:
                            System.out.println("Listado de naves:");
                            break;
                        case 5:
                            System.out.println("Listado de vehículos:");
                            break;
                        case 6:
                            System.out.println("Listado de especies:");
                            break;
                    }

                    // Recorrer e imprimir los nombres del recurso correspondiente
                    for (int i = 0; i < results.size(); i++) {
                        JsonObject item = results.get(i).getAsJsonObject();
                        String itemName = item.get("name") != null ? item.get("name").getAsString() : item.get("title").getAsString(); // Algunos tienen "title" en lugar de "name"
                        System.out.println("- " + itemName);
                    }
                } else {
                    System.out.println("Error en la conexión a la API.");
                }

                // Cerrar la conexión
                connection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Añadir un separador para mayor claridad
            System.out.println("---------------------------------------");
        }
        
        sc.close(); // Cerrar el scanner al finalizar el programa
    }
}