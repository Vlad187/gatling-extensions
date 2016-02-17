package debug

import io.gatling.core.result.message.KO
import io.gatling.http.request.ExtraInfo
import io.gatling.http.request.builder.HttpRequestWithParamsBuilder

import scala.collection.mutable
import scala.util.Try

object RequestExtraInfo {
  val synchronizedList = new scala.collection.mutable.ArrayBuffer[String]() with scala.collection.mutable.SynchronizedBuffer[String]
  if (synchronizedList.nonEmpty) ""
  else {
    synchronizedList += "<style>\ntable{table-layout:auto; word-break:break-all;}\nbody { font-family:Calibri; font-size:small; background-color:white; }\nfont { font-family:Calibri; }\nul { padding-top:0px; margin-top:0px; font-family:Calibri; font-size:small; }\nli { padding-top:0px; margin-top:0px; font-family:Calibri; font-size:11pt; }\np { font-family:Calibri; font-size:11pt; }\na { font-family:Calibri; font-size:11pt; color:black; }\nb { font-family:Calibri; font-size:11pt; }\nth { background-color:#4F81BD; color:#eeeeee; font-size:10.5pt; }\ntr { line-height:14pt; }\ntd { border-width:1px; border-bottom-color:#C0C0C0; border-bottom-style:solid; font-size:11pt; }\ntd.runInfo { white-space:nowrap; font-size:11pt; color:#1F497D; border-color:#C0C0C0; border-bottom-style:solid; border-right-style:solid; padding:2px; }\nspan { background-color:#FFFFFF; border-style:solid; border-width:1px; border-color:#bbbbbb; display:inline-block; }\n.AwrComparisonTable TD, .AwrComparisonTable TH { font-size:9pt; white-space: nowrap; }\n.AwrComparisonTable A { font-size:9pt; }\n.AwrComparisonTableTdId { text-align:center; }\n</style>"
  }
}

class RequestExtraInfo(val extraInfoSet: mutable.HashSet[String] = new mutable.HashSet[String]() with scala.collection.mutable.SynchronizedSet[String]) {

  implicit class ExtraInfoUtils(requestBuilder: HttpRequestWithParamsBuilder) {

    def saveFirstKoExtraInfo(alternateId: String): HttpRequestWithParamsBuilder = requestBuilder.extraInfoExtractor(
      extraInfo => {
        val key =
          if (alternateId.nonEmpty) {
            s"$alternateId${extraInfo.response.statusCode.getOrElse(0)}${extraInfo.requestName}"
          } else {
            s"${extraInfo.request.getUrl}${extraInfo.response.statusCode.getOrElse(0)}${extraInfo.requestName}"
          }
        extraInfo.status == KO match {
          case true =>
            synchronized {
              if (!extraInfoSet.contains(key)) {
                extraInfoSet.add(key)
                RequestExtraInfo.synchronizedList += extraInfoFormatter(extraInfo)
              }
            }
            Nil
          case false => Nil
        }
      }
    )

    def extraInfoFormatter(extraInfo: ExtraInfo): String = {
      <div>
        <br/>
        <table style="border-style: solid; border-width: thin medium; border-color: gray; background-color: white; width:100%">
          <tr>
            <td>
              <a name={extraInfo.requestName}>
                <b>
                  {Try {
                  extraInfo.requestName
                }.getOrElse(Const.NO_DATA_MASSAGE)}
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
                  <th>
                    <center>Title</center>
                  </th>
                  <th>
                    <center>Data</center>
                  </th>
                </tr>
                <tr>
                  <th>Method</th>
                  <td>
                    {Try {
                    extraInfo.request.getMethod.replaceAll("\t", "\n ")
                  }.getOrElse(Const.NO_DATA_MASSAGE)}
                  </td>
                </tr>
                <tr>
                  <th>Url</th>
                  <td>
                    {Try {
                    extraInfo.request.getUrl.replaceAll("\t", "\n ")
                  }.getOrElse(Const.NO_DATA_MASSAGE)}
                  </td>
                </tr>
                <tr>
                  <th>Headers</th>
                  <td>
                    {Try {
                    extraInfo.request.getHeaders.toString.replaceAll("\t", "\n ")
                  }.getOrElse(Const.NO_DATA_MASSAGE)}
                  </td>
                </tr>
                <tr>
                  <th>Form Params</th>
                  <td>
                    {Try {
                    extraInfo.request.getFormParams.toString.replaceAll("\t", "\n ")
                  }.getOrElse(Const.NO_DATA_MASSAGE)}
                  </td>
                </tr>
                <tr>
                  <th>String Data</th>
                  <td>
                    {Try {
                    extraInfo.request.getStringData.replaceAll("\t", "\n ")
                  }.getOrElse(Const.NO_DATA_MASSAGE)}
                  </td>
                </tr>
                <tr>
                  <th>Query Params</th>
                  <td>
                    {Try {
                    extraInfo.request.getQueryParams.toArray.mkString("\n")
                  }.getOrElse(Const.NO_DATA_MASSAGE)}
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
                    <th>
                      <center>Title</center>
                    </th>
                    <th>
                      <center>Data</center>
                    </th>
                  </tr>
                  <tr>
                    <th>Status Code</th>
                    <td>
                      {Try {
                      extraInfo.response.statusCode
                    }.getOrElse(Const.NO_DATA_MASSAGE)}
                    </td>
                  </tr>
                  <tr>
                    <th>Response Time In Millis</th>
                    <td>
                      {Try {
                      extraInfo.response.responseTimeInMillis
                    }.getOrElse(Const.NO_DATA_MASSAGE)}
                    </td>
                  </tr>
                  <tr>
                    <th>Is Redirect</th>
                    <td>
                      {Try {
                      extraInfo.response.isRedirect
                    }.getOrElse(Const.NO_DATA_MASSAGE)}
                    </td>
                  </tr>
                  <tr>
                    <th>Headers</th>
                    <td>
                      {Try {
                      extraInfo.response.headers
                    }.getOrElse(Const.NO_DATA_MASSAGE)}
                    </td>
                  </tr>
                  <tr>
                    <th>Body</th>
                    <td>
                      {Try {
                      scala.io.Source.fromInputStream(extraInfo.response.body.stream).mkString
                    }.getOrElse(Const.NO_DATA_MASSAGE)}
                    </td>
                  </tr>
                  <tr>
                    <th>Cookies</th>
                    <td>
                      {Try {
                      extraInfo.response.cookies
                    }.getOrElse(Const.NO_DATA_MASSAGE)}
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
                    <th>
                      <center>Title</center>
                    </th>
                    <th>
                      <center>Data</center>
                    </th>
                  </tr>
                  <tr>
                    <th>Attributes</th>
                    <td>
                      {Try {
                      extraInfo.session.attributes.toString()
                    }.getOrElse(Const.NO_DATA_MASSAGE)}
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