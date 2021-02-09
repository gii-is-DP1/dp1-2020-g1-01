package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Alumno;
import org.springframework.samples.petclinic.model.Pago;
import org.springframework.samples.petclinic.model.TipoPago;
import org.springframework.samples.petclinic.service.AlumnoService;
import org.springframework.samples.petclinic.service.PagoService;
import org.springframework.samples.petclinic.service.TipoPagoService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(controllers = PagosController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class PagoControllerTests {
	
	private final static String CONCEPTO = "PRIMERA_MATRICULA";
	private final static String NICK_USUARIO = "Evelyn";
	
	private static Pago p;
	@MockBean
	private  TipoPagoService tipoPagoService;
	
	@MockBean
	private PagoService pagoService;
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private AlumnoService alumnoService;
	private Alumno alumno;
	@BeforeEach
	void setup() {
		p = new Pago();
		alumno = new Alumno();
		alumno.setNickUsuario("JaviMartinez7");
		alumno.setContraseya("JaviKuka787");
		alumno.setDniUsuario("45676787Y");
		alumno.setNombreCompletoUsuario("Javi Martinez");
		alumno.setCorreoElectronicoUsuario("javikua7@gmail.com");
		alumno.setNumTelefonoUsuario("677676676");
		alumno.setDireccionUsuario("Calle Pepe");
		alumno.setFechaNacimiento(LocalDate.parse("2000-08-13"));
		alumno.setFechaSolicitud(LocalDate.now());
		TipoPago tipoPago = tipoPagoService.getType("Bizum");
		p.setTipo(tipoPago);
		p.setConcepto(CONCEPTO);
		p.setFecha(LocalDate.now());
		p.setId(30);
		
		p.setTipo(tipoPago);
		p.setAlumnos(alumno);
		
	}
	@WithMockUser(value = "spring")
	@Test
	void testShowPaymentsSet() throws Exception {
		given(this.pagoService.getAllPayments()).willReturn(new HashSet<>());
		mockMvc.perform(get("/pagos")).andExpect(status().isOk());
	}
	
	
	@WithMockUser(value = "spring")
	@Test
	void testStudentListByConcept() throws Exception {
		given(this.pagoService.getStudentsByPayment(CONCEPTO)).willReturn(new ArrayList<>());
		mockMvc.perform(get("/pagos/{concepto}", CONCEPTO)).andExpect(status().isOk());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testStudentNotPaidListByConcept() throws Exception {
		given(this.pagoService.getStudentsNoPayment(CONCEPTO)).willReturn(new ArrayList<>());
		mockMvc.perform(get("/pagos/notPaid/{concepto}", CONCEPTO)).andExpect(status().isOk());
	}
	
	@WithMockUser(value = "Evelyn", authorities = {"alumno"})
	@Test
	void testNotPaidListByStudent() throws Exception {
		given(this.pagoService.getNoPaymentByStudent(NICK_USUARIO)).willReturn(new ArrayList<>());
		mockMvc.perform(get("/pagos/notPaidByStudent/{nickUsuario}", NICK_USUARIO)).andExpect(status().isOk());
	}
	
	@WithMockUser(value = "JaviDeKuka", authorities = {"profesor"})
	@Test
	void testNotPaidListByStudentIsOkAsTeacher() throws Exception {
		given(this.pagoService.getNoPaymentByStudent(NICK_USUARIO)).willReturn(new ArrayList<>());
		mockMvc.perform(get("/pagos/notPaidByStudent/{nickUsuario}", "Paco")).andExpect(status().isOk());
	}
	
	@WithMockUser(value = "Evelyn", authorities = {"alumno"})
	@Test
	void testNotPaidListByStudentIsUnauthorized() throws Exception {
		given(this.pagoService.getNoPaymentByStudent(NICK_USUARIO)).willReturn(new ArrayList<>());
		mockMvc.perform(get("/pagos/notPaidByStudent/{nickUsuario}", "Paco")).andExpect(status().isUnauthorized());
	}
	
	
	
	@WithMockUser(value = "Evelyn", authorities = {"alumno"})
	@Test
	void testPaymentsListByStudent() throws Exception {
		given(this.pagoService.getPaymentsByStudent(NICK_USUARIO)).willReturn(new ArrayList<>());
		mockMvc.perform(get("/pagos/paidByStudent/{nickUsuario}", NICK_USUARIO)).andExpect(status().isOk());
	}

	@WithMockUser(value = "Evelyn", authorities = {"alumno"})
	@Test
	void testPaymentsListByStudentIsUnathorized() throws Exception {
		given(this.pagoService.getPaymentsByStudent(NICK_USUARIO)).willReturn(new ArrayList<>());
		mockMvc.perform(get("/pagos/paidByStudent/{nickUsuario}", "Paco")).andExpect(status().isUnauthorized());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testStudentsHaveNotPaidList() throws Exception {
		given(this.pagoService.getNameStudentByNoPago()).willReturn(new ArrayList<>());
		mockMvc.perform(get("/pagos/studentsNotPaid")).andExpect(status().isOk());
	}
	
	
	/*HAY QUE CORREGIR ESTO*/
	@WithMockUser(value = "spring")
	@Test
	void testSendingNewPaymentSucces() throws Exception{
		Gson gson = new Gson();
		String jsonString = gson.toJson(p);
		log.info("Informa: "+jsonString);
		
		mockMvc.perform(post("/pagos/new")
				.contentType(MediaType.APPLICATION_JSON)
			    .content(jsonString)
			    .with(csrf()).sessionAttr("type", "profesor"))
		.andExpect(status().isOk());
	}
	@WithMockUser(value = "spring")
	@Test
	void testCreatePayment() throws Exception{
		Pago p = new Pago();
		p.setConcepto("Viaje a la nada");
		TipoPago tipoPago = tipoPagoService.getType("Bizum");
		p.setTipo(tipoPago);
		System.out.println("ESTE ES EL TIPO: " + tipoPago);
		p.setFecha(LocalDate.of(2020, 12, 12));
		Alumno a  = new Alumno();
		a.setNickUsuario("JaviMartinez7");
		p.setAlumnos(a);
		given(alumnoService.getAlumno("JaviMartinez7")).willReturn(alumno);
		
		mockMvc.perform(post("/pagos/new")
				.contentType(MediaType.APPLICATION_JSON)
			    .content(p.toJson())
			    .with(csrf()))
		.andExpect(status().isCreated());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testCreatePaymentNotOK() throws Exception{
		Pago p = new Pago();
		p.setConcepto("");
		TipoPago tipoPago = tipoPagoService.getType("Bizum");
		p.setTipo(tipoPago);
		System.out.println("ESTE ES EL TIPO: " + tipoPago);
		p.setFecha(LocalDate.of(2020, 12, 12));
		Alumno a  = new Alumno();
		a.setNickUsuario("JaviMartinez7");
		p.setAlumnos(a);
		given(alumnoService.getAlumno("JaviMartinez7")).willReturn(alumno);
		
		mockMvc.perform(post("/pagos/new")
				.contentType(MediaType.APPLICATION_JSON)
			    .content(p.toJson())
			    .with(csrf()))
		.andExpect(status().isNonAuthoritativeInformation());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testCreatePaymentNotOK2() throws Exception{
		Pago p = new Pago();
		p.setConcepto("Viaje");
		TipoPago tipoPago = tipoPagoService.getType("Bizum");
		p.setTipo(tipoPago);
		p.setFecha(LocalDate.of(2020, 12, 12));
		Alumno a  = new Alumno();
		a.setNickUsuario("JaviMartinez7");
		p.setAlumnos(a);
		given(alumnoService.getAlumno("JaviMartinez7")).willReturn(null);
		
		mockMvc.perform(post("/pagos/new")
				.contentType(MediaType.APPLICATION_JSON)
			    .content(p.toJson())
			    .with(csrf()))
		.andExpect(status().isNoContent());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testCreatePaymentNotOK3() throws Exception{
		Pago p = new Pago();
		p.setConcepto("Viaje");
		TipoPago tipoPago = tipoPagoService.getType("Bizum");
		p.setTipo(tipoPago);
		p.setFecha(LocalDate.of(2020, 12, 12));
		Alumno a  = new Alumno();
		a.setNickUsuario("");
		p.setAlumnos(a);		
		mockMvc.perform(post("/pagos/new")
				.contentType(MediaType.APPLICATION_JSON)
			    .content(p.toJson())
			    .with(csrf()))
		.andExpect(status().isNonAuthoritativeInformation());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testCreatePaymentNotOK4() throws Exception{
		Pago p = new Pago();
		p.setConcepto("Viaje");
		TipoPago tipoPago = tipoPagoService.getType("");
		p.setTipo(tipoPago);
		p.setFecha(LocalDate.of(2020, 12, 12));
		Alumno a  = new Alumno();
		a.setNickUsuario("JaviMartinez");
		p.setAlumnos(a);		
		mockMvc.perform(post("/pagos/new")
				.contentType(MediaType.APPLICATION_JSON)
			    .content(p.toJson())
			    .with(csrf()))
		.andExpect(status().isNonAuthoritativeInformation());
	}
	

}