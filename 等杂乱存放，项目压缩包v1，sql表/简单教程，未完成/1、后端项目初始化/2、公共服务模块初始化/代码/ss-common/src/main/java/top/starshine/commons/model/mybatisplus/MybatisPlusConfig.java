package top.starshine.commons.model.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus 配置类
 * @author: starshine
 * @version: 1.0
 * @since: 2022/6/22  下午 6:12  周三
 * @Description:
 */
@Slf4j
@Configuration
public class MybatisPlusConfig {

    /**
     * 相当于顶部的：@MapperScan
     * { @code @MapperScan("com.baomidou.springboot.mapper*") }
     * 这里可以扩展，比如使用配置文件来配置扫描 Mapper 的路径
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        scannerConfigurer.setBasePackage("top.starshine.mapper*");
        return scannerConfigurer;
    }

    /** 3.4.0之后版本 - 配置分页 */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

}
