package com.eventoApp.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Guest implements Serializable {

	private static final long serialVersionUID = -8519957630327736235L;

	private Long id;
	
	private String guestName;
	
	private Event event;
}