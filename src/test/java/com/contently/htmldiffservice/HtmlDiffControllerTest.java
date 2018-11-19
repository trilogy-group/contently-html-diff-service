package com.contently.htmldiffservice;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HtmlDiffControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void noParamsReturnsBadRequest() throws Exception {
    this.mockMvc.perform(get("/diff")).andExpect(status().isBadRequest());
  }

  @Test
  public void onlyOldHtmlParamReturnsBadRequest() throws Exception {
    this.mockMvc.perform(get("/diff").param("oldHtml", "<p>hi!</p>")).andExpect(status().isBadRequest());
  }

  @Test
  public void onlyNewHtmlParamReturnsBadRequest() throws Exception {
    this.mockMvc.perform(get("/diff").param("newHtml", "<p>hi!</p>")).andExpect(status().isBadRequest());
  }

  @Test
  public void returnsDiffedHtml() throws Exception {
    String oldHtml = "<p>I got some text here.<p>";
    String newHtml = "<p>I got <i>even more</i> text here.</p>";

    String expectedResult = "<p>I got <span class=\"diff-html-removed\" id=\"removed-diff-0\" previous=\"first-diff\" changeId=\"removed-diff-0\" next=\"added-diff-0\">some </span><i><span class=\"diff-html-added\" id=\"added-diff-0\" previous=\"removed-diff-0\" changeId=\"added-diff-0\" next=\"last-diff\">even more</span></i><span class=\"diff-html-added\" previous=\"removed-diff-0\" changeId=\"added-diff-0\" next=\"last-diff\"> </span>text here.</p>";

    MockHttpServletRequestBuilder request = get("/diff").param("oldHtml", oldHtml).param("newHtml", newHtml);
    this.mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().string(containsString(expectedResult)));
  }

  @Test
  public void returnsUTF8andHTML() throws Exception {
    MockHttpServletRequestBuilder request = get("/diff").param("oldHtml", "<p>1</p>").param("newHtml", "<p>2</p>");
    this.mockMvc.perform(request).andExpect(header().string("Content-Type", "text/html;charset=UTF-8"));
  }
}
