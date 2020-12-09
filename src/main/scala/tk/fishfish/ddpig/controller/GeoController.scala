package tk.fishfish.ddpig.controller

import org.apache.spark.sql.SparkSession
import org.datasyslab.geospark.formatMapper.WktReader
import org.elasticsearch.spark.rdd.EsSpark
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}

/**
  * Geo计算
  *
  * @author 奔波儿灞
  * @since 1.0.0
  */
@RestController
@RequestMapping(Array("/v1/geo"))
class GeoController {

  @Autowired val sc: SparkSession = null

  /**
    * 返回空间字段
    *
    * @return Array[String]
    */
  @GetMapping(Array("/list"))
  def list(): Array[String] = {
    EsSpark.esRDD(sc.sparkContext, "bounds_x_0")
      .map(_._2)
      .map { e =>
        // the_geom 为空间字段, WKT/WKB 格式
        // POINT (-88.331492 32.324142)
        val geom = e.get("the_geom").map(_.asInstanceOf[Array[Byte]]).map(new String(_)).orNull
        geom
      }.collect()
  }

  /**
    * 读取空间字段，利用GeoSpark保存为GeoJSON
    */
  @GetMapping(Array("/save-as-geo-json"))
  def saveAsGeoJSON(): Unit = {
    val javaRDD = EsSpark.esRDD(sc.sparkContext, "bounds_x_0")
      .map(_._2)
      .map { e =>
        val geom = e.get("the_geom").map(_.asInstanceOf[Array[Byte]]).map(new String(_)).orNull
        geom
      }.toJavaRDD()
    val spatialRDD = WktReader.readToGeometryRDD(javaRDD, 0, true, false)
    spatialRDD.saveAsGeoJSON("bounds_x_0")
  }

}
