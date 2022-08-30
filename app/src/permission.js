import router from './router'
import localStorage from "./utils/localStorage";
import system from "./common/system";

/** 没有重定向白名单 - 允许未登录情况下直接访问 */
const whiteList = ["/","/home","/user/agreement", "/403", "/404"]

/** 在每个路由请求之前执行 - 导航守卫 */
router.beforeEach(async(to, from, next) => {
  /** 获取 Cookie 中存储的 Token */
  const hashToken = localStorage.get(system.TOKEN_KEY);
  const { path } = to;
  /** 检查当前是否有 Token */
  if (hashToken){
    /** 检查是不是去登陆页面,已登录的用户跳转到首页  */
    if (path === '/login'){ next({ path: '/'}); }
    /** 没有匹配规则放行 */
    next();

  }else{
    if (path === '/login' ){ next(); return true; }
    /** 检查是不是白名单路径 */
    if ((whiteList.filter(item => item === path)).length === 0){
      /** 无权访问,重定向到登录页面 */
      next("/login" )
    }
    next();
  }
  return false;
})
