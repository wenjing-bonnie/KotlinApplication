package com.wj.kotlin

/**
 * create by wenjing.liu at 2022/3/16
 */

fun <E> Iterable<E>.randomItemValue() = this.shuffled().first()

fun <E> Iterable<E>.randomItemValuePrintln() = println(this.shuffled().first())