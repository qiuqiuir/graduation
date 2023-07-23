package com.cslg.graduation.controller;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.entity.Acnumber;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.service.AcnumberService;
import com.cslg.graduation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @auther xurou
 * @date 2023/4/18
 */
@RestController
@RequestMapping("/acnumber")
public class AcnumberController {

    @Autowired
    private AcnumberService acnumberService;

    @Autowired
    private UserService userService;

    @RequestMapping("/getAll")
    public ResponseService getAll(@RequestParam(value = "date",required = true)String time) throws ParseException {
        List<Map<String,Object>> result = new ArrayList<>();
        List<User> userList = userService.getACMer();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date date = ft.parse(time);
        for(User user:userList){
            Map<String, Object> map = new HashMap<>();
            String username = user.getUsername();
            map.put("username",username);
            map.put("name",user.getName());
            map.put("atcoder",acnumberService.getCount(username,"atcoder",date));
            map.put("cf",acnumberService.getCount(username,"codeforces",date));
            map.put("luogu",acnumberService.getCount(username,"luogu",date));
            map.put("vjudge",acnumberService.getCount(username,"vjudge",date));
            map.put("nowcoder",acnumberService.getCount(username,"nowcoder",date));
            map.put("sum",acnumberService.getAllCount(username,date));
            result.add(map);
        }
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer x = (Integer) o1.get("sum");
                Integer y = (Integer) o2.get("sum");
                return y.compareTo(x);
            }
        });
        return ResponseService.createBySuccess(result);
    }

    @RequestMapping("/getAcNumber/{id}")
    public ResponseService getAcNumberById(@PathVariable("id")String id){
        List<Acnumber> acnumbers = acnumberService.selectAcNumberByUsername(id);
        List<Map<String,Object>>result = new ArrayList<>();
        if(acnumbers.size()>0) {
            Map<String, Object> map = new HashMap<>();
            int count = acnumbers.get(0).getCount();
            for (int i = 1; i < acnumbers.size(); i++) {
                if (acnumbers.get(i).getTime().equals(acnumbers.get(i - 1).getTime())) {
                    count += acnumbers.get(i).getCount();
                } else {
                    map.put("time", acnumbers.get(i - 1).getTime());
                    map.put("count", count);
                    result.add(map);
                    map = new HashMap<>();
                    count = acnumbers.get(i).getCount();
                }
            }
            map.put("time", acnumbers.get(acnumbers.size() - 1).getTime());
            map.put("count", count);
            result.add(map);

            for (int i = result.size() - 1; i > 0; i--) {
                int hou = (int) result.get(i).get("count");
                int qian = (int) result.get(i - 1).get("count");
                int cha = hou - qian;
                result.get(i).put("count", cha);
            }
            result.get(0).put("count", 0);
        }
        return ResponseService.createBySuccess(result);
    }

}
