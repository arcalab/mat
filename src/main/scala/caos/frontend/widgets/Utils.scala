package caos.frontend.widgets

object Utils {
  def downloadSvg(element:String): Unit = {
    scalajs.js.eval(
      s"""svgEl = document.getElementById("$element");
         |name = "circuit.svg";
         |
         |svgEl.setAttribute("xmlns", "http://www.w3.org/2000/svg");
         |var svgData = svgEl.outerHTML;
         |
         |// Firefox, Safari root NS issue fix
         |svgData = svgData.replace('xlink=', 'xmlns:xlink=');
         |// Safari xlink NS issue fix
         |//svgData = svgData.replace(/NS\\d+:href/gi, 'xlink:href');
         |svgData = svgData.replace(/NS\\d+:href/gi, 'href');
         |// drop "stroke-dasharray: 1px, 0px;"
         |svgData = svgData.replace(/stroke-dasharray: 1px, 0px;/gi, '');
         |
         |var preface = '<?xml version="1.0" standalone="no"?>\\r\\n';
         |var svgBlob = new Blob([preface, svgData], {type:"image/svg+xml;charset=utf-8"});
         |var svgUrl = URL.createObjectURL(svgBlob);
         |var downloadLink = document.createElement("a");
         |downloadLink.href = svgUrl;
         |downloadLink.download = name;
         |document.body.appendChild(downloadLink);
         |downloadLink.click();
         |document.body.removeChild(downloadLink);
      """.stripMargin)
  }

  def downloadTxt(content:String,fileName:String): Unit = {
    scalajs.js.eval(
      s"""var data = "$content";
         |var c = document.createElement("a");
         |c.download = "$fileName";
         |
         |var t = new Blob([data], {
         |type: "text/plain"
         |});
         |c.href = window.URL.createObjectURL(t);
         |c.click();
      """.stripMargin)
  }

  def uploadTxt(): Unit = {
    scalajs.js.eval(
      s"""
         |var inputEl = document.createElement("input");
         |var rid = "r"+Math.random().toString(16).substr(2, 8); // 8 random characters
         |inputEl.type = "file";
         |inputEl.id = rid;
         |inputEl.setAttribute("id", rid);
         |inputEl.setAttribute("onchange", "startRead()");
         |inputEl.setAttribute("style", "display: none;");
         |
         |function startRead(evt) {
         |    var file = document.getElementById(rid).files[0];
         |    if (file) {
         |        getFileAsText(file); // handled by Scala exported function
         |        document.body.removeChild(inputEl);
         |    }
         |}
         |
         |document.body.appendChild(inputEl);
         |inputEl.click(); // when cancelled the element is not removed...
         |//document.body.removeChild(inputEl); // if removed here, it no longer exists when "startRead".
         """.stripMargin)
  }


  def uploadTxtOld(): Unit = {
    scalajs.js.eval(
      s"""
         |var inputEl = document.createElement("input");
         |inputEl.type = "file";
         |inputEl.id = "examplesInput";
         |inputEl.setAttribute("id", "examplesInput");
         |inputEl.setAttribute("onchange", "startRead()");
         |console.log("bbb");
         |
         |function startRead(evt) {
         |    console.log("aaa");
         |    var x = document.getElementById('examplesInput');
         |    var file = document.getElementById('examplesInput').files[0];
         |    if (file) {
         |        console.log("Name: " + file.name + "\\n" + "Last Modified Date :" + file.lastModifiedDate);
         |        getFileAsText(file);
         |        console.log("---- getAsText(file)");
         |        document.body.removeChild(inputEl);
         |    }
         |}
         |function getAsText(readFile) {
         |    var reader = new FileReader();
         |    reader.readAsText(readFile, "UTF-8");
         |    reader.onload = loadedFile;
         |}
         |function loaded(evt) {
         |    alert("File Loaded Successfully");
         |    var fileString = evt.target.result;
         |    console.log(fileString);
         |    $$("#op").text(fileString);
         |}
         |
         |document.body.appendChild(inputEl);
         |inputEl.click();
         |// document.body.removeChild(inputEl);
         """.stripMargin)
  }

  def resizeCols:Unit = {
    scalajs.js.eval(
      """var dragging = false;
        |
        |// $('#dragbar').css("margin-left", (window.innerWidth*0.25-7) + "px");
        |$('#dragbar').css("margin-left", (25-((4/window.innerWidth)*100)) + "%");
        |
        |$('#dragbar').mousedown(function(e){
        |    e.preventDefault();
        |    dragging = true;
        |    var main = $('#rightbar');
        |    var ghostbar = $('<div>',
        |        {id:'ghostbar',
        |            css: {
        |                height: "100%", //main.outerHeight(),
        |                top: "0", //main.offset().top,
        |                left: main.offset().left
        |            }
        |        }).appendTo('body');
        |
        |    $(document).mousemove(function(e){
        |        ghostbar.css("left",e.pageX-2);
        |    });
        |});
        |$(document).mouseup(function(e){
        |    if (dragging)
        |    {
        |        var percentage = ((e.pageX-2) / window.innerWidth) * 100;
        |        var percentagec = ((e.pageX-5) / window.innerWidth) * 100;
        |        var mainPercentage = 100-percentage;
        |        // $('#console').text("side:" + percentage + " main:" + mainPercentage);
        |
        |        $('#leftbar').css("width",percentage + "%");
        |        $('#rightbar').css("width",mainPercentage + "%");
        |        $('#dragbar').css("margin-left", percentagec + "%");
        |                                    //e.pageX-9 + "px");//(percentageDB) + "%");
        |        $('#ghostbar').remove();
        |        $(document).unbind('mousemove');
        |        dragging = false;
        |    }
        |});""".stripMargin
    )
  }
}
