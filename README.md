# Core
A forge server mod used to create Minigame Servers.

Depends on <a href="https://github.com/LCLPYT/CoreBase">CoreBase</a>.

<hr>

### Using Core in your project
Of course, you are welcome to use Core in your own projects.
<br>
To do this, just add this repository to your build.gradle:
```groovy
repositories {
    maven { url 'https://repo.lclpnet.work/repository/internal' }
}
```
Now add the following dependencies:
```groovy
dependencies {
    implementation fg.deobf("work.lclpnet.mods:CoreBase:VERSION_COREBASE") // required by LCLPMMO
    implementation fg.deobf("work.lclpnet.mods:Core:VERSION")
}
```
You need to replace `VERSION` with the version you want to use.
To see all versions available, you can [check the repository](https://repo.lclpnet.work/#artifact~internal/work.lclpnet.mods/Core).<br>
<br>
Please note that `VERSION_COREBASE` should match the version required of your target LCLPMMO build. To find the correct version, please check the [gradle.properties file](https://github.com/LCLPYT/Core/blob/master/gradle.properties), in which `corebase_version` specifies the required version. Just keep in mind to find the correct commit if you are not using the latest version.
