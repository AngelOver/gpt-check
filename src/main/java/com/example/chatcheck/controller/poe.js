// ==UserScript==
// @name         Poe 添加删除所有记录按钮127-203
// @grant    GM_xmlhttpRequest
// @connect  124.221.62.203
// @namespace    https://github.com/xxnuo/
// @version      0.1
// @license      MIT
// @description  给 Poe 网页版添加一个选中所有历史记录的功能，需要先选择 Delete 一句话，然后点击油猴图标里全选历史记录按钮。 124.221.62.203
// @author       xxnuo
// @match        https://poe.com/*
// @icon         https://www.google.com/s2/favicons?sz=64&domain=poe.com
// @grant        GM_registerMenuCommand
// ==/UserScript==

window.addEventListener('load', function() {
    var clientId = Math.floor(Math.random() * 10000);
    var  msgId =  '';






    // 每200毫秒汇报
    setInterval(function() {
        //汇报客户端是否运行
        //汇报生成的文本



        var url = 'http://124.221.62.203:9081/v1/chat/online2';
        var allText = document.querySelector('div[class^="InfiniteScroll_container"]').lastElementChild.childNodes[1].childNodes[1].innerText;

        var freeFlag = !document.querySelector('button[aria-label="stop message"]');
        GM_xmlhttpRequest({
            method: 'POST',
            url: url,
            headers: {
                'Content-Type': 'application/json',
            },
            data: JSON.stringify({
                freeFlag: freeFlag,
                clientId: clientId,
                text: allText
            }),
            onload: function(response) {
                // console.log(response);


            },
            onerror: function(error) {
                console.error('Error:', error);
            },
        });
    }, 200);
    // 获取任务
    setInterval(function() {
        var url = 'http://124.221.62.203:9081/v1/chat/send';
        var freeFlag = !document.querySelector('button[aria-label="stop message"]');
        GM_xmlhttpRequest({
            method: 'POST',
            url: url,
            headers: {
                'Content-Type': 'application/json',
            },
            data: JSON.stringify({
                freeFlag: freeFlag,
                clientId: clientId
            }),
            onload: function(response) {
                //console.log(response);
                var res =  JSON.parse(response.responseText);
                var textarea =   document.querySelector('textarea');
                var btn = document.querySelector('footer button').nextElementSibling.children[3];
                var clientMsg = "";
                if(res.code == 200){
                    textarea.parentNode.parentNode.parentNode.firstChild.click();
                    clientMsg = res.data.clientMsg;
                    textarea.parentElement.setAttribute("data-replicated-value",clientMsg);
                    textarea.value=clientMsg;
                    btn.removeAttribute('disabled');
                    btn.click();
                    console.log(clientMsg)
                }
            },
            onerror: function(error) {
                console.error('Error:', error);
            },
        });
    }, 500);  // 指定时间间隔为 1000 毫秒，即 1 秒

}, false);
