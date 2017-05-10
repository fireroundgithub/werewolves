package entity;

/**
 * Created by wenc on 2017/5/3.
 * 这是一个实体类，用于存储夜里的信息
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 角色分配好后需要为有功能的玩家追加session的attribute
 * 预言家需要一个checkList属性，女巫需要一个antidote和poison属性和saveNum和poisonNum，
 * 守卫需要一个guardList属性，猎人需要一个shutNum，狼人需要一个killNum,同时共享一个狼队名单wolfList
 * 这里详细说下普村的属性：玩家编号num（1-12）、isReady、isDead、role、voteList、isSpeaker
 * @param map
 */
public class NightInfo {
    // 存储预言家验人信息
    public Map<Integer, String> checkRes=new LinkedHashMap<>();

    // 存储女巫相关动作信息
    public boolean antidote;
    public boolean poison;
    public  int saveNum;
    public  int poisonNum;

    // 存储守卫守人信息
    public  int guardNum;

    // 存储狼人杀人信息
    public  int killNum;

    public NightInfo(Map<Integer, String> checkRes, Boolean antidote, Boolean poison, Integer saveNum, Integer poisonNum, Integer guardNum, Integer killNum) {
        this.checkRes = checkRes;
        this.antidote = antidote;
        this.poison = poison;
        this.saveNum = saveNum;
        this.poisonNum = poisonNum;
        this.guardNum = guardNum;
        this.killNum = killNum;
    }

    public NightInfo(){

    }
}
