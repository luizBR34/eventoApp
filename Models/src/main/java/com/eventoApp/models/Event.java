package com.eventoApp.models;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import com.eventoApp.mappers.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("code")
	private long code;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("city")
	private String city;
	
	@JsonProperty("date")
	private Date date;
	
	@JsonProperty("time")
	private Time time;
	
	private List<Guest> guests;

	@Override
	public String toString() {
		return "Event [code=" + code + ", name=" + name + ", city=" + city + ", date=" + DateUtils.formatDate(date) + ", time=" + time
				+ ", guests=" + guests + "]";
	}

}
