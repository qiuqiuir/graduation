package com.cslg.graduation.service;

import com.cslg.graduation.dao.OjMapper;
import com.cslg.graduation.entity.Oj;
import com.cslg.graduation.entity.Rating;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @auther xurou
 * @date 2023/3/31
 */
@Service
public class OjService {

    @Autowired
    private OjMapper ojMapper;

    @Autowired
    private UserService userService;

    /**
     * 新增一条oj记录
     *
     * @param oj
     */
    public void addOj(Oj oj) {
        ojMapper.insertOj(oj);
    }

    /**
     * 根据学号和平台获取该用户的所有第三方id
     *
     * @param username
     * @param platform
     * @return
     */
    public List<String> getAllOjId(String username, String platform) {
        return ojMapper.selectListOjId(username, platform);
    }

    /**
     * 获取所有oj信息
     *
     * @return
     */
    public List<Oj> getAllOj() {
        return ojMapper.selectAllOj();
    }

    /**
     * 获取某个学号所有的第三方id
     *
     * @param username
     * @return
     */
    public List<Oj> getAllOjId(String username) {
        return ojMapper.selectListOj(username);
    }

    /**
     * 根据学号和平台删除该用户所有oj
     *
     * @param username
     * @param platform
     */
    public void deleteOjByUsername(String username, String platform) {
        ojMapper.deleteOjByUsernameAndPlatform(username, platform);
    }

    /**
     * 根据学号删除该用户所有oj
     *
     * @param username
     */
    public void deleteOjByUsername(String username) {
        ojMapper.deleteOjByUsername(username);
    }

    /**
     * 根据学号，平台，oj的id，当前分数更新
     *
     * @param username
     * @param platform
     * @param ojId
     * @param nowRating
     */
    public void updateNowRating(String username, String platform, String ojId, int nowRating) {
        ojMapper.updateNowRating(username, platform, ojId, nowRating);
    }

    /**
     * 根据学号，平台，oj的id，历史最高分更新
     *
     * @param username
     * @param platform
     * @param ojId
     * @param historyRating
     */
    public void updateHistoryRating(String username, String platform, String ojId, int historyRating) {
        ojMapper.updateHistoryRating(username, platform, ojId, historyRating);
    }

    /**
     * 获取某个平台上所有用户当前rating，按学号升序
     *
     * @param platform
     * @return
     */
    public List<Oj> getOjByPlatform(String platform) {
        List<Oj> ojList = ojMapper.selectOjByPlatform(platform);
        Collections.sort(ojList, new Comparator<Oj>() {
            @Override
            public int compare(Oj o1, Oj o2) {
                String a = o1.getUsername();
                String b = o2.getUsername();
                return a.compareTo(b);
            }
        });
        return ojList;
    }

    /**
     * 根据学号，平台，修改后的oj列表更新
     *
     * @param username
     * @param platform
     * @param ojList
     */
    public void updateOjByPlatform(String username, String platform, List<String> ojList) {
        deleteOjByUsername(username, platform);
        Oj oj = new Oj()
                .setUsername(username)
                .setPlatform(platform)
                .setHistoryRating(0)
                .setNowRating(0);
        for (String ojId : ojList) {
            oj = oj.setOjId(ojId);
            addOj(oj);
        }
    }

    public List<Rating> getRating() {
        List<Oj> atcoder = getOjByPlatform("atcoder");
        List<Oj> codeforces = getOjByPlatform("codeforces");
        List<Oj> nowcoder = getOjByPlatform("nowcoder");
        Map<String, Rating> map = new HashMap<>();
        for (Oj oj : atcoder) {
            String username = oj.getUsername();
            int rating = oj.getNowRating();
            if (map.containsKey(username)) {
                map.get(username).setAtcoder(rating);
            } else {
                String name = userService.findUserByUsername(username).getName();
                Rating now = new Rating()
                        .setUsername(username)
                        .setName(name)
                        .setAtcoder(rating);
                map.put(username, now);
            }
        }
        for (Oj oj : nowcoder) {
            String username = oj.getUsername();
            int rating = oj.getNowRating();
            if (map.containsKey(username)) {
                map.get(username).setNowcoder(rating);
            } else {
                String name = userService.findUserByUsername(username).getName();
                Rating now = new Rating()
                        .setUsername(username)
                        .setName(name)
                        .setNowcoder(rating);
                map.put(username, now);
            }
        }

        for (Oj oj : codeforces) {
            String username = oj.getUsername();
            int rating = oj.getNowRating();
            if (map.containsKey(username)) {
                map.get(username).setCodeforces(rating);
            } else {
                String name = userService.findUserByUsername(username).getName();
                Rating now = new Rating()
                        .setUsername(username)
                        .setName(name)
                        .setCodeforces(rating);
                map.put(username, now);
            }
        }

        List<Rating> ratingList = new ArrayList<>();

        for (Map.Entry<String, Rating> entry : map.entrySet()) {
            ratingList.add(entry.getValue());
        }

        Collections.sort(ratingList, new Comparator<Rating>() {
            @Override
            public int compare(Rating o1, Rating o2) {
                Integer a = o2.getCodeforces();
                Integer b = o1.getCodeforces();
                return a.compareTo(b);
            }
        });
        return ratingList;
    }

}
