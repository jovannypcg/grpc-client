package mx.jovannypcg.grpcclient.amqp;

import com.google.protobuf.InvalidProtocolBufferException;
import mx.jovannypcg.grpcclient.messages.Repository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private RabbitTemplate rabbitTemplate;

    public Receiver(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public Repository receive() throws InvalidProtocolBufferException {
        byte[] repositoryBytes = (byte[]) rabbitTemplate.receiveAndConvert();

        Repository repository = Repository.parseFrom(repositoryBytes);

        return repository;
    }
}
