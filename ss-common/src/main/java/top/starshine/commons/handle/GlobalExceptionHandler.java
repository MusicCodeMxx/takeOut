package top.starshine.commons.handle;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.starshine.commons.exception.*;
import top.starshine.commons.status.BasisStatus;
import top.starshine.commons.status.Result;

import javax.servlet.ServletException;
import java.util.stream.Collectors;

/**
 * 全局结果处理 - 全局响应处理程序
 * @author: starshine
 * @version: 1.0
 * @since: 2022/6/22  下午 5:29  周三
 * @Description:
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================== 自定义异常 star ==================//

    /** 指定异常处理-返回指定 200 响应, 200 (OK/正常) */
    @ExceptionHandler(SuccessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> successExceptionHandler(SuccessException e){
        this.handleExceptionPrintln(e);
        return new Result<>(false,e.getStatus(), e.getMessage());
    }

    /** 返回指定 400 HTTP 响应, 400 (Bad Request/错误请求) */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> badRequestExceptionHandler(BadRequestException e){
        this.handleExceptionPrintln(e);
        return new Result<>(false,e.getStatus(), e.getMessage());
    }

    /** 返回指定 401 HTTP 响应, 401 (Unauthorized/未授权) */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> unauthorizedExceptionHandler(UnauthorizedException e){
        this.handleExceptionPrintln(e);
        return new Result<>(false,e.getStatus(), e.getMessage());
    }

    /** 返回指定 403 HTTP 响应, 403 (Forbidden/禁止) */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<?> forbiddenExceptionHandler(ForbiddenException e){
        this.handleExceptionPrintln(e);
        return new Result<>(false,e.getStatus(), e.getMessage());
    }

    /** 返回指定 404 HTTP 响应, 404 (Not Found/未找到) */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<?> notFoundExceptionHandler(NotFoundException e){
        this.handleExceptionPrintln(e);
        return new Result<>(false,e.getStatus(), e.getMessage());
    }

    /** 返回指定 500 HTTP 响应, 500 (Internal Server Error/内部服务器错误) */
    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> internalServerErrorException(InternalServerErrorException e){
        this.handleExceptionPrintln(e);
        return new Result<>(false,e.getStatus(), e.getMessage());
    }

    /** 指定异常处理 */
    @ExceptionHandler(BussinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> bussinessExceptionHandler(BussinessException e){
        this.handleExceptionPrintln(e);
        return new Result<>(false,e.getStatus(), e.getMessage());
    }

    // ================== 自定义异常 end ==================//

    /** 默认异常处理 */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> exceptionHandler(Exception e){
        this.handleExceptionPrintln(e);
        return new Result<>(false, BasisStatus.ERROR,e.getMessage());
    }

    /**  MVC 注入异常 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        this.handleExceptionPrintln(e);
        return new Result<>(
                false,
                BasisStatus.REQUEST_PARAMETER_CANNOT_BE_EMPTY.getCode(),
                BasisStatus.REQUEST_PARAMETER_CANNOT_BE_EMPTY.getMessage()
        );
    }

    /**  在接口 Validated 值注入 Bean 发生的错误捕获 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        this.handleExceptionPrintln(e);
        return new Result<>(false, BasisStatus.BAO_REQUEST.getCode(), forErrorGetValue(e.getBindingResult()));
    }

    /** 注入 Bean 类型错误 */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> bindExceptionHandler(BindException e){
        this.handleExceptionPrintln(e);
        String errorValue = forErrorGetValue(e.getBindingResult());
        if (StringUtils.hasText(errorValue)) return new Result<>(false, BasisStatus.BAO_REQUEST.getCode(), errorValue);
        errorValue = null;
        e = null;
        return new Result<>(false, BasisStatus.BAO_REQUEST, "非法传入");
    }

    /** 指定异常处理 */
    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> servletExceptionHandler(ServletException e){
        this.handleExceptionPrintln(e);
        return new Result<>(false, BasisStatus.ERROR,e.getMessage());
    }

    /** 循环抛异常的值 */
    private String forErrorGetValue(BindingResult result){
        return result.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
    }

    private  void handleExceptionPrintln(Throwable e){
        e.printStackTrace();
    }

}
