package com.cslg.graduation.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cslg.graduation.entity.Acnumber;
import com.cslg.graduation.entity.User;
import com.cslg.graduation.util.GetUrlJson;
import com.cslg.graduation.util.GraduationUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
                int userAcCount = nowRank.getJSONObject(j).getInteger("acceptedCount");
                int ranking = nowRank.getJSONObject(j).getInteger("ranking");
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
        cookies.put("Cookie", "REVEL_SESSION=eb5fd7b3f3f78632feff17e291e23eeed583a894-%00Rating%3A1513%00%00_TS%3A1696669167%00%00UserScreenName%3Acslg093119134%00%00csrf_token%3A3NacyQTQR%2BQSIlCHzwKfZ73PMsbQBhGa6MrkuiMQcWM%3D%00%00SessionKey%3A7b174f93ca8c5f5df748448db7b689f6bdfccd6e8765439f020542ce6b50802fd1f1aa117c6824-241a3d88027318a4d15f5ca45d0c6309b0e1f1e78592695e4e2e9301340e2ac2%00%00UserName%3Acslg093119134%00%00a%3Afalse%00%00w%3Afalse%00; Path=/; Expires=Sat, 07 Oct 2023 08:59:27 GMT; Max-Age=15552000; HttpOnly; Secure");
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

    public void getNowcoderRank(String id) {
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
        data = data.getJSONObject("data");
    }

    public void updateUserAcNumber() {
        for (int j = 1;j <= 18; j++) {
            String now = j + "";
            if (now.length() == 1) now = "0" + now;
            String time = "2023-04-" + now;

            Date date = new Date(2023 - 1900, 3, j);
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
                if(userService.findUserByUsername(username)==null) continue;
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
        }
    }
}
