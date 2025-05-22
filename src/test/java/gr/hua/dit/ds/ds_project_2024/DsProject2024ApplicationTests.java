package gr.hua.dit.ds.ds_project_2024;

import gr.hua.dit.ds.ds_project_2024.entities.User;
import gr.hua.dit.ds.ds_project_2024.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DsLab2024ApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserRepository userRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	@Transactional
	public void testCreateUser() throws Exception {
		// Arrange
		String userHtml = "username=apiuser&password=pass123&email=apiuser@hua.gr&phone=6946586841";

		// Act
		ResultActions result1 = mockMvc.perform(post("/saveUser")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.with(csrf())
				.content(userHtml));

		// Assert
		result1.andExpect(status().isOk());

		User user = userRepository.findByUsername("apiuser")
				.orElseThrow(() -> new RuntimeException("User not found in DB"));

		// Perform deletion via the controller
		ResultActions result2 = mockMvc.perform(post("/user/delete/" + user.getId())
				.with(csrf()));

		// Assert
		result2.andExpect(status().is3xxRedirection());
	}

	@Test
	public void testLoginUser() throws Exception {
		// Arrange
		String userHtml = "username=apiuser&password=pass123";

		// Act
		ResultActions result = mockMvc.perform(get("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content(userHtml));

		// Assert
		result.andExpect(status().isOk());
	}

	// Secured page requires login
	@Test
	public void testSecuredPageRedirectsToLogin() throws Exception {
		mockMvc.perform(get("/pet")) // protected page
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}
}
