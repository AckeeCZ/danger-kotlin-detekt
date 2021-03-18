[ ![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.ackeecz/danger-kotlin-detekt/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.ackeecz/danger-kotlin-detekt)

# danger-kotlin detekt plugin

Plugin for [danger-kotlin](https://github.com/danger/kotlin) processing outputs
of [detekt](https://github.com/detekt/detekt) tool

## Installation

Put

```kotlin
@file:DependsOn("io.github.ackeecz:danger-kotlin-detekt:x.y.z")
```

to the top of your Dangerfile

## Usage

First you need to register the plugin via

```kotlin
register plugin DetektPlugin
```

and then you can use it through it's single public method

```kotlin
DetektPlugin.parseAndReport(detektReportFile)
```

`parseAndReport` method accepts varargs of files pointing to the detekt reports.

Example Dangerfile

```kotlin
@file:DependsOn("io.github.ackeecz:danger-kotlin-detekt:x.y.z")

import io.github.ackeecz.danger.detekt.DetektPlugin

import systems.danger.kotlin.danger
import systems.danger.kotlin.register

import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.BiPredicate
import java.util.stream.Collectors

register plugin DetektPlugin

danger(args) {
    val detektReports = Files.find(Paths.get(""), 10, BiPredicate { path, attributes ->
        val fileName = path.toFile().name
        fileName.endsWith("detekt.xml")
    }).map { it.toFile() }.collect(Collectors.toList())

    DetektPlugin.parseAndReport(*detektReports.toTypedArray())
}
```

This will find all files in the depth of 10 relative to current directory that ends with `detekt.xml`
and it will pass them to the plugin.

## Customization

Currently, there is no customization of reports. It sends inline commit to the git server directly. When some
customization is needed it will be added.
