package debug

import io.gatling.core.result.message.KO
import io.gatling.http.request.builder.HttpRequestBuilder
import scala.collection.mutable

class RequestExtraInfo(val extraInfoMap: mutable.HashMap[String, String] = new mutable.HashMap[String, String]()) {

  implicit class Util(requestBuilder: HttpRequestBuilder) {

    def saveFirstKoExtraInfo(): HttpRequestBuilder = requestBuilder.extraInfoExtractor(
      extraInfo => {
        val key = s"${extraInfo.request.getUrl}${extraInfo.response.statusCode.getOrElse(0)}${extraInfo.requestName}"
        extraInfo.status == KO || extraInfo.response.statusCode.getOrElse(0) != 200 && extraInfoMap.contains(key).unary_! match {
          case true =>
            extraInfoMap.put(
              key,
              s"Request info:\n ${extraInfo.request.toString.replaceAll("\t", "\n ")}\n\n" +
                s"Response info:\n ${extraInfo.response.toString.replaceAll("\t", "\n ")}\n\n\n" +
                s"${extraInfo.session}"
            )
            List(key, extraInfoMap(key))
          case false => Nil
        }
      }
    )

  }

}