/**
 * Created by wenc on 2017/4/27.
 */

var ctrl = document.getElementById("ctrl"); //控制按钮对象
var timer = document.getElementById("timer"); //时间显示对象
var hour, minute, second; //时，分 ,钞
var t; //setTimeout方法
//初始化显示和按钮
var init = function(){
    timer.innerHTML = "00:00:00"; //由于FF不支持使用innerText，故采用innerHTML
    hour = minute = second = 0; //初始化显示
    ctrl.setAttribute("value", "开始"); //初始化控制按钮文字
    ctrl.setAttribute("onclick", "startit()"); //初始化控制按钮事件
    clearTimeout(t);
}
//开始计时
function startit(){
    t = setTimeout("startit()", 1000); //每隔1秒（1000毫秒）递归调用一次
    second++;
    if(second>=60){ //判断秒是否到60, 是则进位
        second = 0;
        minute++;
    }
    if(minute>=60){ //判断分是否到60, 是则进位
        minute = 0;
        hour++;
    }
    timer.innerHTML = j(hour) + ":" + j(minute) + ":" + j(second) ; //更新显示
//更改按钮状态
    ctrl.setAttribute("value", "暂停/停止"); //更改按钮文字
    ctrl.setAttribute("onclick", "pause()"); //更改按钮触发事件
}
//显示数字填补，即当显示的值为0-9时，在前面填补0;如：1:0:4, 则填补成为 01:00:04
var j = function(arg){
    return arg>=10 ? arg : "0" + arg;
}
//暂停计时
var pause = function(){
    clearTimeout(t);
    ctrl.setAttribute("onclick", "startit()");
    ctrl.setAttribute("value", "继续");
}


/*
进入任何一个阶段都会触发此方法
arg0是此阶段的最长时间计时，由服务器指定；
arg1是此阶段名称，由服务器指定
 */
function init(arg0, arg1){

}