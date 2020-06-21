package org.jms.rabbitmq.amqp.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@ToString
@EqualsAndHashCode(exclude = "id")
public class Wheel implements Serializable {

    @Getter
    private int id;

    private String manufacturer;

    private String model;

    private Season season;

}
