package kim.uno.recyclerview.data

import kim.uno.recyclerview.widget.ContentsDiff
import kim.uno.recyclerview.widget.ItemDiff

data class Model(

    @ItemDiff
    var title: String,

    @ContentsDiff
    var description: String

)