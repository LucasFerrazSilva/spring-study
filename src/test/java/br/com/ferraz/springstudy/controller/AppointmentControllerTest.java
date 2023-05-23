package br.com.ferraz.springstudy.controller;

import br.com.ferraz.springstudy.domain.appointment.AppointmentInfosDTO;
import br.com.ferraz.springstudy.domain.appointment.AppointmentService;
import br.com.ferraz.springstudy.domain.appointment.NewAppointmentDTO;
import br.com.ferraz.springstudy.domain.doctor.Expertise;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<NewAppointmentDTO> newAppointmentDTOJson;
    @Autowired
    private JacksonTester<AppointmentInfosDTO> appointmentInfosDTOJson;

    @MockBean
    private AppointmentService service;

    @Test
    @DisplayName("Deve devolver codigo htttp 400 quando informações estão inválidas")
    @WithMockUser
    void textScheduleCase1() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/consultas")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve devolver codigo htttp 200 quando informações estão válidas")
    @WithMockUser
    void textScheduleCase2() throws Exception {
        var patientId = 2l;
        var doctorId = 5l;
        var appointmentTime = LocalDateTime.now().plusHours(1);

        var newAppointmentDTO = new NewAppointmentDTO(patientId, doctorId, Expertise.CARDIOLOGIA, appointmentTime);
        var expectedDTO = new AppointmentInfosDTO(null, patientId, doctorId, appointmentTime);

        when(service.scheduleAppointment(any())).thenReturn(expectedDTO);

        MockHttpServletResponse response =
                mockMvc.perform(
                            post("/consultas")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(newAppointmentDTOJson.write(newAppointmentDTO).getJson())
                        )
                        .andReturn()
                        .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(appointmentInfosDTOJson.write(expectedDTO).getJson());
    }

}