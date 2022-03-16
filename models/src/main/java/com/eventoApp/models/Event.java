package com.eventoApp.models;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Event implements Serializable {
	
	private static final long serialVersionUID = 4965925525811003013L;

	private long code;

	private String name;

	private String city;

	private String date;

	private String time;
	
	private User user;
	
	private List<Guest> guests;
}



