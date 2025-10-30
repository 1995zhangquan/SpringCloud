package com.cloud.nacosserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;


/**
 * nacos配置中心
 * 1.pom里添加：spring-cloud-starter-bootstrap
 * 2.dataid命名别忘了带上.yaml后缀
 * 3.bootsstrap.yml优先于application.yml
 * ${spring.application.name}-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}）
 * nacos 集群
 * 1.nacos startup.com 修改单机-集群
 * 2.复制cluster.conf.example为cluster.conf，修改里面的集群端口号
 * 3.复制nacos文件夹，分别修改应对的application.properties的8848
 * nacos nacos2.x版本会默认占用四个端口，【尽量让端口差距大一些】，【控制台端口也要改】，同时修改对应的【cluster.conf】
 *      8828 对应浏览器可视化界面 8822
 *      8848            8842
 *      8868            8862
 */

//开启hystrix支持
@EnableHystrix
//实现服务注册
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.cloud"},exclude = SecurityAutoConfiguration.class)
//@SpringBootApplication(scanBasePackages = {"com.cloud"})
public class NacosServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosServerApplication.class, args);
    }

}
