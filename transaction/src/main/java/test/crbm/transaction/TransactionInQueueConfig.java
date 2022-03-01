package test.crbm.transaction;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author carlos
 */
@Configuration
public class TransactionInQueueConfig {

    @Bean
    public Queue transactionInQueue() {
        return new Queue("transaction-in", true, false, false);
    }
    
}
