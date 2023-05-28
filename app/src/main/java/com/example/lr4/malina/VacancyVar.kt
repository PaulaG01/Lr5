package com.example.lr4.malina

import com.fasterxml.jackson.annotation.JsonIgnore

class VacancyVar {
    @JsonIgnore
    private val cities: Map<String, Int> = mapOf("Minsk" to 0, "Krakow" to 1, "Moscow" to 2)
    @JsonIgnore
    private val positions: Map<String, Int> = mapOf("Junior" to 0, "Middle" to 1, "Senior" to 2)

    @JsonIgnore
    private val positionImages: Map<String, Int> = mapOf(
        "Junior" to 0,
        "Middle" to 1,
        "Senior" to 2
    )

    var id: Int = 0
    var title: String = ""
    var city: String = ""
//    slogan
    var days: Any = 0
    var photo: String = ""


    var position: String = ""
    var skills: List<Skill> = listOf()

    fun getCities():Map<String, Int>{
        return cities;
    }

    fun getPositions():Map<String, Int>{
        return positions;
    }

    fun getPositionsImages(): Map<String, Int> {
        return positionImages;
    }

    fun resetData() {
        title = ""
        days = 0
        city = ""
        position = ""
        skills = listOf()
    }
}