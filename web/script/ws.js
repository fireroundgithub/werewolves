/**
 * Created by wenc on 2017/4/28.
 * 用于websocket连接
 */
var ws;

/**
 * 当点击准备按钮时触发此函数，建立websocket连接
 */
function ready(){
    ws=new WebSocket("ws://localhost:8080/werewolves/portal"); // 建立连接
    console.log(ws.readyState)
    //当连接打开时，向服务器发送ready状态
    ws.onopen = function (e) {
        console.log("ws open")
        ws.send("ready");
    };
    ws.onmmessage=function () {
        alert("mes");
    }

}

