package test.crbm.transaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import test.crbm.transaction.message.InputMessage;
import test.crbm.transaction.message.InputMessageRepository;

/**
 *
 * @author carlos
 */
@Component
public class InputRoute {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(InputRoute.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InputMessageRepository inputMessagerepository;

    @Bean
    public Binding transactionInBinding(
            @Qualifier("transactionInQueue") Queue queue,
            @Qualifier("transactionExchange") Exchange exchange) throws ISOException {

        final HashMap<String, Object> bindArgs = new HashMap<>();

        return new Binding(queue.getName(),
                Binding.DestinationType.QUEUE,
                exchange.getName(),
                "transaction.in",
                bindArgs);
    }

    @RabbitListener(id = "transaction-in",
            queues = "#{transactionInQueue.name}")
    public void transform(Channel channel, Message message) throws IOException, ISOException {

        LOGGER.debug("{}", message);

        JsonNode jsonNode = objectMapper.readTree(message.getBody());
        LOGGER.debug("{}", jsonNode);

        Map<String, String> jsonMsg
                = objectMapper.readValue(jsonNode.traverse(), Map.class);
        LOGGER.debug("{}", jsonMsg);

        GenericPackager packager = new GenericPackager(
                getClass().getResource("/iso87ascii.xml").openStream());
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);

        jsonMsg.entrySet().stream().forEach(entry -> {
            isoMsg.set(Integer.parseInt(entry.getKey()),
                    entry.getValue());
        });

        byte[] binary = isoMsg.pack();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < binary.length; i++) {
            sb.append(Integer.toString(binary[i]));
        }

        String isoStrMsg = new String(binary, Charset.defaultCharset());
        LOGGER.debug("{}", isoStrMsg);

        InputMessage inputMessage = new InputMessage();
        inputMessage.setDateTime(OffsetDateTime.now());
        inputMessage.setJson(jsonNode.toString());
        inputMessage.setIso(isoStrMsg);

        inputMessagerepository.save(inputMessage);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                false);
    }

}
