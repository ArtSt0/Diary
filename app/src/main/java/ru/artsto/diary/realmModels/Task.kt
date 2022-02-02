package ru.artsto.diary.realmModels

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

open class Task(
    @PrimaryKey
    var id:Int=0, //id дела
    var date_start:String="", //дата начала дела
    var date_finish:String="", //дата окончания дела
    var name: String = "", //наименование дела
    var description:String = "", //описание дела
    @Ignore
    var index:Int=0,
    @Ignore
    var hour:String = ""
): RealmObject()