package org.sgmap.planes.route;

import org.apache.camel.builder.RouteBuilder;
import org.connectivity.common.protobuf.ConnectivityProto;
import org.sgmap.planes.bean.ConnectivityBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemConnectivityRoute extends RouteBuilder {
    @Value("${planes.connectivity.system.exchange}")
    private String connectivityExchange;

    @Value("${planes.connectivity.interval.milli}")
    private String interval;

    @Override
    public void configure() throws Exception {
        from("timer://systemConnectivity?fixedRate=true&delay=10000&period="+interval)
            .routeId("System Connectivity Route")
            .bean(ConnectivityBean.getInstance(),"getLatestConn")
                .process(e->{
                    byte[] data = e.getMessage().getBody(byte[].class);
                    System.out.println(ConnectivityProto.SystemConnectivity.parseFrom(data));
                })
            .to("rabbitmq:"+connectivityExchange+"?exchangeType=fanout&skipQueueDeclare=true&skipQueueBind=true&autoDelete=true&connectionFactory=#rabbitConnectionFactory");
    }
}
