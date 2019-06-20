package Entity

class Contacts internal constructor(name: String, picId: Int) {
    var name = ""
        internal set
    var picId = 0
        internal set

    init {
        this.name = name
        this.picId = picId
    }
}