package com.lahhass.miaosha.rabbitmq;

import com.lahhass.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

//    public void send(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send message:"+msg);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
//    }
//
//    public void sendTopic(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send message:"+msg);
//        //queue1,queue2能收到message1
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
//        //queue2能收到message1
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
//    }
//
//    public void sendFanout(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send fanout message:"+msg);
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg+"1");
//    }
//
//    public void sendHeader(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send header message:"+msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("header1","value1");
//        properties.setHeader("header2","value2");
//        Message obj = new Message(msg.getBytes(),properties);
//        //发送内容为消息的字节数组和拓扑信息
//        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"", obj);
//    }

    //使用Direct模式的交换机
    public void sendMiaoshaMessage(MiaoshaMessage mm) {
        String msg = RedisService.beanToString(mm);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }
}
