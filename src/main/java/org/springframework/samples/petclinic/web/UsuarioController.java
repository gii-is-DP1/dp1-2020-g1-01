package org.springframework.samples.petclinic.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.service.UsuarioService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class UsuarioController {
	
	private final UsuarioService usuarioService;
	
	@Autowired
	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService=usuarioService;
	}
	
	@GetMapping(value = { "/login" }, produces =  MediaType.APPLICATION_JSON_VALUE )
	public void typeOfUser(@RequestParam("username") String nickUsuario, @RequestParam("password") String contraseya,
			HttpServletResponse response) throws IOException {
		String result="http://localhost:3000";
		String type = usuarioService.typeOfUser(nickUsuario);
		if(type.equals("Username not exist")) {
			result += "/login?message="+type;
		}else {
			Boolean existPassword = usuarioService.existPassword(nickUsuario, contraseya);
			result += existPassword ? "?message="+type : "/login?message=Incorrect password";
		}	
		response.sendRedirect(result);
	}
	
}