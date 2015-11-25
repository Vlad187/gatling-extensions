package debuger

import io.gatling.core.result.message.KO
import io.gatling.http.request.ExtraInfo
import io.gatling.http.request.builder.HttpRequestWithParamsBuilder

import scala.collection.mutable

object ExtraInfoStyle {
  var isStyleAdded = false

  def apply() =
    if (isStyleAdded) ""
    else "<style>\nbody { font-family:Calibri; font-size:small; background-color:white; }\nfont { font-family:Calibri; }\nul { padding-top:0px; margin-top:0px; font-family:Calibri; font-size:small; }\nli { padding-top:0px; margin-top:0px; font-family:Calibri; font-size:11pt; }\np { font-family:Calibri; font-size:11pt; }\na { font-family:Calibri; font-size:11pt; color:black; }\nb { font-family:Calibri; font-size:11pt; }\nth { background-color:#4F81BD; color:#eeeeee; font-size:10.5pt; }\ntr { line-height:14pt; }\ntd { border-width:1px; border-bottom-color:#C0C0C0; border-bottom-style:solid; font-size:11pt; }\ntd.runInfo { white-space:nowrap; font-size:11pt; color:#1F497D; border-color:#C0C0C0; border-bottom-style:solid; border-right-style:solid; padding:2px; }\nspan { background-color:#FFFFFF; border-style:solid; border-width:1px; border-color:#bbbbbb; display:inline-block; }\n.AwrComparisonTable TD, .AwrComparisonTable TH { font-size:9pt; white-space: nowrap; }\n.AwrComparisonTable A { font-size:9pt; }\n.AwrComparisonTableTdId { text-align:center; }\n</style>"

}

class RequestExtraInfo(val extraInfoSet: mutable.HashSet[String] = new mutable.HashSet[String]() with scala.collection.mutable.SynchronizedSet[String]) {

  implicit class ExtraInfoUtils(requestBuilder: HttpRequestWithParamsBuilder) {

    def saveFirstKoExtraInfo(): HttpRequestWithParamsBuilder = requestBuilder.extraInfoExtractor(
      extraInfo => {
        val key = s"${extraInfo.request.getUrl}${extraInfo.response.statusCode.getOrElse(0)}${extraInfo.requestName}"
        extraInfo.status == KO && !extraInfoSet.contains(key) match {
          case true =>
            extraInfoSet.add(key)
            List(ExtraInfoStyle(), extraInfoFormatter(extraInfo))
          case false => Nil
        }
      }
    )

    def extraInfoFormatter(extraInfo: ExtraInfo): String = {
      ExtraInfoStyle.isStyleAdded = true
      <div>
        <br/>
        <table style="border-style: solid; border-width: thin medium; border-color: gray; background-color: white; width:100%">
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
              <table style="border-style: solid; border-width: thin medium; border-color: gray; background-color: white; width:100%">
                <tr>
                  <th colspan="2">
                    <center>
                      <b>Request Info</b>
                    </center>
                  </th>
                </tr>
                <tr>
                  <th width="8%">
                    <center>Title</center>
                  </th>
                  <th>
                    <center>Data</center>
                  </th>
                </tr>
                <tr>
                  <th>Method</th>
                  <td>
                    {extraInfo.request.getMethod.replaceAll("\t", "\n ")}
                  </td>
                </tr>
                <tr>
                  <th>Url</th>
                  <td>
                    {extraInfo.request.getUrl.replaceAll("\t", "\n ")}
                  </td>
                </tr>
                <tr>
                  <th>Headers</th>
                  <td>
                    {extraInfo.request.getHeaders.toString.replaceAll("\t", "\n ")}
                  </td>
                </tr>
                <tr>
                  <th>Form Params</th>
                  <td>
                    {extraInfo.request.getFormParams.toString.replaceAll("\t", "\n ")}
                  </td>
                </tr>
                <tr>
                  <th>String Data</th>
                  <td>
                    {extraInfo.request.getStringData.replaceAll("\t", "\n ")}
                  </td>
                </tr>
                <tr>
                  <th>Query Params</th>
                  <td>
                    {extraInfo.request.getQueryParams.toArray.mkString("\n")}
                  </td>
                </tr>
              </table>
            </td>
            <tr>
              <td>
                <table style="border-style: solid; border-width: thin medium; border-color: gray; background-color: white; width:100%">
                  <tr>
                    <th colspan="2">
                      <center>
                        <b>Response Info</b>
                      </center>
                    </th>
                  </tr>
                  <tr>
                    <th width="8%">
                      <center>Title</center>
                    </th>
                    <th>
                      <center>Data</center>
                    </th>
                  </tr>
                  <tr>
                    <th>Status Code</th>
                    <td>
                      {extraInfo.response.statusCode}
                    </td>
                  </tr>
                  <tr>
                    <th>Response Time In Millis</th>
                    <td>
                      {extraInfo.response.responseTimeInMillis}
                    </td>
                  </tr>
                  <tr>
                    <th>Is Redirect</th>
                    <td>
                      {extraInfo.response.isRedirect}
                    </td>
                  </tr>
                  <tr>
                    <th>Headers</th>
                    <td>
                      {extraInfo.response.headers}
                    </td>
                  </tr>
                  <tr>
                    <th>Body</th>
                    <td>
                      {extraInfo.response.body}
                    </td>
                  </tr>
                  <tr>
                    <th>Cookies</th>
                    <td>
                      {extraInfo.response.cookies}
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td>
                <table style="border-style: solid; border-width: thin medium; border-color: gray; background-color: white; width:100%">
                  <tr>
                    <th colspan="2">
                      <center>
                        <b>Session Info</b>
                      </center>
                    </th>
                  </tr>
                  <tr>
                    <th width="8%">
                      <center>Title</center>
                    </th>
                    <th>
                      <center>Data</center>
                    </th>
                  </tr>
                  <tr>
                    <th>Attributes</th>
                    <td>
                      {extraInfo.session.attributes.dropRight(1)}
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

}