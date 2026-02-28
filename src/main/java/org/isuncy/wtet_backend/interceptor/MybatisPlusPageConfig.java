package org.isuncy.wtet_backend.interceptor;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusPageConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);

         paginationInnerInterceptor.setOverflow(true); // 页数溢出时是否自动处理（如查询第100页，但总共只有10页，是否自动回落到最后一页），默认false
         paginationInnerInterceptor.setMaxLimit(500L); // 单页最大条数限制，默认无限制


        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        return interceptor;
    }
}