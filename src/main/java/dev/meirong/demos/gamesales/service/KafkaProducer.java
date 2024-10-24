package dev.meirong.demos.gamesales.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String TOPIC_NAME = "csv-import";

  public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessage(String message) {
    // TODO To ensure the message is sent to the correct topic, can record a log message in the db
    // and use a cron job to check the log and resend the message if it fails
    kafkaTemplate.send(TOPIC_NAME, message);
  }
}
