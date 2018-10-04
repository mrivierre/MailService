package com.webtrekk.demo.consumer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


import com.webtrekk.demo.constants.IKafkaConstants;
import com.webtrekk.demo.data.EMailData;
import com.webtrekk.demo.producer.ProducerCreator;
import com.webtrekk.demo.service.IEMailService;

public class ConsumerCreator {


	
	private static Consumer<String, EMailData> getConsumer() {
		Map<String, Object> props = new HashMap<>();
		
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, IKafkaConstants.GROUP_ID_CONFIG);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"com.webtrekk.demo.serialize.EMailDeSerializer");
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, IKafkaConstants.MAX_POLL_RECORDS);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, IKafkaConstants.OFFSET_RESET_EARLIER);
		
		KafkaConsumer<String, EMailData> consumer = new KafkaConsumer<String, EMailData>(props);
		consumer.subscribe(Collections.singletonList(IKafkaConstants.TOPIC_NAME));

		return consumer;
	}

	
	public static void runConsumer(IEMailService eMailService) {
		


		Consumer<String, EMailData> consumer = getConsumer();


		while (true) {

			ConsumerRecords<String, EMailData> consumerRecords = consumer.poll(1000);

			consumerRecords.forEach(record -> {
				System.out.println("Record Key " + record.key());
				System.out.println("Record value " + record.value());
				System.out.println("Record partition " + record.partition());
				System.out.println("Record offset " + record.offset());

				try {
					eMailService.sendEmail(record.value());
					((EMailData)record.value()).setSent(true);
					consumer.commitAsync();
				} catch (Exception e) {
					((EMailData)record.value()).setRetry(((EMailData)record.value()).getRetry() + 1);
					e.printStackTrace();
				} finally {
					if(((EMailData)record.value()).getRetry() <= IKafkaConstants.MAX_RETRY && !((EMailData)record.value()).isSent()) {
						ProducerCreator.runProducer(record.value());
					}
				}
			});

			
		}
//		consumer.close();

	}

}
