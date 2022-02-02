package ru.artsto.diary.models

import android.os.Parcelable
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
data class TaskModel(
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
):Parcelable