package com.cslg.graduation.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cslg.graduation.util.GetUrlJson;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther xurou
 * @date 2023/3/31
 */

@Service
public class SpiderService {

    public double getNowcoderScore(String username, String id) {
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
                if (nowRank.getJSONObject(j).getString("uid").equals(username)) {
                    int userAcCount = nowRank.getJSONObject(j).getInteger("acceptedCount");
                    int ranking = nowRank.getJSONObject(j).getInteger("ranking");
                    return 100.0 * (userAcCount * 1.0 / acceptedFirstCount) * (2 * peopleCount - 2) / (peopleCount + ranking - 2);
                }
            }

        }


        return 0.0;
    }

    public double getAtcoderScore(String username, String id) {
        String url = "https://atcoder.jp/contests/" + id + "/standings/json";
        JSONObject data = null;
        Map<String, String> cookies = new HashMap<>();
        cookies.put("Cookie", "REVEL_SESSION=6aa9af3fbca23c8816be033df1f704a2d24f34ed-%00UserScreenName%3Acslg093119134%00%00csrf_token%3A3NacyQTQR%2BQSIlCHzwKfZ73PMsbQBhGa6MrkuiMQcWM%3D%00%00Rating%3A1513%00%00a%3Afalse%00%00w%3Afalse%00%00SessionKey%3A76d0acf2c8127b52b15781605138525494df537689867ba513512e6dd9993d94a665f95e280b34-d0c1a7cc00d335d770e17741f002a8403714c4bd79ee6833185f975d548dd999%00%00_TS%3A1680704534%00%00UserName%3Acslg093119134%00; Path=/; Expires=Wed, 05 Apr 2023 14:22:14 GMT; Max-Age=15552000; HttpOnly; Secure");
        try {
            data = GetUrlJson.getHttpJson(url, cookies);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        data = data.getJSONObject("msg");
        JSONArray standingsData = data.getJSONArray("StandingsData");
        int acceptedFirstCount = standingsData.getJSONObject(0).getJSONObject("TotalResult").getInteger("Accepted");
        int peopleCount = standingsData.size();
        for (int j = 0; j < standingsData.size(); j++) {
            if (standingsData.getJSONObject(j).getString("UserName").equals(username) ||
                    standingsData.getJSONObject(j).getString("UserScreenName").equals(username)) {
                int userAcCount = standingsData.getJSONObject(j).getJSONObject("TotalResult").getInteger("Accepted");
                int ranking = standingsData.getJSONObject(j).getInteger("Rank");
                double score = 100.0 * (userAcCount * 1.0 / acceptedFirstCount) * (2 * peopleCount - 2) / (
                        peopleCount + ranking - 2);
                return score;

            }
        }
        return 0.0;

    }
}
