package debug

import io.gatling.core.result.message.KO
import io.gatling.http.request.builder.HttpRequestWithParamsBuilder

import scala.collection.mutable

class RequestExtraInfo(val extraInfoMap: mutable.HashMap[String, String] = new mutable.HashMap[String, String]() with scala.collection.mutable.SynchronizedMap[String, String]) {

  implicit class ExtraInfoUtils(requestBuilder: HttpRequestWithParamsBuilder) {

    def saveFirstKoExtraInfo(): HttpRequestWithParamsBuilder = requestBuilder.extraInfoExtractor(
      extraInfo => {
        val key = s"${extraInfo.request.getUrl}${extraInfo.response.statusCode.getOrElse(0)}${extraInfo.requestName}"
        extraInfo.status == KO && extraInfoMap.contains(key).unary_! match {
          case true =>
            extraInfoMap.put(
              key,
              "<br><br>" + s"Request info:\n ${extraInfo.request.toString.replaceAll("\t", "\n ")}\n\n" +
                "<br><br>" + s"Response info:\n ${extraInfo.response.toString.replaceAll("\t", "\n ")}\n\n\n" +
                "<br><br>" + s"${extraInfo.session}"
            )
            List(key, extraInfoMap(key))
          case false => Nil
        }
      }
    )

  }

}