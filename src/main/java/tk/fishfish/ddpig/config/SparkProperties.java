package tk.fishfish.ddpig.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Map;

;

/**
 * spark属性
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Data
@Validated
@ConfigurationProperties("egova.spark")
class SparkProperties {

    @NotBlank
    private String appName;

    @NotBlank
    private String master;

    private Map<String, String> props;

}
