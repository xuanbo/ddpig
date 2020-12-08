package tk.fishfish.ddpig.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Spark配置
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SparkProperties.class)
public class SparkConfiguration {

    private final SparkProperties properties;

    @Bean(destroyMethod = "stop")
    public SparkSession sparkSession() {
        SparkConf sparkConf = new SparkConf()
                .setAppName(properties.getAppName())
                .setMaster(properties.getMaster());
        Map<String, String> props = Optional.ofNullable(properties.getProps())
                .orElse(Collections.emptyMap());
        props.forEach(sparkConf::set);

        JavaSparkContext context = new JavaSparkContext(sparkConf);

        return SparkSession.builder()
                .sparkContext(context.sc())
                .appName(properties.getAppName())
                .getOrCreate();
    }

}
