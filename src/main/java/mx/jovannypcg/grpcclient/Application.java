package mx.jovannypcg.grpcclient;

import com.google.protobuf.InvalidProtocolBufferException;
import mx.jovannypcg.grpcclient.amqp.Receiver;
import mx.jovannypcg.grpcclient.messages.EnrollmentResponse;
import mx.jovannypcg.grpcclient.messages.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    @Autowired
    Receiver receiver;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException, InvalidProtocolBufferException {
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
            EnrollmentResponse response = client.enroll(request);

            if (response.getAck()) {
                LOGGER.info(" =======> Repository from AMQP... ");

                Repository repository = receiver.receive();
                LOGGER.info(repository.toString());
            }

        } finally {
            client.shutdown();
        }
    }
}
