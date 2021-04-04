//Michał Malinowski i Krzysztof Anderson, zespół 4

package mazemapping;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.file.Paths;

public class HttpConnector {

    private final HttpClient client = HttpClient.newHttpClient();

    public String get(String map, String command) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://tesla.iem.pw.edu.pl:4444/302f7006/" + map + "/" + command))
                .build();
        HttpResponse<String> response = connectAndRepeatIfNecessary(request);
        if (response != null) {
            return response.body();
        }

        System.out.println("Serwer nie odpowiada. Zamykanie programu.");
        System.exit(0); // nie udało się przekazać odpowiedzi, więc zamykamy program
        return null;
    }

    public int post(String map, String command) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://tesla.iem.pw.edu.pl:4444/302f7006/" + map + "/" + command))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        HttpResponse<String> response = connectAndRepeatIfNecessary(request);
        if (response != null) {
            return 0;
        }
        System.out.println("Serwer nie odpowiada. Zamykanie programu.");
        System.exit(0); // nie udało się przekazać odpowiedzi, więc zamykamy program
        return -1;
    }

    private int checkStatusCode(HttpResponse<String> response) {
        if (response.statusCode() / 100 == 2) {
            return 0;
        } else if (response.statusCode() / 100 == 1 || response.statusCode() / 100 == 3
                || response.statusCode() / 100 == 5) // trzeba spróbować jeszcze raz połączyć
        {
            return 1;
        } else if (response.statusCode() == 400 || response.statusCode() == 404) // błąd w programie, należy zakończyć
        {
            System.out.println("Błąd w komendzie, powód:");
            System.out.println(response.body());
            return -1;
        } else if (response.statusCode() == 403) {
            System.out.println("Błąd logiczny zapytania, powód:");
            System.out.println(response.body());
            return -1;
        } else {
            System.out.println("Inny kod błędu: " + response.statusCode());
            System.out.println(response.body());
            return -1;
        }
    }

    private HttpResponse<String> connectAndRepeatIfNecessary(HttpRequest request) {
        for (int i = 0; i < 3; i++) {
            HttpResponse<String> response = null;
            try {
                response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());
            } catch (InterruptedException | IOException e) {
                System.out.println("Wysyłanie zapytania zostało przerwane.");
                e.printStackTrace();
            }
            int checkStatus = checkStatusCode(response);
            if (checkStatus == 0) {
                return response;
            }
            if (checkStatus == -1) {
                return null;
            }
        }
        return null;
    }

    public int sendMap(String map, String path) {
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://tesla.iem.pw.edu.pl:4444/302f7006/" + map + "/upload"))
                    .POST(HttpRequest.BodyPublishers.ofFile(Paths.get(path)))
                    .build();
        } catch (FileNotFoundException ex) {
            System.out.println("Nie znaleziono pliku do wysłania.");
            ex.printStackTrace();
        }
        HttpResponse<String> response = connectAndRepeatIfNecessary(request);
        if (response != null) {
            System.out.println(response.body());
            return 0;
        }
        System.out.println("Serwer nie odpowiada, zamykanie programu.");
        System.exit(0); // nie udało się przekazać odpowiedzi, więc zamykamy program
        return -1;
    }
}
