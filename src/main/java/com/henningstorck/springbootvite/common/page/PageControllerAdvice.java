package com.henningstorck.springbootvite.common.page;

import com.henningstorck.springbootvite.common.frontend.FrontendService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class PageControllerAdvice {
    private final FrontendService frontendService;

    public PageControllerAdvice(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @ModelAttribute("page")
    public Page page() {
        List<String> stylesheets = frontendService.loadStylesheets();
        List<String> scripts = frontendService.loadScripts();
        return new Page(stylesheets, scripts);
    }
}
