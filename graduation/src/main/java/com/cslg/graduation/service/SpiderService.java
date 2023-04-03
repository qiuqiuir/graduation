package com.cslg.graduation.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cslg.graduation.util.GetUrlJson;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @auther xurou
 * @date 2023/3/31
 */

@Service
public class SpiderService {

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

    public Map<String, Double> getAtcoderScore(String id) {
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

    public Map<String,Integer> getCfSubmission(String id) {
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
        Map<String,Integer> knowledge = new HashMap<>();
        // 存储每道题的id,避免重复ac多次计算
        Set<String> problemIdSet = new HashSet<>();
        for(int i=0;i<submissionData.size();i++){
            // 如果这道题过了
            if(submissionData.getJSONObject(i).getString("verdict").equals("OK")){
                String contestId = submissionData.getJSONObject(i).getJSONObject("problem").getString("contestId");
                String problemId = contestId + submissionData.getJSONObject(i).getJSONObject("problem").getString("index");
                if(problemIdSet.contains(problemId)) continue;
                problemIdSet.add(problemId);
                // 获取该题所有tag
                JSONArray tags = submissionData.getJSONObject(i).getJSONObject("problem").getJSONArray("tags");
                for(int j=0;j<tags.size();j++){
                    String tag = tags.getString(j);
                    if(!knowledge.containsKey(tag)){
                        knowledge.put(tag,1);
                    }else{
                        int number = knowledge.get(tag)+1;
                        knowledge.put(tag,number);
                    }
                }
            }
        }
        return knowledge;
    }

    public void getLuoguSubmission(String id){
        int num = 0;
        Set<String> problemIdSet = new HashSet<>();
        List<String> a[] = new ArrayList[20];
        for(int i=0;i<20;i++){
            List<String> list = new ArrayList<>();
            a[i]=list;
        }
        int minn=10,maxn=0;
        for(int page=1;page<=20;page++) {
            String url = "https://www.luogu.com.cn/record/list?user=" + id + "&status=12&page="+page;
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
                if(problemIdSet.contains(pid)) continue;
                problemIdSet.add(pid);
                int idx=records.getJSONObject(i).getJSONObject("problem").getInteger("difficulty");
                minn=Math.min(minn,idx);
                maxn=Math.max(maxn,idx);
                a[idx].add(pid);
//                System.out.print(pid);
//                System.out.println(records.getJSONObject(i).getJSONObject("problem").getString("title"));
            }
        }
        System.out.println(problemIdSet.size());
        for(int i=minn;i<=maxn;i++){
            a[i].sort(Comparator.naturalOrder());
            System.out.println(i+":"+a[i].size());
            for(int j=0;j<a[i].size();j++){
                System.out.print(a[i].get(j)+" ");
            }
            System.out.println();
        }
    }
}
