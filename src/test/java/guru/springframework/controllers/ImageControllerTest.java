package guru.springframework.controllers;

import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ImageControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    ImageService imageService;

    ImageController controller;

    MockMvc mockMvc;

    @Before
    public void setup(){

        controller = new ImageController(recipeService, imageService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                  .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }


    public void testBadRequestException() throws Exception{

        mockMvc.perform(get("/recipe/dfasdf/uploadimage"))
                .andExpect(view().name("400error"))
                .andExpect(status().isBadRequest());
    }



}