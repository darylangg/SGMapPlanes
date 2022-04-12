package org.sgmap.planes.route;

import org.apache.camel.builder.RouteBuilder;
import org.sgmap.planes.bean.ConnectivityBean;
import org.sgmap.planes.bean.DataBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataPollRoute extends RouteBuilder {
    @Value("${planes.poll.rate.seconds}")
    private String pollRateSeconds;

    @Value("${planes.api.url}")
    private String url;

    @Value("${planes.exchange}")
    private String exchange;

    @Override
    public void configure() throws Exception {
        from("timer://dataFetch?fixedRate=true&delay=0&period="+pollRateSeconds+"000")
                .routeId("Data Polling")
                .doTry()
                    .to("https://" + url )
                    .unmarshal().json()
                    .bean(DataBean.getInstance(),"unpackData")
                    .to("rabbitmq:"+exchange+"_data_fanout?exchangeType=fanout&skipQueueDeclare=true&skipQueueBind=true&autoDelete=true&connectionFactory=#rabbitConnectionFactory")
                    .bean(ConnectivityBean.getInstance(), "ping");
    }
}
