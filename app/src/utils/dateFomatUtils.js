const dayMs = 86400000;

export default {

	/**
	 * 通用日期加减
	 * @author ckk
	 * @param dateStr
	 * @param type 加减数的类型，d是天、h是小时...
	 * @param num 正数加，负数减。ps：支持非整数
	 * @returns {Date}
	 */
	addDay(dateStr, type, num) {
		var m = dayMs; // 默认是天
		if (type == 'h') {
			m = m / 24;
		} else if (type == 'm') {
			m = m / 24 / 60;
		} else if (type == 's') {
			m = m / 24 / 60 / 60;
		} else if (type == 'ms') {
			m = m / 24 / 60 / 60 / 1000;
		} else {
			// type == 'd'
			// m = m;
		}
		return new Date(Date.parse(dateStr) + (m * num));
	},

	/**
	 * 通用日期格式化
	 * 
	 * @author ckk
	 * @param date
	 * @param fmt 如："yyyyMMdd"、"yyyy/MM/dd"、"yyyy-MM-dd hh:mm:ss"、"yyyy年MM月dd日"、"yyyy-MM-dd"等等
	 * @returns {*}
	 */
	dateFormat(date, fmt) {
		// 检查是不是时间戳
		if(typeof date === 'number'){
			date = new Date(date);
		}
		var obj = {
			"M+": date.getMonth() + 1, // 月
			"d+": date.getDate(), // 日
			"h+": date.getHours(), // 时
			"m+": date.getMinutes(), // 分
			"s+": date.getSeconds(), // 秒
			"q+": Math.floor((date.getMonth() + 3) / 3), // 季度
			"S": date.getMilliseconds() // 毫秒
		};
		if (/(y+)/.test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
		}
		for (var i in obj) {
			if (new RegExp("(" + i + ")").test(fmt)) {
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (obj[i]) : (("00" + obj[i]).substr(("" + obj[i])
					.length)));
			}
		}
		return fmt;
	},

	/** 获取指定几天前的时间对象 */
	getDayOfDateObject(day) {
		let dateObject = new Date();
		dateObject.setTime(dateObject.getTime() - ( dayMs * day));
		return dateObject;
	},

	/** 获取指定几天前的时间 */
	formatDateOfDay(day, fmt){
		return this.dateFormat(this.getDayOfDateObject(day),fmt);
	},

	/** 获取昨天的时间 */
	// https://blog.csdn.net/lannieZ/article/details/104537176
	getYesterDay(fmt) {
		let day = new Date();
		day.setTime(day.getTime() - dayMs );
		return this.dateFormat(day, fmt);
	},

	/** 今天时间 */
	getDay(fmt) {
		return this.dateFormat(new Date(), fmt);
	},

	/**
	 * 时间戳转自定义日期格式化
	 * @author ckk
	 * @param 时间戳
	 * @param fmt 如："yyyyMMdd"、"yyyy/MM/dd"、"yyyy-MM-dd hh:mm:ss"、"yyyy年MM月dd日"、"yyyy-MM-dd"等等
	 * @returns {*}
	 */
	timeNumberToDateFormat(tiemNumber, fmt) {
		return dateFormat(new Date(tiemNumber), fmt);
	},

	/**
	 * 时间戳转 "yyyy-MM-dd" 日期格式化
	 * 
	 * @author starshine
	 * @param 时间戳 转 "yyyy-MM-dd"
	 * @returns {*}
	 */
	fmtDate(str) {
		let date = new Date(str);
		// let date1 =  new Date('19-5-13 ');
		// console.log(date1=='Invalid Date');
		// if()
		let y = 1900 + date.getYear();
		let m = "0" + (date.getMonth() + 1);
		let d = "0" + date.getDate();
		return y + "-" + m.substring(m.length - 2, m.length) + "-" + d.substring(d.length - 2, d.length);
	},


	/**
	 * 计算距离指定时间剩余多少
	 * @param dateTime 原始时间
	 * @returns {number} 返回剩余分钟
	 */
	distanceTime(dateTime){
		let distance;
		// 当前时间
		let date1  = new Date();
		// 原始时间 + 29 分钟
		let date2 = new Date(dateTime + 1740000);
		//计算两个日期之间相差的毫秒数
		distance = date2 - date1;
		console.log("1=>>>>>",distance)
		if ( 0 > distance){
			return 0;
		}
		//取绝对值
		distance = Math.abs(distance);
		//毫秒数除以一天的毫秒数,就的到了天数
		distance = Math.floor(distance / ( 60 * 1000));
		console.log("2=>>>>>",distance)
		return distance
	}
	
}
