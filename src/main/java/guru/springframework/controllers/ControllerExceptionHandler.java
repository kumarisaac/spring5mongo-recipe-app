package guru.springframework.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
//@ControllerAdvice
public class ControllerExceptionHandler {

  //  @ResponseStatus(HttpStatus.BAD_REQUEST)
   // @ExceptionHandler
    public ModelAndView handleHTTPBadRequest(Exception exception){

        ModelAndView mv = new ModelAndView();
        mv.setViewName("400error");
        mv.addObject("exception", exception);
        return mv;
    }
}
