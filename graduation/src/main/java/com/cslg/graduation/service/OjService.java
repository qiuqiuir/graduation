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
     * 根据学号，平台，oj的id，更新当前分数
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
     * 根据学号，平台，oj的id，更新历史最高分
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
     * 获取某平台所有oj信息
     *
     * @param platform
     * @return
     */
    public List<Oj> selectOjByPlatform(String platform) {
        return ojMapper.selectOjByPlatform(platform);
    }

    /**
     * 获取某个平台上所有用户当前rating，按学号升序
     *
     * @param platform
     * @return
     */
    public List<Oj> getOjByPlatform(String platform) {
        List<Oj> ojList = selectOjByPlatform(platform);
        Map<String, Integer> maxRating = new HashMap<>();
        for (Oj oj : ojList) {
            String username = oj.getUsername();
            int rating = oj.getNowRating();
            if (maxRating.containsKey(username)) {
                if (rating > maxRating.get(username)) {
                    maxRating.put(username, rating);
                }
            } else {
                maxRating.put(username, rating);
            }
        }
        List<Oj> ojRating = new ArrayList<>();
        for (Oj oj : ojList) {
            if (oj.getNowRating() == maxRating.get(oj.getUsername())) {
                ojRating.add(oj);
            }
        }
        return ojRating;
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

    /**
     * 获取所有学生的所有rating
     *
     * @return
     */
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
                map.get(username).setAtcoderUrl("https://atcoder.jp/users/" + oj.getOjId());
            } else {
                String name = userService.findUserByUsername(username).getName();
                Rating now = new Rating()
                        .setUsername(username)
                        .setName(name)
                        .setAtcoder(rating)
                        .setAtcoderUrl("https://atcoder.jp/users/" + oj.getOjId());
                map.put(username, now);
            }
        }
        for (Oj oj : nowcoder) {
            String username = oj.getUsername();
            int rating = oj.getNowRating();
            if (map.containsKey(username)) {
                map.get(username).setNowcoder(rating);
                map.get(username).setNowcoderUrl("https://ac.nowcoder.com/acm/contest/profile/" + oj.getOjId());
            } else {
                String name = userService.findUserByUsername(username).getName();
                Rating now = new Rating()
                        .setUsername(username)
                        .setName(name)
                        .setNowcoder(rating)
                        .setNowcoderUrl("https://ac.nowcoder.com/acm/contest/profile/" + oj.getOjId());
                map.put(username, now);
            }
        }

        for (Oj oj : codeforces) {
            String username = oj.getUsername();
            int rating = oj.getNowRating();
            if (map.containsKey(username)) {
                map.get(username).setCodeforces(rating);
                map.get(username).setCodeforcesUrl("https://codeforces.com/profile/" + oj.getOjId());
            } else {
                String name = userService.findUserByUsername(username).getName();
                Rating now = new Rating()
                        .setUsername(username)
                        .setName(name)
                        .setCodeforces(rating)
                        .setCodeforcesUrl("https://codeforces.com/profile/" + oj.getOjId());
                map.put(username, now);
            }
        }

        List<Rating> ratingList = new ArrayList<>();

        for (Map.Entry<String, Rating> entry : map.entrySet()) {
            ratingList.add(entry.getValue());
        }

        // 按照cf分数降序排序
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
