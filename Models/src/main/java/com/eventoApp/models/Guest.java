package com.eventoApp.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Guest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private long id;
	
	@JsonProperty("guestName")
	private String guestName;
	
	private Event event;
}
