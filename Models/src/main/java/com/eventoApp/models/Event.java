package com.eventoApp.models;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import com.eventoApp.mappers.utils.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Event implements Serializable {
	
	private static final long serialVersionUID = 4965925525811003013L;

	private long code;

	private String name;

	private String city;

	private Date date;

	private Time time;

	@Override
	public String toString() {
		return "Event [code=" + code + ", name=" + name + ", city=" + city + ", date=" + DateUtils.formatDate(date) + ", time=" + time + "]";
	}
}



