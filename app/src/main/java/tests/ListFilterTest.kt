package tests

import com.example.myapplication.ListFilter
import java.net.URL

class ListFilterTest {
    //check what's being pulled
    fun getPulls(){
        var testList: ListFilter = ListFilter("")
        println(testList)
    }

}

fun main(args : Array<String>) {
    val testList: ListFilter = ListFilter("Pre+IPO")
    println(testList.information)
}