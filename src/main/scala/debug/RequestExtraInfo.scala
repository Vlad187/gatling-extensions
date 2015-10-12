package debug

import io.gatling.core.result.message.KO
import io.gatling.http.request.ExtraInfo
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
              extraInfoFormatter(extraInfo)
            )
            List(key, extraInfoMap(key))
          case false => Nil
        }
      }
    )

    def extraInfoFormatter(extraInfo: ExtraInfo): String =
      <div>
        <br/>
        <table border="1" style="width:100%">
          <tr>
            <td>
              <a name={extraInfo.requestName}>
                <b>
                  {extraInfo.requestName}
                </b>
              </a>
            </td>
          </tr>
          <tr>
            <td>
              <table border="1">
                <tr>
                  <th colspan="2">
                    <center>
                      <b>Request Info</b>
                    </center>
                  </th>
                </tr>
                <tr>
                  <th>
                    <center>Title</center>
                  </th>
                  <th>
                    <center>Data</center>
                  </th>
                </tr>
                <tr>
                  <td>Method</td>
                  <td>
                    {extraInfo.request.getMethod.replaceAll("\t", "\n ")}
                  </td>
                </tr>
                <tr>
                  <td>Url</td>
                  <td>
                    {extraInfo.request.getUrl.replaceAll("\t", "\n ")}
                  </td>
                </tr>
                <tr>
                  <td>Headers</td>
                  <td>
                    {extraInfo.request.getHeaders.toString.replaceAll("\t", "\n ")}
                  </td>
                </tr>
                <tr>
                  <td>Form Params</td>
                  <td>
                    {extraInfo.request.getFormParams.toString.replaceAll("\t", "\n ")}
                  </td>
                </tr>
                <tr>
                  <td>String Data</td>
                  <td>
                    {extraInfo.request.getStringData.replaceAll("\t", "\n ")}
                  </td>
                </tr>
                <tr>
                  <td>Query Params</td>
                  <td>
                    {extraInfo.request.getQueryParams.toString.replaceAll("\t", "\n ")}
                  </td>
                </tr>
              </table>
            </td>
            <tr>
              <td>
                <table border="1">
                  <tr>
                    <th colspan="2">
                      <center>
                        <b>Response Info</b>
                      </center>
                    </th>
                  </tr>
                  <tr>
                    <th>
                      <center>Title</center>
                    </th>
                    <th>
                      <center>Data</center>
                    </th>
                  </tr>
                  <tr>
                    <td>Status Code</td>
                    <td>
                      {extraInfo.response.statusCode}
                    </td>
                  </tr>
                  <tr>
                    <td>Response Time In Millis</td>
                    <td>
                      {extraInfo.response.responseTimeInMillis}
                    </td>
                  </tr>
                  <tr>
                    <td>Is Redirect</td>
                    <td>
                      {extraInfo.response.isRedirect}
                    </td>
                  </tr>
                  <tr>
                    <td>Headers</td>
                    <td>
                      {extraInfo.response.headers}
                    </td>
                  </tr>
                  <tr>
                    <td>Body</td>
                    <td>
                      {extraInfo.response.body}
                    </td>
                  </tr>
                  <tr>
                    <td>Cookies</td>
                    <td>
                      {extraInfo.response.cookies}
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td>
                <table border="1">
                  <tr>
                    <th colspan="2">
                      <center>
                        <b>Session Info</b>
                      </center>
                    </th>
                  </tr>
                  <tr>
                    <th>
                      <center>Title</center>
                    </th>
                    <th>
                      <center>Data</center>
                    </th>
                  </tr>
                  <tr>
                    <td>Attributes</td>
                    <td>
                      {extraInfo.session.attributes}
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </tr>
        </table>
        <br/>
      </div>.toString()

  }

}