package com.example.lr4.malina

import android.app.Application
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference

class Vacancy : Application() {
    var vacancyVar: VacancyVar = VacancyVar()
}
