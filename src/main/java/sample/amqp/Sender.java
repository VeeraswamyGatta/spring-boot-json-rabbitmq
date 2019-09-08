/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.amqp;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.Resource;

public class Sender {
	private static final Logger logger = LoggerFactory.getLogger(Sender.class);
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${classpath:data.json}")
	private Resource dataResource;

	@Scheduled(fixedDelay = 1000L)
	public void send() {

		ObjectMapper mapper = new ObjectMapper();
		try
		{
			List<Data> dataList = Arrays.asList(mapper.readValue(new File(String.valueOf(dataResource.getFile())), Data[].class));
			for (Data data : dataList ) {
				this.rabbitTemplate.convertAndSend("scdf_amq_test", data);
				logger.info(" Data published sucessfully to rabbitmq  :   " + data.getName());
			}
		}
		catch (JsonGenerationException e)
		{
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
