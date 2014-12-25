package tests;

import hello.RestfulController;
import hello.beans.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class, RestfulControllerTest.TestConfig.class})
public class RestfulControllerTest {	
	
	@Configuration
	static class TestConfig {
		
		@Bean
		public RestfulController restfulController() {
			return new RestfulController();
		}
	}
	
	@Autowired
	private RestfulController restfulController;	
	
	@Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        
        User user = new User();
        Authentication auth = new UsernamePasswordAuthenticationToken(user,null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
	
	@Test
    public void testTest() throws Exception {
   
		this.mockMvc.perform(get("/getMyTwitterProfile?function={f1}", "f1"))
        	.andExpect(content().contentType("application/json"))
        	.andExpect(jsonPath("$.id").value(1))
        	.andExpect(jsonPath("$.name").value("Nesa"));
    }
}