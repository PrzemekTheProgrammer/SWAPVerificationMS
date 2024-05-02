package pl.com.pszerszenowicz.SWAPVerificationApi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.util.SerializationUtils;
import pl.com.pszerszenowicz.SWAPVerificationApi.configuration.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.pszerszenowicz.model.VerificationStatus;
import pl.com.pszerszenowicz.SWAPVerificationApi.service.ScanService;

@Slf4j
@Component
public class RpcServerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ScanService scanService;

    @RabbitListener(queues = RabbitMQConfig.RECEIVING_QUEUE)
    public void process(Message msg) {
        String received = new String(msg.getBody());
        log.info("received barcode number: {}", received);
        VerificationStatus status = scanService.processScan(received);
        Message response = MessageBuilder.withBody(SerializationUtils.serialize(status)).build();
        CorrelationData correlationData = new CorrelationData(msg.getMessageProperties().getCorrelationId());
        rabbitTemplate.sendAndReceive(RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.SENDING_QUEUE,
                response,
                correlationData);
        log.info("Send verification status in return message: {}", status);
        log.info("With correlationId: {}", msg.getMessageProperties().getCorrelationId());
    }
}
