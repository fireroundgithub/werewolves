package proc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import log.LogSetting;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by wenc on 2017/4/28.
 * 分配角色阶段，更新所有HttpSession的role属性，计时10s
 */
public class AssignProc extends Proc{

    private Logger logger = LogSetting.loadSetting("分配角色阶段");

    /** 当前阶段的持续时间 */
    private long time=10000;

    /** 可用角色列表 */
    private String[] roles={
            "seer", "witch", "guard", "hunter", "villager", "villager",
            "villager", "villager", "wolf", "wolf", "wolf", "wolf"
    };

    /**
     * 随机分配角色并把结果分发到对应客户端，10s后进入下一阶段
     */
    @Override
    public void run() {
        Set<HttpSession> set = GameProc.map2.keySet();
        randomRoles(set);
        Map<String, HttpSession> map3 = list2map(set2List(set));
        appendAttribute(map3);
        for (Session s : GameProc.map1.keySet()) {
            String role = (String) GameProc.map1.get(s).getAttribute("role");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state", "start");
            jsonObject.put("identify", role);
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(1);
            jsonArray.add(2);
            jsonArray.add(3);
            jsonArray.add(4);
            jsonArray.add(5);
            jsonArray.add(6);
            jsonArray.add(7);
            jsonArray.add(8);
            jsonArray.add(9);
            jsonArray.add(10);
            jsonArray.add(11);
            jsonArray.add(12);
            jsonObject.put("searArr", jsonArray);
            jsonObject.put("mySeat", GameProc.map1.get(s).getAttribute("num"));
            String msg = jsonObject.toJSONString();
            System.out.println(msg);
            try {
                s.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                logger.severe("分发角色到客户端时发生IO异常");
            }
        }
        GameProc.timer.schedule(new NightProc(), time);

    }

    /**
     * 随机为12位玩家分配角色
     * @param set
     */
    private void randomRoles(Set<HttpSession> set){
        List<HttpSession> list = set2List(set);
        int[] vars=getSequence(12);
        int counter=0;
        for (int var : vars) {
            list.get(var).setAttribute("role", roles[counter++]);
        }
    }

    /**
     * 给定no范围内的自然数，随机打乱其顺序
     * @param no
     * @return
     */
    private int[] getSequence(int no) {
        int[] sequence = new int[no];
        for(int i = 0; i < no; i++){
            sequence[i] = i;
        }
        Random random = new Random();
        for(int i = 0; i < no; i++){
            int p = random.nextInt(no);
            int tmp = sequence[i];
            sequence[i] = sequence[p];
            sequence[p] = tmp;
        }

        return sequence;
    }

    /**
     * 角色分配好后需要为有功能的玩家追加session的attribute
     * 预言家需要一个checkList属性，女巫需要一个antidote和poison属性和saveNum和poisonNum，
     * 守卫需要一个guardList属性，猎人需要一个shutNum，狼人需要一个killNum,同时共享一个狼队名单wolfList
     * 这里详细说下普村的属性：玩家编号num（1-12）、isReady、isDead、role、voteList、isSpeaker
     * @param map
     */
    private void appendAttribute(Map<String, HttpSession> map){
        HttpSession s;
        int num=0;
        for(String str : map.keySet()){
            switch (str){
                case "seer":
                    s=map.get("seer");
                    s.setAttribute("checkList", new HashMap<Integer, String>());
                    map.put("seer", s);
                    break;
                case "witch":
                    s = map.get("witch");
                    s.setAttribute("antidote", true); // 设置为true表明女巫解药还在
                    s.setAttribute("poison", true);
                    s.setAttribute("saveNum", num);
                    s.setAttribute("poisonNum", num);
                    map.put("witch", s);
                    break;
                case "guard":
                    s = map.get("guard");
                    s.setAttribute("guardList", new ArrayList<Integer>());
                    map.put("guard", s);
                    break;
                case "hunter":
                    s = map.get("hunter");
                    s.setAttribute("shutNum", num);
                    map.put("hunter", s);
                    break;
                case "wolf":
                    ArrayList<Integer> wolfList = new ArrayList();
                    s = map.get("wolf");
                    s.setAttribute("wolfList", wolfList);
                    s.setAttribute("killNum", num);
                    map.put("wolf", s);
                    break;
                case "villager":
                    break;
                default:
                    logger.severe( "会话map中key值存在非法值");
                    break;
            }
        }
    }

    /**
     * 把role作为key，便于根据role来查找相应信息
     * @param list
     * @return
     */
    private Map<String, HttpSession> list2map(List<HttpSession> list) {
        Map<String, HttpSession> map = new HashMap<>();
        for (HttpSession s : list) {
            map.put((String) s.getAttribute("role"), s);
        }
        return map;
    }

    private List set2List(Set<HttpSession> set) {
        List res = new ArrayList();
        for (HttpSession s : set)
            res.add(s);
        return res;
    }
}
