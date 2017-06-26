package com.company.exception.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.company.model.error.ApplicationError;

@Controller
@EnableAutoConfiguration
public class OopsController {

	@RequestMapping("/oops")
	public ModelAndView presentViewForException(HttpServletRequest request, HttpServletResponse response){
		ApplicationError error = new ApplicationError();
		error.setStatus((int)request.getAttribute("status"));
		error.setMessage((String)request.getAttribute("message"));
		error.setDescription((String)request.getAttribute("description"));
		error.setAction((String)request.getAttribute("action"));
		response.setStatus((int)request.getAttribute("status"));
		return new ModelAndView("oops", "error", error); 
	}

	@RequestMapping(value="/oops", produces={"application/json"})
	public ResponseEntity<ApplicationError> presentJSONForException(HttpServletRequest request, HttpServletResponse response){
		ApplicationError error = new ApplicationError();
		error.setStatus((int)request.getAttribute("status"));
		error.setMessage((String)request.getAttribute("message"));
		error.setDescription((String)request.getAttribute("description"));
		error.setAction((String)request.getAttribute("action"));
		return new ResponseEntity<ApplicationError>(error,HttpStatus.valueOf(error.getStatus()));
	}
}