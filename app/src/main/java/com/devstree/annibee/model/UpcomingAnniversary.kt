package com.devstree.annibee.model

data class UpcomingAnniversary(
    var id: Int,
    val year: String,
    val detail: List<UpcomingAnniversaryDetail>
)

data class UpcomingAnniversaryDetail(
    var id: Int,
    val month: String,
    val day: String,
    val title: String,
    val name: String
)

