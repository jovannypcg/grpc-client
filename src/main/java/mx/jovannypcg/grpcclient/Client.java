package mx.jovannypcg.grpcclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import mx.jovannypcg.grpcclient.messages.HelloReply;
import mx.jovannypcg.grpcclient.messages.HelloRequest;
import mx.jovannypcg.grpcclient.services.GreeterGrpc;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getSimpleName());

    private ManagedChannel channel;
    private GreeterGrpc.GreeterBlockingStub blockingStub;

    Client(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build());
    }

    Client(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = GreeterGrpc.newBlockingStub(this.channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void greet(String name) {
        LOGGER.info("Will try to greet " + name + " ...");

        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;

        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException sre) {
            LOGGER.log(Level.WARNING, "RPC failed: {0}", sre.getStatus());
            return;
        }

        LOGGER.info("Greeting: " + response.getMessage());
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client("localhost", 5000);

        try {
            client.greet("Jovanny Pablo");
        } finally {
            client.shutdown();
        }
    }
}
