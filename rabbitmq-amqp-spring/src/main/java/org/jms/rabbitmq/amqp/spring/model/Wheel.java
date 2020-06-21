package org.jms.rabbitmq.amqp.spring.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Wheel implements Serializable {

    private int id;

    private String manufacturer;

    private String model;

    private Season season;

}
