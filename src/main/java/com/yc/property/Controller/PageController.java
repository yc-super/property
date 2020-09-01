package com.yc.property.Controller;

import com.yc.property.annotation.PassToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {
    @PassToken
    @RequestMapping("{page}")
    public String showpage(@PathVariable String page){
        return page;
    }
}
