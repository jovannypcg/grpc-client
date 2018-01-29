package mx.jovannypcg.grpcclient;

import mx.jovannypcg.grpcclient.messages.Repository;

import java.util.HashMap;
import java.util.Map;

public class Application {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client(HOST, PORT);

        Map<String, Integer> languageContributions = new HashMap<>();
        languageContributions.put("Java", 123);
        languageContributions.put("Ruby", 543);
        languageContributions.put("Elixir", 6234);

        Repository request = Repository.newBuilder()
                .setName("protocol-server")
                .setDescription("An example project that demonstrates gRPC in action")
                .setCodeFrequency(5.4f)
                .putAllLanguageContributions(languageContributions)
                .setStarts(2345341L)
                .build();

        try {
            client.enroll(request);
        } finally {
            client.shutdown();
        }
    }
}
