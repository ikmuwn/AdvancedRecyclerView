package kim.uno.recyclerview.ui.main

import kim.uno.recyclerview.widget.ContentsDiffField
import kim.uno.recyclerview.widget.ItemDiffField

data class Model(

    @ItemDiffField
    @ContentsDiffField
    var title: String,

    @ContentsDiffField
    var description: String

)