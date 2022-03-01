package test.crbm.transaction;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author carlos
 */
@Configuration
public class TransactionExchangeConfig {

    @Bean
    public Exchange transactionExchange() {
        return new DirectExchange("transaction", true, false);
    }

}
