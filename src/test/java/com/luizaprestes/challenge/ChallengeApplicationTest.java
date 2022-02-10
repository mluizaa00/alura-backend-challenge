package com.luizaprestes.challenge;

import com.luizaprestes.challenge.model.security.AuthenticationRequest;
import com.luizaprestes.challenge.util.JacksonAdapter;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ChallengeApplicationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testWrongAuthenticationToReturn400() throws Exception {
    final URI uri = new URI("/auth");
    final AuthenticationRequest json = new AuthenticationRequest("test", "nothing");

    mockMvc.perform(MockMvcRequestBuilders
        .post(uri)
        .content(JacksonAdapter.getInstance().serialize(json))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers
          .status()
          .is(400));
  }

}
