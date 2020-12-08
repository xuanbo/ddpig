package tk.fishfish.ddpig.controller

import org.apache.spark.sql.SparkSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PostMapping, RequestMapping, RestController}

/**
  * word-count
  *
  * @author 奔波儿灞
  * @since 1.0.0
  */
@RestController
@RequestMapping(Array("/v1/word-count"))
class WordCountController {

  @Autowired val sc: SparkSession = null

  val input = "hello world hello hello hello"

  @PostMapping
  def count: Array[(String, Int)] = {
    import sc.implicits._
    sc.createDataset(Seq(input)).rdd
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .top(10)
  }

}
