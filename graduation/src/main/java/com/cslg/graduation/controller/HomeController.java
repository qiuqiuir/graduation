package com.cslg.graduation.controller;

import com.cslg.graduation.dao.DiscussPostMapper;
import com.cslg.graduation.entity.DiscussPost;
import com.cslg.graduation.entity.Page;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.service.DiscussPostService;
import com.cslg.graduation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther xurou
 * @date 2022/6/7
 */

@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page) {
        // 方法调用前,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象的属性.
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for(DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
//                User user = userService.findUserById(post.getUserId());
//                map.put("user",user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "/index";
    }

}
