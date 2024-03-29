package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.Oj;
import com.cslg.graduation.entity.Rating;
import com.cslg.graduation.service.OjService;
import com.cslg.graduation.service.SpiderService;
import com.cslg.graduation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @auther xurou
 * @date 2023/4/3
 */

@RestController
@RequestMapping("/oj")

public class OjController {

    @Autowired
    private OjService ojService;

    @Autowired
    private UserService userService;

    @Autowired
    private SpiderService spiderService;

    @PreAuthorize("hasAuthority('user')")
    @PostMapping("/addOj")
    public ResponseService addOj(@RequestBody Oj oj) {
        ojService.addOj(oj);
        return ResponseService.createBySuccess();
    }

    @RequestMapping("/getAll/{username}")
    public ResponseService getAllOj(@PathVariable("username") String username) {
        List<Oj> ojList = ojService.getAllOjId(username);
        return ResponseService.createBySuccess(ojList);
    }



    @PreAuthorize("hasAuthority('user')")
    @PostMapping("/updateOj/all/{username}")
    public ResponseService updateAllOj(@PathVariable("username") String username, @RequestBody Map<String, List<String>> ojList) {
        ojService.deleteOjByUsername(username);
        for (Map.Entry<String, List<String>> entry : ojList.entrySet()) {
            Oj oj = new Oj()
                    .setUsername(username)
                    .setPlatform(entry.getKey())
                    .setHistoryRating(0)
                    .setNowRating(0);
            for (String ojId : entry.getValue()) {
                oj = oj.setOjId(ojId);
                ojService.addOj(oj);
            }
        }
        return ResponseService.createBySuccess();
    }

    @PreAuthorize("hasAuthority('user')")
    @PostMapping("/updateOj/atcoder/{username}")
    public ResponseService updateAtcoderOj(@PathVariable("username") String username, @RequestBody List<String> ojList) {
        ojService.updateOjByPlatform(username, "atcoder", ojList);
        return ResponseService.createBySuccess();
    }

    @PreAuthorize("hasAuthority('user')")
    @PostMapping("/updateOj/nowcoder/{username}")
    public ResponseService updateNowcoderOj(@PathVariable("username") String username, @RequestBody List<String> ojList) {
        ojService.updateOjByPlatform(username, "nowcoder", ojList);
        return ResponseService.createBySuccess();
    }

    @PreAuthorize("hasAuthority('user')")
    @PostMapping("/updateOj/codeforces/{username}")
    public ResponseService updateCodeforcesOj(@PathVariable("username") String username, @RequestBody List<String> ojList) {
        ojService.updateOjByPlatform(username, "codeforces", ojList);
        return ResponseService.createBySuccess();
    }

    @PreAuthorize("hasAuthority('user')")
    @PostMapping("/updateOj/hdu/{username}")
    public ResponseService updateHduOj(@PathVariable("username") String username, @RequestBody List<String> ojList) {
        ojService.updateOjByPlatform(username, "hdu", ojList);
        return ResponseService.createBySuccess();
    }

    @PreAuthorize("hasAuthority('user')")
    @PostMapping("/updateOj/acwing/{username}")
    public ResponseService updateAcwingOj(@PathVariable("username") String username, @RequestBody List<String> ojList) {
        ojService.updateOjByPlatform(username, "acwing", ojList);
        return ResponseService.createBySuccess();
    }

    @PreAuthorize("hasAuthority('user')")
    @PostMapping("/updateOj/luogu/{username}")
    public ResponseService updateLuoguOj(@PathVariable("username") String username, @RequestBody List<String> ojList) {
        ojService.updateOjByPlatform(username, "luogu", ojList);
        return ResponseService.createBySuccess();
    }

    @RequestMapping("/rating")
    public ResponseService getRating(){
        List<Rating> ojList= ojService.getRating();
        return ResponseService.createBySuccess(ojList);
    }

}
