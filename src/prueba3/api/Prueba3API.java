/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package prueba3.api;

/**
 *
 * @author die_a
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
//import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;




public class Prueba3API {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            int opcion;
            
            do {
                System.out.println("Seleccione el tipo de solicitud que desea realizar:");
                System.out.println("1. Obtener información de un producto (GET)");
                System.out.println("2. Agregar un nuevo producto (POST)");
                System.out.println("3. Actualizar información de un producto existente (PUT)");
                System.out.println("4. Eliminar un producto (DELETE)");
                System.out.println("5. Salir");
                System.out.print("Ingrese el número de la opción deseada: ");
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea después del entero
                
                switch (opcion) {
                    case 1:
                        System.out.println("Ingrese el nombre del producto del que desea obtener más información:");
                        String nombreProductoGET = scanner.nextLine();
                        String informacionProductoGET = obtenerInformacionProducto(nombreProductoGET);
                        if (informacionProductoGET != null) {
                            System.out.println("Información del producto:");
                            System.out.println(informacionProductoGET);
                        } else {
                            System.out.println("El producto no fue encontrado.");
                        }
                        break;
                    case 2:
                        System.out.println("Ingrese los datos del nuevo producto:");
                        System.out.print("Nombre: ");
                        String nombreProductoPOST = scanner.nextLine();
                        System.out.print("Precio: ");
                        int precioProductoPOST = scanner.nextInt();
                        scanner.nextLine(); // Consumir la nueva línea después del entero
                        System.out.print("Descripción: ");
                        String descripcionProductoPOST = scanner.nextLine();
                        String respuestaPOST = agregarNuevoProducto(nombreProductoPOST, precioProductoPOST, descripcionProductoPOST);
                        System.out.println(respuestaPOST);
                        break;
                    case 3:
                        System.out.println("Ingrese el ID del producto que desea actualizar:");
                        int idProductoPUT = scanner.nextInt();
                        scanner.nextLine(); // Consumir la nueva línea después del entero
                        System.out.println("Ingrese los nuevos datos del producto:");
                        System.out.print("Nombre: ");
                        String nombreProductoPUT = scanner.nextLine();
                        System.out.print("Precio: ");
                        int precioProductoPUT = scanner.nextInt();
                        scanner.nextLine(); // Consumir la nueva línea después del entero
                        System.out.print("Descripción: ");
                        String descripcionProductoPUT = scanner.nextLine();
                        String respuestaPUT = actualizarProducto(idProductoPUT, nombreProductoPUT, precioProductoPUT, descripcionProductoPUT);
                        System.out.println(respuestaPUT);
                        break;
                    case 4:
                        System.out.println("Ingrese el ID del producto que desea eliminar:");
                        int idProductoDELETE = scanner.nextInt();
                        scanner.nextLine(); // Consumir la nueva línea después del entero
                        String respuestaDELETE = eliminarProducto(idProductoDELETE);
                        System.out.println(respuestaDELETE);
                        break;
                    case 5:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
                }
            } while (opcion != 5);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String obtenerInformacionProducto(String nombreProducto) throws Exception {
        String urlString = "http://localhost:5004/producto?nombre=" + nombreProducto.replace(" ", "%20");
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }
    
    public static String agregarNuevoProducto(String nombreProducto, int precioProducto, String descripcionProducto) throws Exception {
        String urlString = "http://localhost:5004/producto";
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        
        String jsonInputString = String.format("{\"nombre\": \"%s\", \"precio\": %d, \"descripcion\": \"%s\"}", nombreProducto, precioProducto, descripcionProducto);
        
        try (var os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    public static String actualizarProducto(int id, String nombre, int precio, String descripcion) throws Exception {
        String urlString = "http://localhost:5004/producto/" + id;
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        
        String jsonInputString = String.format("{\"nombre\": \"%s\", \"precio\": %d, \"descripcion\": \"%s\"}", nombre, precio, descripcion);
        
        try (var os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    public static String eliminarProducto(int id) throws Exception {
        String urlString = "http://localhost:5004/producto/" + id;
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
    

}


