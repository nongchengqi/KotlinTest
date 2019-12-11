package com.evan.online.bean

data class SearchBean(var code:Int,
                      var msg:String,
                      var timestamp:Long,
                      var data:List<MusicBean>)