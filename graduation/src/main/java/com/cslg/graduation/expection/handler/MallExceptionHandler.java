package com.cslg.graduation.expection.handler;

import com.cslg.graduation.common.ResponseService;
import com.cslg.graduation.expection.MallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import java.sql.SQLException;

@ControllerAdvice
public class MallExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(MallExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseService Handle(Exception e) {
        logger.error(e.getMessage());
        if (e instanceof MallException) {
            /**
             * 自定义错误
             */
            MallException mallException = (MallException) e;
            return ResponseService.createByCodeErrorMessage(mallException.getStatus(), mallException.getMessage());
        }
        if (e instanceof AccessDeniedException) {
            /**
             * 没有访问权限
             */
            AccessDeniedException accessDeniedException = (AccessDeniedException) e;
            return ResponseService.createByCodeErrorMessage(-1, "没有访问权限:" + accessDeniedException.getMessage());
        }
        if (e instanceof BindException) {
            /**
             * 头json请求，缺少参数
             */
            BindException bindException = (BindException) e;
            return ResponseService.createByCodeErrorMessage(-1, "缺少参数/参数错误:" + bindException.getMessage());
        }
        if (e instanceof ServletException) {
            /**
             *头表单请求，缺少参数
             */
            ServletException servletException = (ServletException) e;
            return ResponseService.createByCodeErrorMessage(-1, "缺少参数:" + servletException.getMessage());
        }
        if (e instanceof NullPointerException) {
            /**
             *头表单请求，参数
             */
            NullPointerException nullPointerException = (NullPointerException) e;
            return ResponseService.createByCodeErrorMessage(-1, "参数错误:" + nullPointerException.getMessage());
        }
        if (e instanceof DataAccessException) {
            /**
             * 主键冲突
             */
            DataAccessException dataAccessException = (DataAccessException) e;
            return ResponseService.createByCodeErrorMessage(-1, "数据库错误操作" + dataAccessException.getMessage());
        }
        if (e instanceof SQLException) {
            /**
             * 数据库交互错误
             */
            SQLException sqlException = (SQLException) e;
            return ResponseService.createByCodeErrorMessage(-1, sqlException.getMessage());
        }
        if (e instanceof HttpMessageNotReadableException) {
            /**
             * json格式错误
             */
            HttpMessageNotReadableException exception = (HttpMessageNotReadableException) e;
            return ResponseService.createByCodeErrorMessage(-1, "json格式错误");
        }

        if (e instanceof MethodArgumentNotValidException) {
            /**
             * json缺少参数错误
             */
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            return ResponseService.createByCodeErrorMessage(-1, "缺少参数:" + e.getMessage());
        } else {
            /**
             * 全局错误
             */
            return ResponseService.createByCodeErrorMessage(-1,"全局异常:"+e.getMessage());
        }
    }
}
