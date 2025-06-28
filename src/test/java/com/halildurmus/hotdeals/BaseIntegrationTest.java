package com.halildurmus.hotdeals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("integration-test")
@AutoConfigureJsonTesters
@AutoConfigureMockMvc(addFilters = false)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class BaseIntegrationTest {


  static {
    // Force container reuse
    System.setProperty("embedded.containers.reuse", "true");
    System.setProperty("testcontainers.reuse.enable", "true");
  }

  // See https://stackoverflow.com/questions/53514532/
  protected <T> T asParsedJson(Object object) throws JsonProcessingException {
    var json = new ObjectMapper().writeValueAsString(object);
    return JsonPath.read(json, "$");
  }
}
