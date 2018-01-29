package mx.jovannypcg.grpcclient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import mx.jovannypcg.grpcclient.messages.EnrollmentResponse;
import mx.jovannypcg.grpcclient.messages.Repository;
import mx.jovannypcg.grpcclient.services.RepositoryEnrollerServiceGrpc;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getSimpleName());

    private final ManagedChannel channel;
    private final RepositoryEnrollerServiceGrpc.RepositoryEnrollerServiceBlockingStub blockingStub;

    Client(String host, int port) {
        this(ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext(true)
                .build());
    }

    Client(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = RepositoryEnrollerServiceGrpc.newBlockingStub(this.channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public EnrollmentResponse enroll(Repository repository) {
        LOGGER.info("Attemting to enroll [" + repository.getName() + "]...");

        EnrollmentResponse response;

        try {
            response = blockingStub.enroll(repository);
        } catch (StatusRuntimeException sre) {
            LOGGER.log(Level.WARNING, "RPC failed: {0}", sre.getStatus());
            return null;
        }

        LOGGER.info("Enrollment " + (response.getAck() ? "successful!" : "failed") + "!");
        return response;
    }
}
