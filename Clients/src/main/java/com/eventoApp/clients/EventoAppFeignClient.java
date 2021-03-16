package com.eventoApp.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eventoApp.models.Event;
import com.eventoApp.models.Guest;
import com.eventoApp.models.Role;
import com.eventoApp.models.User;

@FeignClient(name = "evento-cache", url = "${EVENTO_CACHE_URI:http://localhost:8585}")
public interface EventoAppFeignClient {
	
	@GetMapping("/eventoCache/")
	public ResponseEntity<List<Event>> eventList();
	
	
	@GetMapping("/eventoCache/searchEvent/{code}")
	public ResponseEntity<Event> seekEvent(@PathVariable("code") long code);
	
	
	@GetMapping("/eventoCache/seekUser/{login}")
	public ResponseEntity<User> seekUser(@PathVariable("login") String login);
	
	
	@GetMapping("/eventoCache/listGuests/{eventCode}")
	public ResponseEntity<List<Guest>> guestList(@PathVariable("eventCode") long eventCode);
	
	
	@DeleteMapping("/eventoCache/deleteEvent/{code}")
	public ResponseEntity<Void> deleteEvent(@PathVariable("code") long code);
	
	
	@DeleteMapping("/eventoCache/deleteGuest/{id}")
	public ResponseEntity<Event> deleteGuest(@PathVariable("id") long id);
	
	
	@GetMapping("/eventoCache/seekRole/{theRoleName}")
	public ResponseEntity<Role> seekRoleByName(@PathVariable("theRoleName") String theRoleName);
}