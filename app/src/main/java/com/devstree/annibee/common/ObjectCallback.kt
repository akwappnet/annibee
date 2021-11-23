package com.devstree.annibee.common

interface ObjectCallback<T> {
    fun response(obj: T?)
}