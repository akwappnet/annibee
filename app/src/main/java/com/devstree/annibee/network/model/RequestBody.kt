package com.devstree.annibee.network.model

open class RequestBody() {
    var parameters = hashMapOf<String, Any>()

    constructor(map: HashMap<String, Any>) : this() {
        this.parameters = map
    }

    fun add(key: String, value: Any) {
        parameters[key] = value
    }

    fun remove(key: String) {
        parameters.remove(key)
    }
}