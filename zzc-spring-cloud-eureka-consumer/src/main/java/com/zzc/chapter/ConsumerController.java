package com.zzc.chapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/consumer")
public class ConsumerController {

    static Logger logger=LoggerFactory.getLogger(ConsumerController.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/logic")
    public String home(){
        return "this is home page";
    }

    @RequestMapping(value = "/index")
    public void index(){
        discoveryClient.getInstances("zzc-spring-cloud-eureka-consumer")
                .stream()
                .forEach(
                        instance -> {
                            logger.info("服务地址：{}，服务端口号：{}，服务实例编号：{}，服务地址：{}", instance.getHost(), instance.getPort(), instance.getServiceId(), instance.getUri());
                            String response = restTemplate.getForEntity("http://" + instance.getServiceId() + "/consumer/logic", String.class).getBody();
                            logger.info("响应内容：{}", response);
                        }
                );
    }
}
