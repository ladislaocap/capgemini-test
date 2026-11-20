package com.capgemini.test.code;

import com.capgemini.test.code.application.exception.DniRejectedException;
import com.capgemini.test.code.application.port.out.DniValidator;
import com.capgemini.test.code.application.port.out.EmailNotifier;
import com.capgemini.test.code.application.port.out.SmsNotifier;
import com.capgemini.test.code.infrastructure.persistence.SpringDataUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserAcceptanceTest {
  @Container
  private static final PostgreSQLContainer POSTGRES =
      new PostgreSQLContainer("postgres:17").withDatabaseName("users_test");

  @Autowired private MockMvc mockMvc;
  @Autowired private SpringDataUserRepository users;
  @MockBean private DniValidator dniValidator;
  @MockBean private EmailNotifier emailNotifier;
  @MockBean private SmsNotifier smsNotifier;

  @DynamicPropertySource
  static void registerDatabaseProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
    registry.add("spring.datasource.driver-class-name", POSTGRES::getDriverClassName);
  }

  @BeforeEach
  void clearUsers() {
    users.deleteAll();
  }

  @Test
  void createsAdminAndReturnsOnlyGeneratedId() throws Exception {
    mockMvc.perform(post("/api/v1/rooms/1/users")
            .contentType(APPLICATION_JSON)
            .content(userJson("pablo", "pablo@example.com", "admin", "23454234W")))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").doesNotExist());

    verify(emailNotifier).send("pablo@example.com", "usuario guardado");
  }

  @Test
  void getsUserById() throws Exception {
    String location = mockMvc.perform(post("/api/v1/rooms/1/users")
            .contentType(APPLICATION_JSON)
            .content(userJson("pablo", "pablo@example.com", "admin", "23454234W")))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getHeader("Location");

    mockMvc.perform(get(location))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("pablo"))
        .andExpect(jsonPath("$.rol").value("admin"));
  }

  @Test
  void rejectsDuplicateEmailWithConflict() throws Exception {
    mockMvc.perform(post("/api/v1/rooms/1/users")
        .contentType(APPLICATION_JSON)
        .content(userJson("pablo", "pablo@example.com", "admin", "23454234W")));

    mockMvc.perform(post("/api/v1/rooms/1/users")
            .contentType(APPLICATION_JSON)
            .content(userJson("pablo", "pablo@example.com", "admin", "23454234W")))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value(409))
        .andExpect(jsonPath("$.message").value("error validation email"));
  }

  @Test
  void rejectsInvalidDniWithoutPersisting() throws Exception {
    doThrow(new DniRejectedException()).when(dniValidator).validate("99999999w");

    mockMvc.perform(post("/api/v1/rooms/1/users")
            .contentType(APPLICATION_JSON)
            .content(userJson("pablo", "pablo@example.com", "admin", "99999999w")))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value(409))
        .andExpect(jsonPath("$.message").value("error validation dni"));

    org.assertj.core.api.Assertions.assertThat(users.count()).isZero();
  }

  @Test
  void sendsSmsForSuperadmin() throws Exception {
    mockMvc.perform(post("/api/v1/rooms/1/users")
            .contentType(APPLICATION_JSON)
            .content(userJson("pablo", "pablo@example.com", "superadmin", "23454234W")))
        .andExpect(status().isCreated());

    verify(smsNotifier).send("677998899", "usuario guardado");
  }

  @Test
  void mapsInvalidNameToExpectedConflictContract() throws Exception {
    mockMvc.perform(post("/api/v1/rooms/1/users")
            .contentType(APPLICATION_JSON)
            .content(userJson("toolong", "pablo@example.com", "admin", "23454234W")))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value(409))
        .andExpect(jsonPath("$.message").value("error validation userName"));
  }

  private String userJson(String name, String email, String role, String dni) {
    return """
        {"name":"%s","email":"%s","phone":"677998899","rol":"%s","dni":"%s"}
        """.formatted(name, email, role, dni);
  }
}
