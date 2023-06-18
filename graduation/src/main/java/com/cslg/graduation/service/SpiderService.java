package com.cslg.graduation.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cslg.graduation.entity.Acnumber;
import com.cslg.graduation.entity.Oj;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.util.GetUrlJson;
import com.cslg.graduation.util.GraduationUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @auther xurou
 * @date 2023/3/31
 */

@Service
public class SpiderService {

    @Autowired
    private UserService userService;

    @Autowired
    private AcnumberService acnumberService;

    @Autowired
    private OjService ojService;

    /**
     * 根据牛客比赛id获取本周周赛情况
     *
     * @param id
     * @return Map<String, Double> 用户id-积分
     */
    public Map<String, Double> getNowcoderScore(String id) {
        String url = "https://ac.nowcoder.com/acm-heavy/acm/contest/real-time-rank-data?token=&id=" + id + "&limit=0&_=1643019279364";
        JSONObject data = null;
        try {
            data = GetUrlJson.getHttpJson(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        data = data.getJSONObject("msg");
        data = data.getJSONObject("data");
        JSONObject basicInfo = data.getJSONObject("basicInfo");
        // 提交记录的页数
        int pageCount = basicInfo.getInteger("pageCount");
        JSONArray rankData = data.getJSONArray("rankData");
        // 第一名过了多少题
        int acceptedFirstCount = rankData.getJSONObject(0).getInteger("acceptedCount");
        // 实际过题人数
        int peopleCount = 0;
        Map<String, Double> nowcoderData = new HashMap<>();
        // 第一次遍历获取实际过题人数
        for (int i = 1; i <= pageCount; i++) {
            String nowUrl = "https://ac.nowcoder.com/acm-heavy/acm/contest/real-time-rank-data?token=&id=" + id + "&page=" + i + "&limit=0&_=1643019279364";
            JSONObject nowData = null;
            try {
                nowData = GetUrlJson.getHttpJson(nowUrl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            nowData = nowData.getJSONObject("msg");
            nowData = nowData.getJSONObject("data");
            JSONArray nowRank = nowData.getJSONArray("rankData");
            for (int j = 0; j < nowRank.size(); j++) {
                int nowCount = nowRank.getJSONObject(j).getInteger("acceptedCount");
                if (nowCount > 0) peopleCount++;
            }
        }

        // 第二次遍历获取userId的排名和过题数计算分数
        for (int i = 1; i <= pageCount; i++) {
            // 爬虫网页
            String nowUrl = "https://ac.nowcoder.com/acm-heavy/acm/contest/real-time-rank-data?token=&id=" + id + "&page=" + i + "&limit=0&_=1643019279364";
            JSONObject nowData = null;
            try {
                nowData = GetUrlJson.getHttpJson(nowUrl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            nowData = nowData.getJSONObject("msg");
            nowData = nowData.getJSONObject("data");
            // 排行榜数据
            JSONArray nowRank = nowData.getJSONArray("rankData");
            for (int j = 0; j < nowRank.size(); j++) {
                // 用户通过题数
                int userAcCount = nowRank.getJSONObject(j).getInteger("acceptedCount");
                // 用户排名
                int ranking = nowRank.getJSONObject(j).getInteger("ranking");
                // 计算积分
                double score = 100.0 * (userAcCount * 1.0 / acceptedFirstCount) * (2 * peopleCount - 2) / (peopleCount + ranking - 2);
                nowcoderData.put(nowRank.getJSONObject(j).getString("uid"), score);
            }
        }
        return nowcoderData;
    }

    /**
     * 根据atcoder比赛id获取该场周赛的积分
     *
     * @param id
     * @return Map<String, Double> 用户id-积分
     */
    public Map<String, Double> getAtcoderScore(String id) {
        String url = "https://atcoder.jp/contests/" + id + "/standings/json";
        JSONObject data = null;
        Map<String, String> cookies = new HashMap<>();
        cookies.put("Cookie", "__pp_uid=vdqR1VkSU6uJg6zkMhQwpihMkBcWkMx9; language=en; _ga=GA1.2.1995201393.1572173694; timeDelta=-1238; _gid=GA1.2.336295427.1682778439; REVEL_FLASH=; REVEL_SESSION=03a566f83538bb6d102deacfbb1bad5898254df4-%00SessionKey%3A02b0da9b11f81cb57ce7b46d445ea80c258518ddd5da9b33f1c1888ffdc1204d9352dc64f62b33-afc046088e869e19750e81ad308264b43f7dbd18a057973be1876990a34a7da4%00%00UserName%3Acslg093119134%00%00UserScreenName%3Acslg093119134%00%00csrf_token%3A3NacyQTQR%2BQSIlCHzwKfZ73PMsbQBhGa6MrkuiMQcWM%3D%00%00w%3Afalse%00%00Rating%3A1513%00%00a%3Afalse%00%00_TS%3A1698330456%00");
        cookies.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.54");
        try {
            data = GetUrlJson.getHttpJson(url, cookies);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        data = data.getJSONObject("msg");
        JSONArray standingsData = data.getJSONArray("StandingsData");
        int acceptedFirstCount = standingsData.getJSONObject(0).getJSONObject("TotalResult").getInteger("Accepted");
        Map<String, Double> rankData = new HashMap<>();
        int peopleCount = standingsData.size();
        for (int j = 0; j < standingsData.size(); j++) {
            int userAcCount = standingsData.getJSONObject(j).getJSONObject("TotalResult").getInteger("Accepted");
            int ranking = standingsData.getJSONObject(j).getInteger("Rank");
            double score = 100.0 * (userAcCount * 1.0 / acceptedFirstCount) * (2 * peopleCount - 2) / (peopleCount + ranking - 2);
            rankData.put(standingsData.getJSONObject(j).getString("UserName"), score);
            rankData.put(standingsData.getJSONObject(j).getString("UserScreenName"), score);
        }
        return rankData;
    }

    /**
     * 根据用户cf的id统计该用户通过题目的知识点情况
     *
     * @param id
     * @return Map<String, Integer> 知识点-个数
     */
    public Map<String, Integer> getCfSubmission(String id) {
        String url = "https://codeforces.com/api/user.status?handle=" + id;
        JSONObject data = null;
        Map<String, String> cookies = new HashMap<>();
        try {
            data = GetUrlJson.getHttpJson(url, cookies);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        data = data.getJSONObject("msg");
        // 获取所有提交结果
        JSONArray submissionData = data.getJSONArray("result");
        // 储存每个知识点过了多少题
        Map<String, Integer> knowledge = new HashMap<>();
        // 存储每道题的id,避免重复ac多次计算
        Set<String> problemIdSet = new HashSet<>();
        for (int i = 0; i < submissionData.size(); i++) {
            // 如果这道题过了
            if (submissionData.getJSONObject(i).getString("verdict").equals("OK")) {
                String contestId = submissionData.getJSONObject(i).getJSONObject("problem").getString("contestId");
                String problemId = contestId + submissionData.getJSONObject(i).getJSONObject("problem").getString("index");
                if (problemIdSet.contains(problemId)) continue;
                problemIdSet.add(problemId);
                // 获取该题所有tag
                JSONArray tags = submissionData.getJSONObject(i).getJSONObject("problem").getJSONArray("tags");
                for (int j = 0; j < tags.size(); j++) {
                    String tag = tags.getString(j);
                    if (!knowledge.containsKey(tag)) {
                        knowledge.put(tag, 1);
                    } else {
                        int number = knowledge.get(tag) + 1;
                        knowledge.put(tag, number);
                    }
                }
            }
        }
        return knowledge;
    }

    /**
     * 根据用户洛谷的id统计该用户通过题目的知识点情况
     *
     * @param id
     */
    public void getLuoguSubmission(String id) {
        int num = 0;
        Set<String> problemIdSet = new HashSet<>();
        List<String> a[] = new ArrayList[20];
        for (int i = 0; i < 20; i++) {
            List<String> list = new ArrayList<>();
            a[i] = list;
        }
        int minn = 10, maxn = 0;
        for (int page = 1; page <= 20; page++) {
            String url = "https://www.luogu.com.cn/record/list?user=" + id + "&status=12&page=" + page;
            JSONObject data = null;
            Map<String, String> cookies = new HashMap<>();
            cookies.put("x-luogu-type", "content-only");
            cookies.put("cookie", "__client_id=dcf0daf23bce048335213c9e6d5c9a42726c3a93; login_referer=https%3A%2F%2Fwww.luogu.com.cn%2Fuser%2F276450; _uid=276450");
            cookies.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.54");
            try {
                data = GetUrlJson.getHttpJson(url, cookies);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            data = data.getJSONObject("msg");
            data = data.getJSONObject("currentData");
            data = data.getJSONObject("records");
            JSONArray records = data.getJSONArray("result");

            for (int i = 0; i < records.size(); i++) {
                num++;
                String pid = records.getJSONObject(i).getJSONObject("problem").getString("pid");
                if (problemIdSet.contains(pid)) continue;
                problemIdSet.add(pid);
                int idx = records.getJSONObject(i).getJSONObject("problem").getInteger("difficulty");
                minn = Math.min(minn, idx);
                maxn = Math.max(maxn, idx);
                a[idx].add(pid);
//                System.out.print(pid);
//                System.out.println(records.getJSONObject(i).getJSONObject("problem").getString("title"));
            }
        }
        System.out.println(problemIdSet.size());
        for (int i = minn; i <= maxn; i++) {
            a[i].sort(Comparator.naturalOrder());
            System.out.println(i + ":" + a[i].size());
            for (int j = 0; j < a[i].size(); j++) {
                System.out.print(a[i].get(j) + " ");
            }
            System.out.println();
        }
    }

    /**
     * 根据队员牛客的id统计当前rating和历史最高rating
     *
     * @param id
     * @return
     */
    public Map<String, Integer> getNowcoderRating(String id) {
        String url = "https://ac.nowcoder.com/acm/contest/rating-history?token=&uid=" + id + "&_=1681560296783";
        JSONObject data = null;
        Map<String, String> cookies = new HashMap<>();
        cookies.put("cookie", "_uab_collina=164008151843874210609052; __snaker__id=NVh8bmBte8HlYvOj; NOWCODERUID=7655DF926F6E4133AA6FFFB120019158; NOWCODERCLINETID=8998D896DEAE7519A9B8DB042B773A82; gr_user_id=09f8cacb-965e-43d8-88c0-2c8c7805f5f2; c196c3667d214851b11233f5c17f99d5_gr_last_sent_cs1=859022021; YD00000586307807%3AWM_NI=hfm%2BS9v%2BgBVHoP14QrapVjt0C93mnZ66lvFVEpzYCedfVeyn6j4%2BNlzSAK8mxPpmrTHKqOEFo4pNSIUoK9OzygVElCSbMSF7qB4hdxfx8Vt4cDXZPlDMecqgyqrSbToqWHQ%3D; YD00000586307807%3AWM_NIKE=9ca17ae2e6ffcda170e2e6eeb8dc7cb4ea898ecf4abb8a8bb2d45e978b8facc86ea3948583e65fab9987b7c22af0fea7c3b92aacbaa3ccd233b6f5a9a3d544ad90ffd5d47aa3b09a8bf73c81e7fd8eed52f6eaa497c55e87b0b6bac26fa8ba9a86fc66f6b6fccccd68ba8d8facbb67bc90c08ef1608c95bfb0c84eadecc0afd24397b185dace72bbb99aa6ef4991be83b4c53db39efc89e774a6a8f8b7f740a19c98bbd3688893ab84f46dbbafbeb4e160adaf99a9d437e2a3; YD00000586307807%3AWM_TID=3Jp48kldYVpEVAEEEFJ%2FoM9iPY3K0Ow9; _clck=lvq5ys|1|fa1|0; isAgreementChecked=true; t=EBD44802565F6446BE8372887652140B; Hm_lvt_a808a1326b6c06c437de769d1b85b870=1680777722,1680854193,1681020415,1681521314; acw_tc=a8dbf6f742068156fb66b088fc455a51ee297d9b02fcf820458fd9cb2dd29494; c196c3667d214851b11233f5c17f99d5_gr_session_id=8b82af1f-7b07-45e3-901c-6cb2004edbef; c196c3667d214851b11233f5c17f99d5_gr_last_sent_sid_with_cs1=8b82af1f-7b07-45e3-901c-6cb2004edbef; c196c3667d214851b11233f5c17f99d5_gr_session_id_8b82af1f-7b07-45e3-901c-6cb2004edbef=true; Hm_lpvt_a808a1326b6c06c437de769d1b85b870=1681560460; c196c3667d214851b11233f5c17f99d5_gr_cs1=859022021");
        try {
            data = GetUrlJson.getHttpJson(url, cookies);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        data = data.getJSONObject("msg");
        JSONArray ratingList = data.getJSONArray("data");
        int maxn = 0, now = 0;
        for (int i = 0; i < ratingList.size(); i++) {
            int rating = ratingList.getJSONObject(i).getInteger("rating");
            maxn = Math.max(maxn, rating);
            now = rating;
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("history", maxn);
        map.put("current", now);
        return map;
    }

    /**
     * 根据队员codeforces的id统计当前rating和历史最高rating
     *
     * @param id
     * @return
     */
    public Map<String, Integer> getCfRating(String id) {
        String url = "https://codeforces.com/api/user.rating?handle=" + id;
        JSONObject data = null;
        Map<String, String> cookies = new HashMap<>();
        cookies.put("cookie", "lastOnlineTimeUpdaterInvocation=1684750619351; RCPC=a13d3b3eb51e1dd7351319891dd47e85; cf_clearance=1tZkaQ8xlK74htH..ZXT0cDc4KasHYxk0uZDJq5Z744-1681899994-0-250; __utmc=71512449; JSESSIONID=BA89611685C90E7902FF50DDD4FE961C-n1; 39ce7=CFRTkGJR; evercookie_png=bqnkpag5zed5okjp4s; evercookie_etag=bqnkpag5zed5okjp4s; evercookie_cache=bqnkpag5zed5okjp4s; 70a7c28f3de=bqnkpag5zed5okjp4s; X-User=; X-User-Sha1=6bd8dcb81b0b63bc355e224e6e71e0a5fcf213e1; lastOnlineTimeUpdaterInvocation=1684736999346; __utmz=71512449.1684741376.47.8.utmcsr=localhost:5173|utmccn=(referral)|utmcmd=referral|utmcct=/; __utma=71512449.1482994933.1679837284.1684741376.1684748672.48");
        cookies.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.50");
        try {
            data = GetUrlJson.getHttpJson(url, cookies);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (data == null) {
            Map<String, Integer> map = new HashMap<>();
            map.put("history", 0);
            map.put("current", 0);
            return map;
        }
        data = data.getJSONObject("msg");
        // 获取所有提交结果
        JSONArray ratingList = data.getJSONArray("result");
        int maxn = 0, now = 0;
        for (int i = 0; i < ratingList.size(); i++) {
            int rating = ratingList.getJSONObject(i).getInteger("newRating");
            maxn = Math.max(maxn, rating);
            now = rating;
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("history", maxn);
        map.put("current", now);
        return map;
    }

    /**
     * 根据队员atcoder的id统计当前rating和历史最高rating
     *
     * @param id
     * @return
     */
    public Map<String, Integer> getAtcoderRating(String id) {
        String url = "https://atcoder.jp/users/" + id + "/history/json";
        JSONObject data = null;
        Map<String, String> cookies = new HashMap<>();
        try {
            data = GetUrlJson.getHttpJson(url, cookies);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JSONArray ratingList = data.getJSONArray("msg");
        int maxn = 0, now = 0;
        for (int i = 0; i < ratingList.size(); i++) {
            int rating = ratingList.getJSONObject(i).getInteger("NewRating");
            maxn = Math.max(maxn, rating);
            now = rating;
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("history", maxn);
        map.put("current", now);
        return map;
    }

    /**
     * 定时组件，每天8点爬取队员每日过题数
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void updateUserAcNumber() {
        Calendar now = Calendar.getInstance();
        String year = String.valueOf(now.get(Calendar.YEAR));
        String mouth = String.valueOf(now.get(Calendar.MONTH) + 1);
        if (mouth.length() == 1) mouth = "0" + mouth;
        String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
        if (day.length() == 1) day = "0" + day;
        String time = year + "-" + mouth + "-" + day;
        Date date = new Date();
        System.out.println("当前时间:" + date);
        System.out.println("开始计算" + time + "的过题数");
        String url = "http://47.94.81.95:8081/rank/list?page=1&size=100&keyword=&date=" + time;
        JSONObject data = null;
        try {
            data = GetUrlJson.getHttpJson(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        data = data.getJSONObject("msg");
        data = data.getJSONObject("data");
        JSONArray records = data.getJSONArray("records");
        List<User> userList = userService.getAllUsers();
        for (int i = 0; i < records.size(); i++) {
            String username = records.getJSONObject(i).getString("username");
            if (userService.findUserByUsername(username) == null) continue;
            Acnumber acnumber = new Acnumber()
                    .setUsername(username)
                    .setTime(date);
            int atcoder = records.getJSONObject(i).getInteger("atcoder");
            acnumber = acnumber.setPlatform("atcoder").setCount(atcoder);
            acnumberService.insert(acnumber);

            int nowcoder = records.getJSONObject(i).getInteger("niuke");
            acnumber = acnumber.setPlatform("nowcoder").setCount(nowcoder);
            acnumberService.insert(acnumber);

            int cf = records.getJSONObject(i).getInteger("codeforces");
            acnumber = acnumber.setPlatform("codeforces").setCount(cf);
            acnumberService.insert(acnumber);

            int vjudge = records.getJSONObject(i).getInteger("vjudge");
            acnumber = acnumber.setPlatform("vjudge").setCount(vjudge);
            acnumberService.insert(acnumber);

            int luogu = records.getJSONObject(i).getInteger("luogu");
            acnumber = acnumber.setPlatform("luogu").setCount(luogu);
            acnumberService.insert(acnumber);

        }
        date = new Date();
        System.out.println("当前时间:" + date);
        System.out.println(time + "过题数计算已完成，已保存至数据库");
    }

    /**
     * 定时组件，每天2点爬取队员rating
     *
     * @throws InterruptedException
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateUserRating() throws InterruptedException {
        Date date = new Date();
        System.out.println("当前时间:" + date);
        System.out.println("开始爬取队员各大oj上的rating");
        List<Oj> ojList = ojService.getAllOj();
        for (Oj oj : ojList) {
            Thread.sleep(1000);
            String platform = oj.getPlatform();
            String username = oj.getUsername();
            String id = oj.getOjId();
            System.out.println(username + "   " + platform + "   " + id);
            if (platform.equals("nowcoder")) {
                Map<String, Integer> rating = getNowcoderRating(id);
                ojService.updateNowRating(username, platform, id, rating.get("current"));
                ojService.updateHistoryRating(username, platform, id, rating.get("history"));
            } else if (platform.equals("codeforces")) {
                Map<String, Integer> rating = getCfRating(id);
                ojService.updateNowRating(username, platform, id, rating.get("current"));
                ojService.updateHistoryRating(username, platform, id, rating.get("history"));
            } else if (platform.equals("atcoder")) {
                Map<String, Integer> rating = getAtcoderRating(id);
                ojService.updateNowRating(username, platform, id, rating.get("current"));
                ojService.updateHistoryRating(username, platform, id, rating.get("history"));
            }
            System.out.println("over");
        }
        date = new Date();
        System.out.println("当前时间:" + date);
        System.out.println("rating统计已完成");
    }


}
