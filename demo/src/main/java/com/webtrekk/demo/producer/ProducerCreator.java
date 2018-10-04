package com.webtrekk.demo.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;


import com.webtrekk.demo.constants.IKafkaConstants;
import com.webtrekk.demo.data.EMailData;


public class ProducerCreator {
	
	
	
	private static Producer<String, EMailData> getProducer(){
		Map<String, Object> props = new HashMap<>();
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 3);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "com.webtrekk.demo.serialize.EMailSerializer");
		KafkaProducer<String, EMailData> producer = new KafkaProducer<String, EMailData>(props);
		
		return producer ;
	}
	
    public static void runProducer(EMailData data) {

    	Producer<String, EMailData> producer = ProducerCreator.getProducer();

      

            ProducerRecord<String, EMailData> record = new ProducerRecord<String, EMailData>(IKafkaConstants.TOPIC_NAME, data.getTo(), data);

            try {
            	RecordMetadata metadata = producer.send(record).get();
            	System.out.println("Record sent with key " + data.getFirstName() + " to partition " + metadata.partition()  + " with offset " + metadata.offset());
            } catch (ExecutionException e) {
            	System.out.println("Error in sending record");
            	System.out.println(e);
            } catch (InterruptedException e) {
            	System.out.println("Error in sending record");
            	System.out.println(e);
            } finally {
            	producer.close();
            }

         

    }
	
	

}
