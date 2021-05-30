package com.intertech.radioplayer.data.model

class RadioPlayList : ArrayList<RadioPlayList.RadioPlayListItem>(){
    data class RadioPlayListItem(
        var album: String = "",
        var artist: String = "", // CHRIS REA
        var image_url: String = "", // https://is5-ssl.mzstatic.com/image/thumb/Music124/v4/0b/d8/d7/0bd8d7fc-beac-9fba-ac59-32f570f1b03f/source/100x100bb.jpg
        var link_url: String = "",
        var name: String = "", // ON THE BEACH
        var played_at: String = "",
        var preview_url: String = "",
        var sid: String = ""
    )
}