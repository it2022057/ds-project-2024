package gr.hua.dit.ds.ds_project_2024;

import gr.hua.dit.ds.ds_project_2024.entities.User;
import gr.hua.dit.ds.ds_project_2024.repositories.UserRepository;
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
	public void testCreateUser() throws Exception {
		// Arrange
		String userHtml = "username=apiuser&password=pass123&email=apiuser@hua.gr&phone=6946586841";

		// Act
		ResultActions result = mockMvc.perform(post("/saveUser")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.with(csrf())
				.content(userHtml));

		// Assert
		result.andExpect(status().isOk());
	}

	@Test
	public void testLoginUser() throws Exception {
		// User credentials
		String userHtml = "username=loginuser&password=pass12345&email=loginuser@hua.gr&phone=6966896842";

		// Create a user
		mockMvc.perform(post("/saveUser")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.with(csrf())
				.content(userHtml))
				.andExpect(status().isOk());

		// Then proceed with log in
		ResultActions result = mockMvc.perform(post("/login")
				.param("username", "loginuser")
				.param("password", "pass12345")
				.with(csrf()));

		// Assert
		result
				.andExpect(status().is3xxRedirection()) // Typically redirects on successful login
				.andExpect(redirectedUrl("/")); // at home page
	}

	@Test
	public void testInvalidLogin() throws Exception {
		mockMvc.perform(post("/login")
						.param("username", "invalid")
						.param("password", "wrong")
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?error=true"));
	}

	// Secured page requires login
	@Test
	public void testSecuredPageRedirectsToLogin() throws Exception {
		mockMvc.perform(get("/pet")) // protected page
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	public void testCreatePet() throws Exception {
		// Info fields for the new pet
		String petForm = "name=Rex&age=3&species=Dog&sex=Male&approval_status=0&image_path=http://play.min.io/pet-adoption-app/pet-photos/Rex.jpg";

		// Save new pet
		mockMvc.perform(post("/pet/new")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.with(csrf())
						.content(petForm))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	public void testDeleteUser() throws Exception {
		// Find the user to delete
		User user = userRepository.findByUsername("apiuser")
				.orElseThrow(() -> new RuntimeException("User not found in DB"));

		// Perform deletion via the controller
		ResultActions result2 = mockMvc.perform(post("/user/delete/" + user.getId())
				.with(csrf()));

		// Assert
		result2.andExpect(status().is3xxRedirection());
	}
}
