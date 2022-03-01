package test.crbm.store;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 *
 * @author carlos
 */
@Component
public class StoreRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        
        from("file:///home/carlos/projects/store/src/test/resources?autoCreate=true&delay=10000&delete=false")
                .to("rabbitmq:transaction?addresses=localhost:5672"
                        + "&username=guest"
                        + "&password=guest"
                        + "&queue=transaction-in"                        
                        + "&autoDelete=false");

    }

}
