package top.starshine.commons.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.starshine.commons.exception.InternalServerErrorException;

import java.util.Objects;


/**
 * 响应状态和结果封装类
 * <li>status: true正常响应, false不正常响应  </li>
 * <li>code: code </li>
 * <li>message: 消息  </li>
 * <li>data: 数据体  </li>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/2/7  下午 7:56  周一
 * @Description: 响应状态和结果封装类
*  data 聚合注解
 * 包含 @Setter、@Getter、@RequiredArgsConstructor、@ToString、@EqualsAndHashCode
 * JsonInclude(JsonInclude.Include.NON_NULL) 当字段的参数为 null 不进行序列化
 * 该注解的参考博客地址: https://www.cnblogs.com/-xuzhankun/p/8034179.html
 */
public class Result<T> implements java.io.Serializable {

    /** true 正常响应, false不正常响应 */
    private boolean status;

    /** 状态码 */
    private int code;

    /** 消息 */
    private String message;

    /** 数据体 */
    private T data;

    public Result(boolean status, Integer code, String message,T data) {
        if ( null == code ) throw new InternalServerErrorException(BasisStatus.RESULT_VALUE_ERROR);
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Result(boolean status, Integer code, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    /** 状态枚举 - 泛型 Data 数据体 */
    public Result(boolean status, R r, T data) {
        this(status,r.getCode(),r.getMessage(),data);
    }

    /** 状态枚举 - 泛型 Data 数据体 */
    public Result(R r, T data) {
        this(true,r,data);
    }

    /** BasisStatus.SUCCESS - 泛型 Data 数据体 */
    public Result(T data) {
        this(true, BasisStatus.SUCCESS, data);
    }

    public Result(){}

    //public static <K> Result<String> successConverterJSON(K data){}

    /** 状态码: 200 - 消息 操作成功  */
    public static Result<Object>  success(){
        return new Result<Object>(true, BasisStatus.SUCCESS.getCode(), BasisStatus.SUCCESS.getMessage());
    }

    /** 状态码: 9999 - 消息: 服务姬崩溃了  */
    public static Result<Object> error(R r){
        return new Result<Object>(false,r.getCode(), r.getMessage());
    }

    /** 状态码: 9999 - 消息: 服务姬崩溃了  */
    public static Result<Object> error(){
        return new Result<Object>(false, BasisStatus.ERROR.getCode(), BasisStatus.ERROR.getMessage());
    }

    /** true 正常响应, false不正常响应 */
    public boolean isStatus() {
        return status;
    }

    /**检查状态是否正常返回,否则就抛异常*/
    public void isStatusAndIsThrow() {
        if (!status) throw new InternalServerErrorException(500,"远程调用失败");
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() { return data; }

    public <K> K getData(Class<K> clazz) {
        if (null ==  data) return null;
        return new ObjectMapper().convertValue(data,clazz);
    }

    public <K> K getData(TypeReference<K> toValueTypeRef) {
        if (null ==  data) return null;
        return new ObjectMapper().convertValue(data,toValueTypeRef);
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return status == result.status && code == result.code && Objects.equals(message, result.message) && Objects.equals(data, result.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, code, message, data);
    }

    @Override
    public String toString() {
        if (null == data) return "{\"status\":" + status + ",\"code\":" + code + ",\"message\":\"" + message + "\"}";
        return "{\"status\":" + status + ",\"code\":" + code + ",\"message\":\"" + message + "\",\"data\":\"" + data + "\"}";
    }

}

