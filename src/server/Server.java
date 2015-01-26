package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Person;
import exceptions.NotFoundException;
import facades.PersonFacadeDB;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import model.RoleSchool;


public class Server {

    static int port = 7070;
//    static String ip = "localhost";
    static String ip = "100.74.184.141";
    static String publicFolder = "src/htmlFiles/";
    static String startFile = "index.html";
    static String filesUri = "/pages";
    ObjectMapper om = new ObjectMapper();

    public void run() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(ip, port), 0);
        //REST Routes
        server.createContext("/person", new HandlerPerson());

        server.createContext("/role", new HandlerRole());
        //HTTP Server Routes
        server.createContext(filesUri, new HandlerFileServer());

        server.start();
        System.out.println("Server started, listening on port: " + port);
    }

    public static void main(String[] args) throws Exception {
        if (args.length >= 3) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
            publicFolder = args[2];
        }
        new Server().run();
    }

    class HandlerPerson implements HttpHandler {

        PersonFacadeDB facade = new PersonFacadeDB();

        @Override
        public void handle(HttpExchange he) throws IOException {
            String response = "";
            int status = 200;
            String method = he.getRequestMethod().toUpperCase();
            switch (method) {
                case "GET":
                    try {
                        String path = he.getRequestURI().getPath();
                        System.out.println(path);
                        int lastIndex = path.lastIndexOf("/");
                        if (lastIndex > 0) {  //person/id
                            String idStr = path.substring(lastIndex + 1);
                            int id = Integer.parseInt(idStr);
                            response = facade.getPerson(id);

                        } else { // person                           
                            response = facade.getPersons();
                        }
                    } catch (NumberFormatException nfe) {
                        response = "Id is not a number";
                        status = 404;
                    } catch (NotFoundException nfe) {
                        response = nfe.getMessage();
                        status = 404;
                    }
                    break;
                case "POST":
                    try {
                        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                        BufferedReader br = new BufferedReader(isr);
                        String jsonQuery = br.readLine();
                        System.out.println(jsonQuery);
                        Person p = facade.addPerson(jsonQuery);
                        response = new Gson().toJson(p);
                    } catch (IllegalArgumentException iae) {
                        status = 400;
                        response = iae.getMessage();
                    } catch (IOException e) {
                        status = 500;
                        response = "Internal Server Problem";
                    }
                    break;
                case "PUT":
                    break;
                case "DELETE":
                    try {
                        String path = he.getRequestURI().getPath();
                        System.out.println(path);
                        int lastIndex = path.lastIndexOf("/");
                        if (lastIndex > 0) {  //person/id
                            int id = Integer.parseInt(path.substring(lastIndex + 1));
                            Person pDeleted = facade.delete(id);

                            //response = new Gson().toJson(pDeleted);
                            response = om.writeValueAsString(pDeleted);
                        } else {
                            status = 400;
                            response = "No id supplied with request";
                        }
                    } catch (NotFoundException nfe) {
                        status = 404;
                        response = nfe.getMessage();
                    } catch (NumberFormatException nfe) {
                        response = "Id is not a number";
                        status = 404;
                    }
                    break;
            }
            he.getResponseHeaders().add("Content-Type", "application/json");
            he.sendResponseHeaders(status, 0);
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    class HandlerRole implements HttpHandler {

        PersonFacadeDB facade = new PersonFacadeDB();

        @Override
        public void handle(HttpExchange he) throws IOException {
            String response = "";
            int status = 200;
            String method = he.getRequestMethod().toUpperCase();
            switch (method) {
                case "GET":

                    break;
                case "POST":
                    try {
                        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                        BufferedReader br = new BufferedReader(isr);
                        String jsonQuery = br.readLine();
                        System.out.println("request: " + jsonQuery);

                        String path = he.getRequestURI().getPath();
                        System.out.println(path);
                        int lastIndex = path.lastIndexOf("/");
                        if (lastIndex > 0) {  //role/id
                            int id = Integer.parseInt(path.substring(lastIndex + 1));
                            RoleSchool r = facade.addRole(jsonQuery, id);

                            //response = new Gson().toJson(r);
                            response = om.writeValueAsString(r);

                        } else {
                            status = 400;
                            response = "No id supplied with request";
                        }

                    } catch (NotFoundException nfe) {
                        status = 404;
                        response = nfe.getMessage();
                    } catch (NumberFormatException nfe) {
                        response = "Id is not a number";
                        status = 404;
                    } catch (IllegalArgumentException iae) {
                        status = 400;
                        response = iae.getMessage();
                    } catch (IOException e) {
                        status = 500;
                        response = "Internal Server Problem";
                    }
                    break;
                case "PUT":
                    break;
                case "DELETE":
                    break;
            }

            he.getResponseHeaders().add("Content-Type", "application/json");
            he.sendResponseHeaders(status, 0);
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }

        }
    }

    class HandlerFileServer implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            int responseCode = 500;
            //Set initial error values if an un expected problem occurs
            String errorMsg = null;
            byte[] bytesToSend = "<h1>Internal Error </h1><p>We are sorry. The server encountered an unexpected problem</p>".getBytes();
            String mime = null;

            String requestedFile = he.getRequestURI().toString();
            String f = requestedFile.substring(requestedFile.lastIndexOf("/") + 1);
            try {
                String extension = f.substring(f.lastIndexOf("."));
                mime = getMime(extension);
                File file = new File(publicFolder + f);
                //System.out.println(publicFolder + f);
                bytesToSend = new byte[(int) file.length()];
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytesToSend, 0, bytesToSend.length);
                responseCode = 200;
            } catch (Exception e) {
                responseCode = 404;
                errorMsg = "<h1>404 Not Found</h1>No context found for request";
            }
            if (responseCode == 200) {
                Headers h = he.getResponseHeaders();
                h.set("Content-Type", mime);
            } else {
                bytesToSend = errorMsg.getBytes();
            }
            he.sendResponseHeaders(responseCode, bytesToSend.length);
            try (OutputStream os = he.getResponseBody()) {
                os.write(bytesToSend, 0, bytesToSend.length);
            }
        }

        private String getMime(String extension) {
            String mime = "";
            switch (extension) {
                case ".pdf":
                    mime = "application/pdf";
                    break;
                case ".png":
                    mime = "image/png";
                case ".css":
                    mime = "text/css";
                    break;
                case ".js":
                    mime = "text/javascript";
                    break;
                case ".html":
                    mime = "text/html";
                    break;
                case ".jar":
                    mime = "application/java-archive";
                    break;
            }
            return mime;
        }
    }
}
