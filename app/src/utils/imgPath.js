

const HOST = '/front';

function imgPath(path){
    return '/common/download?name=' + path
}


/// http://10.10.10.55:9000/reggie/static/item/00874a5e-0df2-446b-8f69-a30eb7d88ee8.png

//将url传参转换为数组
function parseUrl(url) {

    // 找到url中的第一个?号
    var parse = url.substring(url.indexOf("?") + 1),
        params = parse.split("&"),
        len = params.length,
        item = [],
        param = {};

    for (var i = 0; i < len; i++) {
        item = params[i].split("=");
        param[item[0]] = item[1];
    }

    return param;
}

