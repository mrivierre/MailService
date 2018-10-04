package com.webtrekk.demo.serialize;


import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.webtrekk.demo.data.EMailData;

public class EMailDeSerializer implements Deserializer<EMailData> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {

	}

	@Override

	public EMailData deserialize(String topic, byte[] data) {

		ObjectMapper mapper = new ObjectMapper();

		EMailData object = null;

		try {

			object = mapper.readValue(data, EMailData.class);

		} catch (Exception exception) {

			System.out.println("Error in deserializing bytes " + exception);

		}

		return object;

	}

	@Override

	public void close() {

	}

}