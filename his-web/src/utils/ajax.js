export default function ajax(options) {
  var baseUrl = 'http://localhost:19196'
  if (options.type == null) {
    options.type = "POST";
  }

  if (options.url == null) {
    options.url = "";
  }

  if (options.timeout == null) {
    options.timeout = 5000;
  }

  if (options.onComplate == null) {
    options.onComplate = function () {}
  }

  if (options.onError == null) {
    options.onError = function () {}
  }

  if (options.onSuccess == null) {
    options.onSuccess = function () {}
  }

  if (options.data) {
    options.data = "";
  }

  var xml;
  if (typeof ActiveXObject != 'undefined') {
    var aVersions = ["Microsoft.XMLHTTP", "Msxml2.XMLHttp.6.0", "Msxml2.XMLHttp.5.0", "Msxml2.XMLHttp.4.0",
      "Msxml2.XMLHttp.3.0"
    ];
    for (var i = 0; i < aVersions.length; i++) {
      try {
        xml = new ActiveXObject(aVersions[i]);
      } catch (e) {}
    }
  } else if (typeof XMLHttpRequest != 'undefined') {
    xml = new XMLHttpRequest();
  }
  xml.open(options.type, baseUrl + options.url, true);

  var timeoutLength = options.timeout;

  var requestDone = false;

  setTimeout(function () {
    requestDone = true;
  }, timeoutLength);

  xml.onreadystatechange = function () {

    if (xml.readyState == 4 && !requestDone) {

      if (httpSuccess(xml)) {

        options.onSuccess(httpData(xml));
      } else {
        options.onError();
      }

      options.onComplate();

      xml = null;
    }
  };
  xml.send();

  function httpSuccess(r) {
    try {
      return !r.status && location.protocol === "file:" ||
        (r.status >= 200 && r.status <= 300) ||
        r.status === 304 ||

        navigator.userAgent.indexOf("Safari") >= 0 &&
        typeof r.status === "undefined";
    } catch (e) {}
    return false;
  }

  function httpData(r) {
    var ct = r.getResponseHeader("responseType");
    if (ct) {
      if (ct === "script") {
        eval.call(window, data);
      }
      if (ct === "xml") {
        return r.responseXML;
      }
      if (ct === "json") {
        return JSON.parse(r.responseText);
      }
    }
    return r.responseText;
  }
}

export function connect(success) {
  var options = new Object();
  options.type = "GET";
  options.url = "/OpenDevice";
  options.timeout = 5000;
  options.onSuccess = success;
  ajax(options);
}

export function readCert(success) {
  var startDt = new Date();
  var options = new Object();
  options.type = "GET";
  options.url = "/readcard";
  options.timeout = 5000;
  options.onSuccess = success;
  ajax(options);
}
