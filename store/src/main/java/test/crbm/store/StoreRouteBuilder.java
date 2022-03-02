package test.crbm.store;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author carlos
 */
@Component
public class StoreRouteBuilder extends RouteBuilder {

    @Value("${case.input.from}")
    private String from;

    @Value("${case.rabbitmq.addresses}")
    private String addresses;

    @Value("${case.rabbitmq.username}")
    private String username;

    @Value("${case.rabbitmq.password}")
    private String password;

    @Value("${case.rabbitmq.ssl-protocol:}")
    private String sslProtocol;

    @Override
    public void configure() throws Exception {

        StringBuilder to = new StringBuilder();
        to.append("rabbitmq:transaction")
                .append("?addresses=").append(addresses)
                .append("&username=").append(username)
                .append("&password=").append(password);
        if (sslProtocol.trim().length() > 0) {
            to.append("&sslProtocol=").append(sslProtocol);
        }
        to.append("&queue=transaction-in")
                .append("&autoDelete=false");

        from(from).to(to.toString());
    }

}
