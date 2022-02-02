package ru.artsto.diary.compose

import android.annotation.SuppressLint
import android.widget.CalendarView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.launch
import ru.artsto.diary.realmModels.Task
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TaskScreen(
    setOnDateChangeListener: (Calendar) -> Unit,
    listTask: List<Task>,
    onClickButton: (Task) -> Unit,
    searchTask: State<Task>,
    replaceTask: (Task) -> Unit
){
    val snackBarCoroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //наименование задачи
            val name = remember { mutableStateOf("") }
            //описание задачи
            val description = remember { mutableStateOf("") }
            val index = remember { mutableStateOf(0) }
            val calendar = remember {
                mutableStateOf(Calendar.getInstance())
            }
            val openDialog = remember {
                mutableStateOf(false)
            }

            if (searchTask.value.id>0){
                openDialog.value = true
            }

            if (listTask.isNotEmpty()) {

                //диалог
                if (openDialog.value){
                    //показываем диалог
                    AlertDialog(
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        title = {
                            Text(text = "Внимание!")
                        },
                        text={
                             Text(text = "Задача на данную дату и время уже существует, хотите перезаписать?")
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                openDialog.value = false
                                replaceTask(searchTask.value)
                            }) {
                                Text(text = "Да")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                searchTask.value.id = 0
                                openDialog.value = false }) {
                                Text(text = "Нет")
                            }
                        }

                    )
                }

                calendar.value.timeInMillis = listTask[0].date_start.toLong()*10000

                OutlinedTextFieldUI(label = "наименование", name = name)
                OutlinedTextFieldUI(label = "описание задачи", name = description)
                OutlinedTextFieldDateUI(
                    setOnDateChangeListener = setOnDateChangeListener,
                    calendar = calendar
                )

                val dropDownMenuList = mutableListOf<DDML>()
                listTask.forEach {
                    var hour = it.hour
                    if (it.name.isNotEmpty()) {
                        hour += "\n"
                        hour += it.name
                    }
                    dropDownMenuList.add(DDML(index = it.index, value = hour))
                }
                DropDownMenuUI(
                    list = dropDownMenuList,
                    index = index,
                    label = "время"
                )

                ButtonUI(textButton = "добавить", onClickButton = {

                    //проверка на наличие наименования дела
                    if (name.value.isNotEmpty()) {

                        val tk = Task(
                            name = name.value,
                            description = description.value,
                            date_start = listTask[index.value].date_start,
                            date_finish = listTask[index.value].date_finish
                        )
                        onClickButton(tk)
                    } else {
                        //показываем snackBar с сообщением
                        snackBarCoroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "заполните поле наименование и повторите попытку"
                            )
                        }
                    }

                })
            }
        }
    }

}
/*
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewTaskScreen(){
    val listTask = remember {
        mutableStateListOf<Task>()
    }
    TaskScreen(
        setOnDateChangeListener = {},
        listTask = listTask,
        onClickButton = {},
        searchTask = vmTaskView.searchTask
    )
}*/

@Composable
fun OutlinedTextFieldUI(label: String = "label", name: MutableState<String>){
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
        ,
        singleLine = true,
        value = name.value,
        onValueChange = { name.value = it},
        label = { Text(text = label) }
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewOutlinedTextFieldUI(){
    val name = remember {
        mutableStateOf("")
    }
    OutlinedTextFieldUI(name = name)
}

@Composable
fun OutlinedTextFieldDateUI(
    setOnDateChangeListener: (Calendar) -> Unit,
    calendar: MutableState<Calendar>
){

    val visibleCalendar = remember { mutableStateOf(false) }
    val enable = remember {
        mutableStateOf(true)
    }
    var value by remember {
        mutableStateOf(SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.value.timeInMillis))
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
        ,
        singleLine = true,
        value = value,
        onValueChange = {
            value = it
        },
        label = {
            Text(text = "дата")
        },
        trailingIcon = {
            IconButton(onClick = {
                visibleCalendar.value = true
            }) {
                Icon(Icons.Filled.DateRange, "")
            }
        },
        enabled = enable.value
    )
    if (visibleCalendar.value){
        AlertDialog(
            onDismissRequest = {
                visibleCalendar.value = false
            },
            title = {
                Text(text = "выбор даты события")
            },
            buttons = {
            },
            text = {
                AndroidView(
                    factory = { CalendarView(it) },
                    modifier = Modifier.wrapContentWidth(),
                    update = {view->
                        view.date = calendar.value.timeInMillis
                        view.setOnDateChangeListener {_, year, month, dayOfMonth ->
                            val cal = Calendar.getInstance()
                            cal.set(year, month, dayOfMonth)
                            calendar.value = cal
                            value = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.value.timeInMillis)
                            setOnDateChangeListener(cal)
                            visibleCalendar.value = false
                        }
                    }
                )
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewOutlinedTextFieldDateUI(){
    val calendar = remember {
        mutableStateOf(Calendar.getInstance())
    }
    OutlinedTextFieldDateUI(setOnDateChangeListener = {}, calendar = calendar)
}

@Composable
fun DropDownMenuUI(
    list: List<DDML>,
    label: String = "список значений",
    index: MutableState<Int>,
) {

    //сохраняем состояние открыто/закрыто меню
    var expanded by remember {
        mutableStateOf(false)
    }

    //начальное значение из списка
    var value by remember {
        mutableStateOf(list[index.value])
    }

    var textFieldSize by remember { mutableStateOf(Size.Zero)}

    //для использования иконок материального дизайна (Icons.Filled.)
    //необходимо добавить зависимость в gradle модуля
    //implementation "androidx.compose.material:material-icons-extended:$compose_version"
    val icon = if (expanded) { Icons.Filled.ArrowDropUp }
    else{ Icons.Filled.ArrowDropDown }

    val focusManager = LocalFocusManager.current

    Column(
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .onFocusChanged {
                    expanded = it.isFocused
                },
            readOnly = true,
            value = value.value,
            onValueChange = {
                value.value = it
            },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            },
            label = { Text(text = label) }
        )

        DropdownMenu(
            modifier = Modifier
                .width(
                    with(LocalDensity.current) {
                        textFieldSize.width.toDp()
                    }
                )
                .heightIn(min = 50.dp, max = 300.dp),
            //открытие/закрытие меню
            expanded = expanded,
            //событие при клике за пределами списка
            onDismissRequest = {
                expanded = false
                focusManager.clearFocus()
            }
        ) {
            //формируем список значений
            list.forEach { item ->
                DropdownMenuItem(

                    onClick = {
                        expanded = false
                        //при выборе элемента выводим значение в OutlinedTextField
                        index.value = item.index
                        value = item
                        focusManager.clearFocus()
                    }
                ) {

                    Text(text = item.value)
                    Divider(thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun ButtonUI(
    onClickButton:()->Unit,
    textButton:String
){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
        ,
        onClick = {
            onClickButton()
        }) {

        Text(text = textButton.uppercase(Locale.getDefault()))
    }
}

@Composable
@Preview
fun PreviewButtonUI(){
    ButtonUI(onClickButton = {}, textButton = "Text")
}